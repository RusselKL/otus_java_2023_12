package ru.otus;

import ru.otus.proxy.LoggingProxyFactory;

public class Demo {
    public static void main(String[] args) {
        TestLogging proxy = LoggingProxyFactory.getProxyOf(TestLoggingImpl.class);
        proxy.calculation(2);
        proxy.calculation(5);
        proxy.calculation("first param, ", 3);
    }
}
