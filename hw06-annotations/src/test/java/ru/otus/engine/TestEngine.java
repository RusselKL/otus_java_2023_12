package ru.otus.engine;

import ru.otus.annotaions.After;
import ru.otus.annotaions.Before;
import ru.otus.annotaions.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestEngine {

    public static <T> void run(T testClass) {
        Class<?> clazz = testClass.getClass();
        try {
            Method[] declaredMethods = clazz.getDeclaredMethods();
            var annotatedMethods = getAnnotatedMethods(declaredMethods);

            List<Method> beforeMethods = annotatedMethods.get(Before.class);
            var beforeMethodsPassed = executeMethods(beforeMethods, testClass);

            List<Method> testMethods = annotatedMethods.get(Test.class);
            var testMethodsPassed = executeMethods(testMethods, testClass);

            List<Method> afterMethods = annotatedMethods.get(After.class);
            var afterMethodsPassed = executeMethods(afterMethods, testClass);

            var total = beforeMethods.size() + testMethods.size() + afterMethods.size();
            var passed = beforeMethodsPassed.size() + testMethodsPassed.size() + afterMethodsPassed.size();
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

    private static <T> List<Method> executeMethods(
            List<Method> methodsToExecute,
            T testClass
    ) {
        List<Method> methodsSucceed = new ArrayList<>();
        if (methodsToExecute != null) {
            for (Method method : methodsToExecute) {
                try {
                    method.setAccessible(true);
                    method.invoke(testClass);
                    methodsSucceed.add(method);
                } catch (IllegalAccessException exception) {
                    System.out.println("Method execution failed: " + exception.getMessage());
                } catch (InvocationTargetException exception) {
                    System.out.println("Method execution failed: " + exception.getTargetException().getMessage());
                }
            }
        }
        return methodsSucceed;
    }

}
