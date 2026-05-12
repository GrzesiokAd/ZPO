package com.validation.strategy;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.validation.annotation.ValidationFor;

public class ValidationStrategyFactory {

    private static final String STRATEGY_PACKAGE = "com.validation.strategy";
    private static final Map<Class<? extends Annotation>, ValidationStrategy> strategies = new HashMap<>();

    static {
        loadStrategies();
    }
    private ValidationStrategyFactory() {
    }
    public static ValidationStrategy getStrategy(Annotation annotation) {
        return strategies.get(annotation.annotationType());
    }
    private static void loadStrategies() {
        String packagePath = STRATEGY_PACKAGE.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> resources = classLoader.getResources(packagePath);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if ("file".equals(resource.getProtocol())) {
                    loadStrategiesFromDirectory(new File(resource.toURI()));
                }
            }
        } catch (IOException | URISyntaxException e) {
            throw new IllegalStateException("Nie można wczytać strategii walidacji", e);
        }
    }
    private static void loadStrategiesFromDirectory(File directory) {
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".class") && !name.contains("$"));
        if (files == null) {
            return;
        }
        for (File file : files) {
            String className = file.getName().substring(0, file.getName().length() - ".class".length());
            registerStrategy(STRATEGY_PACKAGE + "." + className);
        }
    }
    private static void registerStrategy(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
                return;
            }
            if (!ValidationStrategy.class.isAssignableFrom(clazz)) {
                return;
            }
            if (!clazz.isAnnotationPresent(ValidationFor.class)) {
                return;
            }
            ValidationFor validationFor = clazz.getAnnotation(ValidationFor.class);
            ValidationStrategy strategy = (ValidationStrategy) clazz.getDeclaredConstructor().newInstance();
            strategies.put(validationFor.value(), strategy);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Nie można utworzyć strategii: " + className, e);
        }
    }
}
