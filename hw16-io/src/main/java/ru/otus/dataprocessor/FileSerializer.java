package ru.otus.dataprocessor;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {
    private static final Logger logger = LoggerFactory.getLogger(FileSerializer.class);
    private final String fileName;
    private final Gson gson = GsonProvider.getInstance();

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        try (var writer = new FileWriter(fileName)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            logger.atError().log("Writing to file exception: {}", e.getMessage());
        }
    }
}
