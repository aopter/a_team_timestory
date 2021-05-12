package com.example.timesequence.entity.problem;

public class ProblemCheckAnswer {

    private int userId;
    private int userCount;
    private int userExperience;
    private String unlock;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public int getUserExperience() {
        return userExperience;
    }

    public void setUserExperience(int userExperience) {
        this.userExperience = userExperience;
    }

    public String getUnlock() {
        return unlock;
    }

    public void setUnlock(String unlock) {
        this.unlock = unlock;
    }

    @Override
    public String toString() {
        return "ProblemCheckAnswer{" +
                "userId=" + userId +
                ", userCount=" + userCount +
                ", userExperience=" + userExperience +
                ", unlock='" + unlock + '\'' +
                '}';
    }
}
