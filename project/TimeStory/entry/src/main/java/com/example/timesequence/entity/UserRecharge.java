package com.example.timesequence.entity;



public class UserRecharge {
    private Integer id;//流水号
    private long createTime;//订单创建时间

    private User user;//用户
    private Pricing pricing;//充值类型


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Pricing getPricing() {
        return pricing;
    }

    public void setPricing(Pricing pricing) {
        this.pricing = pricing;
    }

    @Override
    public String toString() {
        return "UserRecharge{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", user=" + user +
                ", pricing=" + pricing +
                '}';
    }
}
