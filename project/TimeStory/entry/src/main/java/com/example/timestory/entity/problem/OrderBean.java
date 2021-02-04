package com.example.timestory.entity.problem;

import java.io.Serializable;

public class OrderBean implements Serializable {
    private String content;
    private int order;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "OrderBean{" +
                "content='" + content + '\'' +
                ", order=" + order +
                '}';
    }
}
