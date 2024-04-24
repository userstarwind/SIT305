package com.example.quizapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
public class Task {
    private String topic;
    private Date date;
    private List<Question> questionList;

    public String getTopic() {
        return topic;
    }

    public Task(String topic, Date date, List<Question> questionList) {
        this.topic = topic;
        this.date = date;
        this.questionList = questionList;
    }

    public String getFormattedDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    public Task() {
    }
}
