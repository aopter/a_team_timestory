package com.example.timestroy.entity.problem;


import com.example.timestroy.entity.Dynasty;
import com.example.timestroy.entity.Problem;

public class ProblemCollection {
    private Dynasty dynasty;
    private Problem problem;


    public ProblemCollection() {
    }

    public ProblemCollection(Dynasty dynasty, Problem problem) {
        this.dynasty = dynasty;
        this.problem = problem;
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
}
