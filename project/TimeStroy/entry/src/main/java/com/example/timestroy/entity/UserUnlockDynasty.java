package com.example.timestroy.entity;

public class UserUnlockDynasty {
    private Integer userId;//流水号
    private Integer progress;//答对题目个数
    private String dynastyName;
    private String dynastyId;//朝代


    @Override
    public String toString() {
        return "UserUnlockDynasty{" +
                "userId=" + userId +
                ", progress=" + progress +
                ", dynastyId='" + dynastyId + '\'' +
                '}';
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getDynastyId() {
        return dynastyId;
    }

    public void setDynastyId(String dynastyId) {
        this.dynastyId = dynastyId;
    }

    public String getDynastyName() {
        return dynastyName;
    }

    public void setDynastyName(String dynastyName) {
        this.dynastyName = dynastyName;
    }
}
