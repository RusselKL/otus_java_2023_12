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
        Map<Class<? extends Annotation>, List<Method>> methodsForAnnotations = new HashMap<>();
        try {
            Method[] declaredMethods = clazz.getDeclaredMethods();
            getAnnotatedMethods(methodsForAnnotations, declaredMethods);

            executeMethods(methodsForAnnotations, testClass, Before.class);
            executeMethods(methodsForAnnotations, testClass, Test.class);
            executeMethods(methodsForAnnotations, testClass, After.class);
        } catch (Exception exception) {
            System.out.println("Test run failed: " + exception.getMessage());
        }
    }

    private static void getAnnotatedMethods(
            Map<Class<? extends Annotation>, List<Method>> methodsForAnnotations,
            Method[] declaredMethods
    ) {
        for (Method method : declaredMethods) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (!methodsForAnnotations.containsKey(annotation.annotationType())) {
                    var methods = new ArrayList<Method>();
                    methods.add(method);
                    methodsForAnnotations.put(annotation.annotationType(), methods);
                } else {
                    methodsForAnnotations.get(annotation.annotationType()).add(method);
                }
            }
        }
    }

    private static <T> void executeMethods(
            Map<Class<? extends Annotation>, List<Method>> methodsForAnnotations,
            T testClass,
            Class<? extends Annotation> annotationType
    ) {
        List<Method> methodsToExecute = methodsForAnnotations.get(annotationType);
        if (methodsToExecute != null) {
            for (Method method : methodsToExecute) {
                try {
                    method.setAccessible(true);
                    method.invoke(testClass);
                } catch (InvocationTargetException exception) {
                    System.out.println("Method execution failed: " + exception.getTargetException().getMessage());
                } catch (IllegalAccessException exception) {
                    System.out.println("Method execution failed: " + exception.getMessage());
                }
            }
        }
    }

}
