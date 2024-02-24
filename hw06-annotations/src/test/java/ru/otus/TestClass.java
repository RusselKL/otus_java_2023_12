package ru.otus;

import ru.otus.annotaions.After;
import ru.otus.annotaions.Before;
import ru.otus.annotaions.Test;

public class TestClass {

    @Before
    void setUp() {
        System.out.println("setUp");
    }

    @After
    void tearDown() {
        System.out.println("tearDown");
    }

    @Test
    void firstTest() {
        System.out.println("firstTest");
    }

    @Test
    void secondTest() {
        System.out.println("secondTest");
    }

    @Test
    void exceptionTest() {
        System.out.println("exceptionTest");
        throw new RuntimeException("Ooops you got exception");
    }

    @Test
    void thirdTest() {
        System.out.println("thirdTest");
    }

}
