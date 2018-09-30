package com.yauhenipo;

import com.popospringframework.beans.factory.BeanFactory;
import com.popospringframework.beans.factory.BeanFactoryAware;
import com.popospringframework.beans.factory.BeanNameAware;
import com.popospringframework.beans.factory.stereotype.Service;

@Service
public class PromotionsService implements BeanNameAware, BeanFactoryAware {
    private String beanName;
    private BeanFactory beanFactory;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public String getBeanName() {
        return this.beanName;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }
}
