package ru.otus.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.model.Measurement;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ResourcesFileLoader implements Loader {
    private static final Logger logger = LoggerFactory.getLogger(ResourcesFileLoader.class);
    private final String fileName;
    private final Gson gson = GsonProvider.getInstance();
    private final TypeToken<ArrayList<Measurement>> type = new TypeToken<>() {
    };

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        var loadResult = new ArrayList<Measurement>();
        try (var fileStream = ClassLoader.getSystemResourceAsStream(fileName)) {
            if (fileStream != null) {
                try (var reader = new InputStreamReader(fileStream)) {
                    loadResult = gson.fromJson(reader, type.getType());
                }
            } else {
                throw new FileNotFoundException();
            }
        } catch (FileNotFoundException e) {
            logger.atError().log("File not found exception: {}", e.getMessage());
        } catch (IOException e) {
            logger.atError().log("File load exception: {}", e.getMessage());
        }
        return loadResult;
    }
}
