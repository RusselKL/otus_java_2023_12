package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ExceptionTimeProcessor implements Processor {

    private final LocalDateTimeProvider dateTime;

    public ExceptionTimeProcessor(LocalDateTimeProvider dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public Message process(Message message) {
        if (dateTime.getSecond() % 2 == 0) {
            throw new RuntimeException("Process of message: " + message + " is at time with even second");
        }
        return message;
    }
}
