package com.yauhenipo;

import com.popospringframework.beans.factory.BeanFactory;

public class Main {
    public static void main(String[] args) {
        BeanFactory beanFactory = new BeanFactory();
        beanFactory.instantiate("com.yauhenipo");

        beanFactory.populatePropertiesByName();
        
        ProductService productService = (ProductService) beanFactory.getBean("productService");

    }
}