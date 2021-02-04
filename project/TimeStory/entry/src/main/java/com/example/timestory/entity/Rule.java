package com.example.timestory.entity;

public class Rule {
    private Integer ruleId;//规则标识符
    private String ruleInfo;//规则详情

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleInfo() {
        return ruleInfo;
    }

    public void setRuleInfo(String ruleInfo) {
        this.ruleInfo = ruleInfo;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "ruleId=" + ruleId +
                ", ruleInfo='" + ruleInfo + '\'' +
                '}';
    }
}
