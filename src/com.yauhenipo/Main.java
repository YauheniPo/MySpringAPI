package com.yauhenipo;

import com.popospringframework.beans.factory.BeanFactory;
import com.popospringframework.context.ApplicationContext;

public class Main {

    public static void main(String[] args) {
//        BeanFactory beanFactory = new BeanFactory();
//        beanFactory.addPostProcessor(new CustomPostProcessor());
//
//        beanFactory.instantiate("com.yauhenipo");
//        beanFactory.populatePropertiesByName();
//        beanFactory.injectBeanNames();
//        beanFactory.initializeBeans();
//
//        beanFactory.injectBeanFactory();
//
//        ProductService productService = (ProductService) beanFactory.getBean("productService");
//
//        beanFactory.close();

        ApplicationContext applicationContext = new ApplicationContext("com.yauhenipo");
        applicationContext.close();
    }
}