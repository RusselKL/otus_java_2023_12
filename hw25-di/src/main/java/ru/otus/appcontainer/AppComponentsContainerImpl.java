package ru.otus.appcontainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {
    private static final Logger logger = LoggerFactory.getLogger(AppComponentsContainerImpl.class);

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?>... initialConfigClass) {
        Arrays.stream(initialConfigClass)
                .sorted(Comparator.comparingInt(configClass ->
                        {
                            checkConfigClass(configClass);
                            return configClass.getAnnotation(AppComponentsContainerConfig.class).order();
                        }
                ))
                .forEachOrdered(configClass -> processConfig(configClass));
        ;
    }

    private void processConfig(Class<?> configClass) {
        var appComponents = Arrays.stream(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .toList();
        if (!appComponents.isEmpty()) {
            checkComponents(appComponents);
            try {
                Constructor<?> constructor = configClass.getDeclaredConstructor();
                var configInstance = constructor.newInstance();
                appComponents.stream()
                        .sorted(Comparator.comparingInt(method -> method.getAnnotation(AppComponent.class).order()))
                        .forEachOrdered(method -> createAppComponent(configInstance, method));
            } catch (Exception e) {
                logger.atError().log("Process config class went wrong: {}", e.getMessage());
            }
        }
    }

    private void createAppComponent(Object configInstance, Method method) {
        try {
            method.setAccessible(true);
            var parameterTypes = method.getParameterTypes();

            var parameterComponents = Arrays.stream(parameterTypes)
                    .map(type -> getAppComponent(type))
                    .toArray();
            var component = method.invoke(configInstance, parameterComponents);
            var componentName = method.getAnnotation(AppComponent.class).name();
            appComponents.add(component);
            appComponentsByName.put(componentName, component);
        } catch (Exception e) {
            logger.atError().log("{} method invocation in config class went wrong: {}", method.getName(), e.getMessage());
        }
    }

    private void checkComponents(List<Method> appComponents) {
        var componentRepeatedNames = appComponents.stream()
                .map(method -> method.getAnnotation(AppComponent.class).name())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream().filter(entry -> entry.getValue() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if (!componentRepeatedNames.isEmpty()) {
            throw new IllegalArgumentException(String.format("Config class contains components with repeated names %s", componentRepeatedNames.keySet()));
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        var components = appComponents.stream()
                .filter(c -> componentClass.isInstance(c))
                .toList();
        if (components.size() > 1) {
            throw new IllegalArgumentException(String.format("Config class has two instances of %s", componentClass));
        }
        return (C) components.getFirst();
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        var component = Optional.ofNullable(appComponentsByName.get(componentName)).orElseThrow();
        return (C) component;
    }
}
