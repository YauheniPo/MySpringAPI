package com.yauhenipo;

import com.popospringframework.beans.factory.BeanNameAware;
import com.popospringframework.beans.factory.stereotype.Component;

@Component
public class PromotionsService implements BeanNameAware {
    private String beanName;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    public String getBeanName() {
        return this.beanName;
    }
}
