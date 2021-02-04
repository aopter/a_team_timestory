package com.example.timestory.entity;



import com.example.timestory.entity.card.UserCard;

import java.util.HashSet;
import java.util.Set;

public class User {
    private Integer userId;//用户标识符
    private String userAccount;//用户账号
    private String userPassword;//用户密码
    private String userHeader;//用户头像
    private String userNickname;//用户昵称
    private long userExperience;//用户经验点
    private int userCount;//用户积分
    private String userSignature;//用户个性签名
    private int flag;//用于区分手机号登录和QQ登录

    public String getUserSignature() {
        return userSignature;
    }

    public void setUserSignature(String userSignature) {
        this.userSignature = userSignature;
    }

    private UserStatus userStatus;//用户地位
    private Set<UserCard> userCards = new HashSet<>();//用户拥有的卡片
    private Set<UserRecharge> userRecharges = new HashSet<>();//用户的历史订单
    private Set<UserUnlockDynasty> userUnlockDynasties = new HashSet<>();//用户解锁的朝代集合
    private Set<UserUnlockDynastyIncident> userUnlockDynastyIncidents = new HashSet<>();//用户解锁的某个朝代的事件集合
    private Set<UserProblem> problems = new HashSet<>();//用户做过的题目

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserHeader() {
        return userHeader;
    }

    public void setUserHeader(String userHeader) {
        this.userHeader = userHeader;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public long getUserExperience() {
        return userExperience;
    }

    public void setUserExperience(long userExperience) {
        this.userExperience = userExperience;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public Set<UserCard> getUserCards() {
        return userCards;
    }

    public void setUserCards(Set<UserCard> userCards) {
        this.userCards = userCards;
    }

    public Set<UserRecharge> getUserRecharges() {
        return userRecharges;
    }

    public void setUserRecharges(Set<UserRecharge> userRecharges) {
        this.userRecharges = userRecharges;
    }

    public Set<UserUnlockDynasty> getUserUnlockDynasties() {
        return userUnlockDynasties;
    }

    public void setUserUnlockDynasties(Set<UserUnlockDynasty> userUnlockDynasties) {
        this.userUnlockDynasties = userUnlockDynasties;
    }

    public Set<UserUnlockDynastyIncident> getUserUnlockDynastyIncidents() {
        return userUnlockDynastyIncidents;
    }

    public void setUserUnlockDynastyIncidents(Set<UserUnlockDynastyIncident> userUnlockDynastyIncidents) {
        this.userUnlockDynastyIncidents = userUnlockDynastyIncidents;
    }

    public Set<UserProblem> getProblems() {
        return problems;
    }

    public void setProblems(Set<UserProblem> problems) {
        this.problems = problems;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userAccount='" + userAccount + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userHeader='" + userHeader + '\'' +
                ", userNickname='" + userNickname + '\'' +
                ", userExperience=" + userExperience +
                ", userCount=" + userCount +
                ", userSignature='" + userSignature + '\'' +
                ", userStatus=" + userStatus +
                ", userCards=" + userCards +
                ", userRecharges=" + userRecharges +
                ", userUnlockDynasties=" + userUnlockDynasties +
                ", userUnlockDynastyIncidents=" + userUnlockDynastyIncidents +
                ", problems=" + problems +
                '}';
    }
}
