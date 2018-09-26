package com.yauhenipo;

import com.popospringframework.beans.factory.BeanFactory;

public class Main {
    public static void main(String[] args) {
        BeanFactory beanFactory = new BeanFactory();
        beanFactory.instantiate("com.yauhenipo");

        beanFactory.addPostProcessor(new CustomPostProcessor());
        beanFactory.initializeBeans();
        beanFactory.populatePropertiesByName();
        beanFactory.injectBeanNames();
        beanFactory.injectBeanFactory();
        ProductService productService = (ProductService) beanFactory.getBean("productService");

    }
}