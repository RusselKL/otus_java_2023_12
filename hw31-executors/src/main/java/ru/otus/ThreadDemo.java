package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadDemo {
    private static final Logger logger = LoggerFactory.getLogger(ThreadDemo.class);
    private String thread1 = "ID_1";
    private String thread2 = "ID_2";
    private String currentThread = thread1;

    public static void main(String[] args) {
        var demo = new ThreadDemo();
        var thread1 = Thread.ofPlatform().start(() -> demo.task(demo.thread1));
        var thread2 = Thread.ofPlatform().start(() -> demo.task(demo.thread2));
    }

    private synchronized void task(String id) {
        var count = 1;
        var reverse = false;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (id != currentThread) {
                    wait();
                }
                logger.info("{} : {} ", Thread.currentThread().getName(), count);
                count = reverse ? count - 1 : count + 1;
                switch (count) {
                    case 10:
                        reverse = true;
                        break;
                    case 1:
                        reverse = false;
                        break;
                }
                Thread.sleep(1000);
                currentThread = currentThread == thread1 ? thread2 : thread1;
                notify();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
