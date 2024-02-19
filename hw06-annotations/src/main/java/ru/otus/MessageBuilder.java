package ru.otus;

public interface MessageBuilder {
    String buildMessage(String templateName, String text, String signature);
}
