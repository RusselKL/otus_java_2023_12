package ru.otus.processor.homework;

import java.time.LocalDateTime;

public interface LocalDateTimeProvider {
    LocalDateTime getDate();

    int getSecond();
}
