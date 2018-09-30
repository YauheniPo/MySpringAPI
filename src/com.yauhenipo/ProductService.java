package com.yauhenipo;

import com.popospringframework.beans.factory.DisposableBean;
import com.popospringframework.beans.factory.InitializingBean;
import com.popospringframework.beans.factory.annotation.Autowired;
import com.popospringframework.beans.factory.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class ProductService implements InitializingBean, DisposableBean {
    @Autowired
    private PromotionsService promotionsService;

    public PromotionsService getPromotionsService() {
        return this.promotionsService;
    }

    public void setPromotionsService(PromotionsService promotionsService) {
        this.promotionsService = promotionsService;
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println(promotionsService.getClass().getName() + " init...");
    }

    @Override
    public void destroy() {
        System.out.println(this.getClass().getName() + " destroy");
    }

    @PreDestroy
    public void destroy2() {
        System.out.println(this.getClass().getName() + " @PreDestroy...");
    }
}
