package ru.otus;

public class DefaultMessageTemplateProvider implements MessageTemplateProvider {

    public static final String DEFAULT_TEMPLATE = "Hi!\n %s \n With best regards, %s";

    @Override
    public String getMessageTemplate(String templateName) {
        return DEFAULT_TEMPLATE;
    }
}
