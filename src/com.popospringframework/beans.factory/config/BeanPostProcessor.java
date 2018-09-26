package com.popospringframework.beans.factory.config;

public interface BeanPostProcessor {

    Object postProcessBeforeInitialization(Object bean, String beanName);
    Object postProcessAfterInitialization(Object bean, String beanName);
}
