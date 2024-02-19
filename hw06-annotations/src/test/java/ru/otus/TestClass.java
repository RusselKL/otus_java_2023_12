package ru.otus;

import ru.otus.annotaions.After;
import ru.otus.annotaions.Before;
import ru.otus.annotaions.Test;

public class TestClass {

    @Before
    void setUp() {
        System.out.println("setUp");
        throw new RuntimeException("Ooops in setUp");
    }

    @After
    void tearDown() {
        System.out.println("tearDown");
    }

    @Test
    void buildMessageTest() {
        System.out.println("buildMessageTest");
        throw new RuntimeException("Ooops in buildMessageTest");
    }

}
