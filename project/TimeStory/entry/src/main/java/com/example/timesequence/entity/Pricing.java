package com.example.timesequence.entity;

public class Pricing {
    private Integer pricingId;//定价标识符
    private Integer pricingMoney;//金额
    private Integer pricingCount;//积分数量


    public Integer getPricingId() {
        return pricingId;
    }

    public void setPricingId(Integer pricingId) {
        this.pricingId = pricingId;
    }

    public Integer getPricingMoney() {
        return pricingMoney;
    }

    public void setPricingMoney(Integer pricingMoney) {
        this.pricingMoney = pricingMoney;
    }

    public Integer getPricingCount() {
        return pricingCount;
    }

    public void setPricingCount(Integer pricingCount) {
        this.pricingCount = pricingCount;
    }
}
