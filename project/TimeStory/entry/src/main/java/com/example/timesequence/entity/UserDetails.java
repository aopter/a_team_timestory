package com.example.timesequence.entity;

public class UserDetails {
    private Integer userId;//用户标识符
    private String userNickname;//用户昵称
    private String userSignature;//个性签名
    private String userSex;//性别
    private String userNumber;//手机号
    private long userCreationTime;//创建时间


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserSignature() {
        return userSignature;
    }

    public void setUserSignature(String userSignature) {
        this.userSignature = userSignature;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public long getUserCreationTime() {
        return userCreationTime;
    }

    public void setUserCreationTime(long userCreationTime) {
        this.userCreationTime = userCreationTime;
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "userId=" + userId +
                ", userNickname='" + userNickname + '\'' +
                ", userSignature='" + userSignature + '\'' +
                ", userSex='" + userSex + '\'' +
                ", userNumber='" + userNumber + '\'' +
                ", userCreationTime=" + userCreationTime +
                '}';
    }
}
