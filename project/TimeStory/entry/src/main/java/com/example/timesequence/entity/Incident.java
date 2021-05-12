package com.example.timesequence.entity;

public class Incident {
    private Integer incidentId;//朝代事件标识符
    private String incidentName;//事件名称
    private String incidentInfo;//事件简介
    private String incidentPicture;//事件图片
    private String incidentDialog;//事件对话内容
    private String incidentCreator;//创建人
    private long incidentCreationTime;//创建时间


    public Integer getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(Integer incidentId) {
        this.incidentId = incidentId;
    }

    public String getIncidentName() {
        return incidentName;
    }

    public void setIncidentName(String incidentName) {
        this.incidentName = incidentName;
    }

    public String getIncidentInfo() {
        return incidentInfo;
    }

    public void setIncidentInfo(String incidentInfo) {
        this.incidentInfo = incidentInfo;
    }

    public String getIncidentPicture() {
        return incidentPicture;
    }

    public void setIncidentPicture(String incidentPicture) {
        this.incidentPicture = incidentPicture;
    }

    public String getIncidentDialog() {
        return incidentDialog;
    }

    public void setIncidentDialog(String incidentDialog) {
        this.incidentDialog = incidentDialog;
    }

    public String getIncidentCreator() {
        return incidentCreator;
    }

    public void setIncidentCreator(String incidentCreator) {
        this.incidentCreator = incidentCreator;
    }

    public long getIncidentCreationTime() {
        return incidentCreationTime;
    }

    public void setIncidentCreationTime(long incidentCreationTime) {
        this.incidentCreationTime = incidentCreationTime;
    }

    @Override
    public String toString() {
        return "Incident{" +
                "incidentId=" + incidentId +
                ", incidentName='" + incidentName + '\'' +
                ", incidentInfo='" + incidentInfo + '\'' +
                ", incidentPicture='" + incidentPicture + '\'' +
                ", incidentDialog='" + incidentDialog + '\'' +
                ", incidentCreator='" + incidentCreator + '\'' +
                ", incidentCreationTime=" + incidentCreationTime +
                '}';
    }
}
