package com.example.timesequence.entity;

public class Team {
    private Integer teamId;//团队标识符
    private String teamName;//团队名称
    private String teamMember;//团队人员
    private String teamEmail;//团队邮箱

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamMember() {
        return teamMember;
    }

    public void setTeamMember(String teamMember) {
        this.teamMember = teamMember;
    }

    @Override
    public String toString() {
        return "Team{" +
                "teamId=" + teamId +
                ", teamName='" + teamName + '\'' +
                ", teamMember='" + teamMember + '\'' +
                ", teamEmail='" + teamEmail + '\'' +
                '}';
    }

    public String getTeamEmail() {
        return teamEmail;
    }

    public void setTeamEmail(String teamEmail) {
        this.teamEmail = teamEmail;
    }
}
