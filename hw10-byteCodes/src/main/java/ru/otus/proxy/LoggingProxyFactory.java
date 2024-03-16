package ru.otus.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotation.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LoggingProxyFactory {
    private static final Logger logger = LoggerFactory.getLogger(LoggingProxyFactory.class);

    private LoggingProxyFactory() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T getProxyOf(Class<T> clazz) {
        Class<?>[] interfaces = clazz.getInterfaces();
        InvocationHandler handler = new ProxyInvocationHandler<>(clazz);
        return (T) Proxy.newProxyInstance(
                LoggingProxyFactory.class.getClassLoader(),
                interfaces,
                handler
        );
    }

    private static class ProxyInvocationHandler<T> implements InvocationHandler {
        Map<String, List<String>> loggingMethods;
        T declaredInstance;

        ProxyInvocationHandler(Class<T> clazz) {
            try {
                var constructor = clazz.getConstructor();
                declaredInstance = constructor.newInstance();
                loggingMethods = Arrays.stream(clazz.getMethods())
                        .filter(method -> method.getAnnotation(Log.class) != null)
                        .collect(Collectors.toUnmodifiableMap(
                                        this::getMethodSignature,
                                        this::getMethodParameterNames
                                )
                        );
            } catch (Exception e) {
                logger.atError().log(
                        "Instance of" + clazz.getName() + "is not created, something gone wrong",
                        e.getMessage()
                );
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            var parameters = loggingMethods.get(getMethodSignature(method));
            if (parameters != null) {
                var message = new StringBuilder();
                for (int i = 0; i < parameters.size(); i++) {
                    message.append(parameters.get(i))
                            .append(": ")
                            .append(args[i]);
                }
                logger.atInfo().log("Executed method: " + method.getName() + ", " + message);
            }
            return method.invoke(declaredInstance, args);
        }

        private String getMethodSignature(Method method) {
            return method.getName() + getMethodParameterTypes(method);
        }

        private List<String> getMethodParameterTypes(Method method) {
            return Arrays.stream(method.getParameterTypes()).map(Class::getName).toList();
        }

        private List<String> getMethodParameterNames(Method method) {
            return Arrays.stream(method.getParameters()).map(Parameter::getName).toList();
        }

    }

}
