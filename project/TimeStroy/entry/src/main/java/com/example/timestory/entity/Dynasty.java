package com.example.timestory.entity;

import java.util.Set;

public class Dynasty {
    private Integer dynastyId;//朝代标识符
    private String dynastyName;//朝代名称
    private String dynastyTime;//朝代时间
    private String dynastyInfo;//朝代简介
    private String dynastyCreator;//创建人
    private long dynastyCreationTime;//创建时间

    private Set<Incident> incidents;//朝代中的事件
    private Set<Problem> problems;//朝代的题目

    public Integer getDynastyId() {
        return dynastyId;
    }

    public void setDynastyId(Integer dynastyId) {
        this.dynastyId = dynastyId;
    }

    public String getDynastyName() {
        return dynastyName;
    }

    public void setDynastyName(String dynastyName) {
        this.dynastyName = dynastyName;
    }

    public String getDynastyTime() {
        return dynastyTime;
    }

    public void setDynastyTime(String dynastyTime) {
        this.dynastyTime = dynastyTime;
    }

    public String getDynastyInfo() {
        return dynastyInfo;
    }

    public void setDynastyInfo(String dynastyInfo) {
        this.dynastyInfo = dynastyInfo;
    }

    public String getDynastyCreator() {
        return dynastyCreator;
    }

    public void setDynastyCreator(String dynastyCreator) {
        this.dynastyCreator = dynastyCreator;
    }

    public long getDynastyCreationTime() {
        return dynastyCreationTime;
    }

    public void setDynastyCreationTime(long dynastyCreationTime) {
        this.dynastyCreationTime = dynastyCreationTime;
    }

    public Set<Incident> getIncidents() {
        return incidents;
    }

    public void setIncidents(Set<Incident> incidents) {
        this.incidents = incidents;
    }

    public Set<Problem> getProblems() {
        return problems;
    }

    public void setProblems(Set<Problem> problems) {
        this.problems = problems;
    }


    @Override
    public String toString() {
        return "Dynasty{" +
                "dynastyId=" + dynastyId +
                ", dynastyName='" + dynastyName + '\'' +
                ", dynastyTime='" + dynastyTime + '\'' +
                ", dynastyInfo='" + dynastyInfo + '\'' +
                ", dynastyCreator='" + dynastyCreator + '\'' +
                ", dynastyCreationTime=" + dynastyCreationTime +
                ", incidents=" + incidents +
                ", problems=" + problems +
                '}';
    }
}
