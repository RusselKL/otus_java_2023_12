package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

// Этот класс нужно реализовать
@SuppressWarnings({"java:S1068", "java:S125"})
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final BlockingQueue<SensorData> bufferData;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        this.bufferData = new PriorityBlockingQueue<>(
                bufferSize,
                Comparator.comparing(SensorData::getMeasurementTime)
        );
    }

    @Override
    public void process(SensorData data) {
        log.info("Проверка буфера");
        if (bufferData.size() >= bufferSize) {
            flush();
        }
        try {
            log.info("Запись данных в буфер: {}", data);
            bufferData.put(data);
            log.info("Количество данных в буфере после записи: {}", bufferData.size());
        } catch (Exception e) {
            log.error("Ошибка при записи в буфер", e);
        }
    }

    public synchronized void flush() {
        try {
            // Double check
            if (bufferData.isEmpty()) throw new IllegalStateException("Буфер был уже очищен");

            log.info("Буфер переполнен, текущий размер: {}", bufferData.size());
            log.info("Максимальный размер буфера: {}", bufferSize);

            var dataToFlush = new ArrayList<SensorData>(bufferSize);
            bufferData.drainTo(dataToFlush);
            log.info("Буфер очищен, текущий размер: {}", bufferData.size());

            writer.writeBufferedData(dataToFlush);
            log.info("Будет записано данных из буфера: {}", dataToFlush.size());
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }

}
