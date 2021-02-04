package com.example.timestory.entity;


public class UserUnlockDynastyIncident {
    private Integer incidentId;//事件ID
    private String incidentName;//事件名
    private String incidentPicture;//事件图片

    @Override
    public String toString() {
        return "UserUnlockDynastyIncident{" +
                "incidentId=" + incidentId +
                ", incidentName='" + incidentName + '\'' +
                ", incidentPicture='" + incidentPicture + '\'' +
                '}';
    }

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

    public String getIncidentPicture() {
        return incidentPicture;
    }

    public void setIncidentPicture(String incidentPicture) {
        this.incidentPicture = incidentPicture;
    }
}
