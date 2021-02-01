package com.example.timestroy.entity;

public class UserProblem {
    private Integer id;//流水号

    private Dynasty dynasty;//朝代
    private Problem problem;//题目

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Dynasty getDynasty() {
        return dynasty;
    }

    public void setDynasty(Dynasty dynasty) {
        this.dynasty = dynasty;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    @Override
    public String toString() {
        return "UserProblem{" +
                "id=" + id +
                ", dynasty=" + dynasty +
                ", problem=" + problem +
                '}';
    }
}
