package com.popospringframework.beans.factory;

import com.popospringframework.beans.factory.annotation.Autowired;
import com.popospringframework.beans.factory.config.BeanPostProcessor;
import com.popospringframework.beans.factory.stereotype.Component;
import com.popospringframework.beans.factory.stereotype.Service;
import epam.popovich.annotation.log.Log;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class BeanFactory {
    private Map<String, Object> singletons = new HashMap<>();
    private List<BeanPostProcessor> postProcessors = new ArrayList<>();

    public Map<String, Object> getSingletons() {
        return singletons;
    }

    public void addPostProcessor(BeanPostProcessor postProcessor) {
        this.postProcessors.add(postProcessor);
    }

    public Object getBean(String beanName) {
        return singletons.get(beanName);
    }

    public void instantiate(String basePackage) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        String path = basePackage.replace('.', '/');
        try {
            Enumeration<URL> resources = classLoader.getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();

                File file = new File(resource.toURI());

                fetchBean(file, basePackage);
            }
        } catch (IOException | URISyntaxException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void fetchBean(File file, String basePackage) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        for (File classFile : Objects.requireNonNull(file.listFiles())) {
            if (!classFile.isFile()) {
                fetchBean(classFile, basePackage + "." + classFile.getName());
            }
            String fileName = classFile.getName();

            if (fileName.endsWith(".class")) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));

                Class classObject = Class.forName(basePackage + "." + className);

                if (classObject.isAnnotationPresent(Component.class) || classObject.isAnnotationPresent(Service.class)) {
                    Object instance = classObject.newInstance();

                    String beanName = className.substring(0, 1).toLowerCase() + className.substring(1);
                    singletons.put(beanName, instance);
                }
            }
        }
    }

    @Log
    public void populateProperties() {
        for (Object object : singletons.values()) {
            for (Field field : object.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    for (Object dependency : singletons.values()) {
                        if (dependency.getClass().equals(field.getType())) {
                            String setterName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                            try {
                                Method setter = object.getClass().getMethod(setterName, dependency.getClass());
                                setter.invoke(object, dependency);
                            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    @Log
    public void populatePropertiesByName() {
        singletons.values().forEach(obj -> Arrays.stream(obj.getClass().getDeclaredFields()).parallel()
                .filter(field -> field.isAnnotationPresent(Autowired.class) && singletons.containsKey(field.getName()))
                .forEach(field -> {
                    Object dependency = singletons.get(field.getName());
                    String setterName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                    try {
                        Method setter = obj.getClass().getMethod(setterName, dependency.getClass());
                        setter.invoke(obj, dependency);
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                })
        );
    }

    public void injectBeanNames() {
        singletons.entrySet().stream().filter(bean -> bean.getValue() instanceof BeanNameAware)
                .forEach(bean -> ((BeanNameAware) bean.getValue()).setBeanName(bean.getKey()));
    }

    public void injectBeanFactory() {
        singletons.values().stream().filter(bean -> bean instanceof BeanFactoryAware)
                .forEach(bean -> ((BeanFactoryAware) bean).setBeanFactory(new BeanFactory()));
    }

    public void initializeBeans() {
        singletons.forEach((name, bean) -> {
            postProcessors.forEach(processor -> processor.postProcessBeforeInitialization(bean, name));
            if (bean instanceof InitializingBean) {
                ((InitializingBean) bean).afterPropertiesSet();
            }
            postProcessors.forEach(processor -> processor.postProcessAfterInitialization(bean, name));
        });
    }

    public void close() {
        singletons.values().forEach(bean -> {
            Arrays.stream(bean.getClass().getMethods()).filter(method -> method.isAnnotationPresent(PreDestroy.class))
                    .forEach(method -> {
                        try {
                            method.invoke(bean);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    });
            if (bean instanceof DisposableBean) {
                ((DisposableBean) bean).destroy();
            }
        });
    }
}