package com.yauhenipo;

import com.popospringframework.beans.factory.annotation.Autowired;
import com.popospringframework.beans.factory.stereotype.Component;

@Component
public class ProductService {
    @Autowired
    private PromotionsService promotionsService;

    public PromotionsService getPromotionsService() {
        return this.promotionsService;
    }

    public void setPromotionsService(PromotionsService promotionsService) {
        this.promotionsService = promotionsService;
    }
}
