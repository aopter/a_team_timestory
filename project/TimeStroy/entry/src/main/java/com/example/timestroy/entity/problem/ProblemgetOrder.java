package com.example.timestroy.entity.problem;





import com.example.timestroy.entity.Problem;

import java.io.Serializable;
import java.util.List;

public class ProblemgetOrder extends Problem implements Serializable {
    //     朝代id
    private String dynastyId;//朝代标识符
    //     题目id
    private int problemId;//题目标识符

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private List<OrderBean> contents;//内容
    private int problemType;
    private String problemKey;

    @Override
    public String getProblemKey() {
        return problemKey;
    }

    @Override
    public void setProblemKey(String problemKey) {
        this.problemKey = problemKey;
    }

    @Override
    public int getProblemType() {
        return problemType;
    }

    @Override
    public void setProblemType(int problemType) {
        this.problemType = problemType;
    }

    private String problemDetails;

    @Override
    public String getProblemDetails() {
        return problemDetails;
    }

    @Override
    public void setProblemDetails(String problemDetails) {
        this.problemDetails = problemDetails;
    }
    public String getDynastyId() {
        return dynastyId;
    }

    public void setDynastyId(String dynastyId) {
        this.dynastyId = dynastyId;
    }

    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public List<OrderBean> getContents() {
        return contents;
    }

    public void setContents(List<OrderBean> contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "ProblemgetOrder{" +
                "dynastyId='" + dynastyId + '\'' +
                ", problemId=" + problemId +
                ", title='" + title + '\'' +
                ", contents=" + contents +
                ", problemType=" + problemType +
                ", problemKey='" + problemKey + '\'' +
                ", problemDetails='" + problemDetails + '\'' +
                '}';
    }
}
