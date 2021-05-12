package com.example.timesequence.entity;

public class UserStatus {
    private Integer statusId;//地位标识符
    private String statusName;//地位名称
    private long statusExperienceLow;//最低经验点
    private long statusExperienceTop;//最高经验点
    private String statusInfo;//地位简介
    private String statusCreator;//创建人
    private long statusCreationTime;//创建时间


    @Override
    public String toString() {
        return "UserStatus{" +
                "statusId=" + statusId +
                ", statusName='" + statusName + '\'' +
                ", statusExperienceLow=" + statusExperienceLow +
                ", statusExperienceTop=" + statusExperienceTop +
                ", statusInfo='" + statusInfo + '\'' +
                ", statusCreator='" + statusCreator + '\'' +
                ", statusCreationTime=" + statusCreationTime +
                '}';
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public long getStatusExperienceLow() {
        return statusExperienceLow;
    }

    public void setStatusExperienceLow(long statusExperienceLow) {
        this.statusExperienceLow = statusExperienceLow;
    }

    public long getStatusExperienceTop() {
        return statusExperienceTop;
    }

    public void setStatusExperienceTop(long statusExperienceTop) {
        this.statusExperienceTop = statusExperienceTop;
    }

    public String getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(String statusInfo) {
        this.statusInfo = statusInfo;
    }

    public String getStatusCreator() {
        return statusCreator;
    }

    public void setStatusCreator(String statusCreator) {
        this.statusCreator = statusCreator;
    }

    public long getStatusCreationTime() {
        return statusCreationTime;
    }

    public void setStatusCreationTime(long statusCreationTime) {
        this.statusCreationTime = statusCreationTime;
    }
}
