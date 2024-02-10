package ru.otus;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HelloOtus {
    public static void main(String[] args) {
        List<String> names = Lists.newArrayList("Ruslan", "Andrey", "Sergey");
        List<String> reversed = Lists.reverse(names);

        Logger logger = Logger.getLogger(HelloOtus.class.getName());

        logger.log(Level.INFO, "Reversed list: {0}", reversed);
    }
}
