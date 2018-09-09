package com.yauhenipo;

import com.popospringframework.beans.factory.stereotype.Component;

@Component
public class ProductService {
    private PromotionsService promotionsService;

    public PromotionsService getPromotionsService() {
        return this.promotionsService;
    }

    public void setPromotionsService(PromotionsService promotionsService) {
        this.promotionsService = promotionsService;
    }
}
