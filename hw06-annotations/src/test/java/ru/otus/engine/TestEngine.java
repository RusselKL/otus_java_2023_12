package ru.otus.engine;

import ru.otus.annotaions.After;
import ru.otus.annotaions.Before;
import ru.otus.annotaions.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestEngine {

    public static <T> void run(Class<T> clazz) {
        try {
            Method[] declaredMethods = clazz.getDeclaredMethods();
            Constructor<T> constructor = clazz.getConstructor();

            var annotatedMethods = getAnnotatedMethods(declaredMethods);
            List<Method> beforeMethods = annotatedMethods.get(Before.class);
            List<Method> testMethods = annotatedMethods.get(Test.class);
            List<Method> afterMethods = annotatedMethods.get(After.class);

            var passed = 0;
            for (Method test : testMethods) {
                var testClass = constructor.newInstance();
                executeMethods(beforeMethods, testClass);
                if (executeMethod(test, testClass)) passed++;
                executeMethods(afterMethods, testClass);
            }

            var total = testMethods.size();
            var failed = total - passed;

            System.out.printf("Total: %1$d\nPassed: %2$d\nFailed: %3$d", total, passed, failed);
        } catch (Exception exception) {
            System.out.println("Test run failed: " + exception.getMessage());
        }
    }

    private static Map<Class<? extends Annotation>, List<Method>> getAnnotatedMethods(Method[] declaredMethods) {
        Map<Class<? extends Annotation>, List<Method>> annotatedMethods = new HashMap<>();
        for (Method method : declaredMethods) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (!annotatedMethods.containsKey(annotation.annotationType())) {
                    var methods = new ArrayList<Method>();
                    methods.add(method);
                    annotatedMethods.put(annotation.annotationType(), methods);
                } else {
                    annotatedMethods.get(annotation.annotationType()).add(method);
                }
            }
        }
        return annotatedMethods;
    }

    private static <T> void executeMethods(
            List<Method> methods,
            T testClass
    ) {
        if (methods != null) {
            for (Method method : methods) {
                executeMethod(method, testClass);
            }
        }
    }

    private static <T> Boolean executeMethod(
            Method method,
            T testClass
    ) {
        try {
            method.setAccessible(true);
            method.invoke(testClass);
            return true;
        } catch (IllegalAccessException exception) {
            System.out.println("Method execution failed: " + exception.getMessage());
            return false;
        } catch (InvocationTargetException exception) {
            System.out.println("Method execution failed: " + exception.getTargetException().getMessage());
            return false;
        }
    }

}
