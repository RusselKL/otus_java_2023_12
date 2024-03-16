package ru.otus;

import ru.otus.annotation.Log;

public class TestLoggingImpl implements TestLogging, TestLoggingOther {
    @Log
    @Override
    public void calculation(int param) {
        var result = param * 2;
        System.out.println("Calculation with params was done, result: " + result);
    }

    @Override
    public void calculation(String param1, int param2) {
        var result = param1 + param2;
        System.out.println("Calculation with params was done, result: " + result);
    }

    @Log
    public void calculation(int param1, int param2, String param3) {

    }

    @Log
    @Override
    public void calculation(String param1, String param2) {
        var result = param1 + param2;
        System.out.println("Calculation with params was done, result: " + result);
    }
}
