package com.popospringframework.context;

import com.popospringframework.beans.factory.BeanFactory;
import com.popospringframework.context.event.ContextClosedEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

public class ApplicationContext {

    private BeanFactory beanFactory = new BeanFactory();

    public ApplicationContext(String basePackage) {
        beanFactory.instantiate(basePackage);
        beanFactory.populatePropertiesByName();
        beanFactory.injectBeanNames();
        beanFactory.initializeBeans();
    }

    public void close() {
        beanFactory.close();
        beanFactory.getSingletons().values().forEach(bean -> Arrays.stream(bean.getClass().getGenericInterfaces())
            .filter(type -> type instanceof ParameterizedType).map(type -> (ParameterizedType) type)
            .filter(parameterizedType -> parameterizedType.getActualTypeArguments()[0].equals(ContextClosedEvent.class))
            .forEach(parameterizedType -> {
                try {
                    bean.getClass().getMethod("onApplicationEvent", ContextClosedEvent.class).invoke(bean, new ContextClosedEvent());
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }));
    }
}