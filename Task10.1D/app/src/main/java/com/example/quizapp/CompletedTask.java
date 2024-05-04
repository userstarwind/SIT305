package com.example.quizapp;

import java.util.List;

public class CompletedTask {
    private Task task;
    private List<Boolean> answerList;

    public CompletedTask(Task task, List<Boolean> answerList) {
        this.task = task;
        this.answerList = answerList;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public List<Boolean> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Boolean> answerList) {
        this.answerList = answerList;
    }

    public CompletedTask() {
    }
}
