package ru.otus.dataprocessor;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GsonProvider {
    private static final Logger logger = LoggerFactory.getLogger(GsonProvider.class);
    private static final Gson instance = new Gson();

    private GsonProvider() {
    }

    static Gson getInstance() {
        return instance;
    }
}
