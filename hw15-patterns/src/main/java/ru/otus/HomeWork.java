package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.homework.ExceptionTimeProcessor;
import ru.otus.processor.homework.FieldsSwitcherProcessor;
import ru.otus.processor.homework.LazyLocalDateTimeProvider;

import java.util.ArrayList;
import java.util.List;

public class HomeWork {
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
        var processors = List.of(new FieldsSwitcherProcessor(), new ExceptionTimeProcessor(new LazyLocalDateTimeProvider()));

        var complexProcessor = new ComplexProcessor(processors, ex -> {
        });
        var historyListener = new HistoryListener(new ArrayList<>());
        complexProcessor.addListener(historyListener);

        var field13 = new ObjectForMessage();
        field13.setData(List.of("One", "Two"));

        var message1 = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field11("field11")
                .field12("field12")
                .field13(field13)
                .build();

        var result = complexProcessor.handle(message1);
        logger.info("result:{}", result);

        var message2 = new Message.Builder(2L)
                .field2("field2")
                .build();

        complexProcessor.handle(message2);
        var firstMsg = historyListener.findMessageById(1L);
        logger.info("firstMsg:{}", firstMsg);

        var secondMsg = historyListener.findMessageById(2L);
        logger.info("secondMsg:{}", secondMsg);

        complexProcessor.removeListener(historyListener);
    }
}
