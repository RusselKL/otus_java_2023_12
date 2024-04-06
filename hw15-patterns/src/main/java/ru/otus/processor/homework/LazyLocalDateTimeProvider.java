package ru.otus.processor.homework;

import java.time.LocalDateTime;

public class LazyLocalDateTimeProvider implements LocalDateTimeProvider {
    @Override
    public LocalDateTime getDate() {
        return LocalDateTime.now();
    }

    @Override
    public int getSecond() {
        return LocalDateTime.now().getSecond();
    }
}
