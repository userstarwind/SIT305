package com.example.quizapp;

import java.util.List;

public class Question {
    private String question;
    private List<String> options;
    private String answer;

    public Question() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Question(String question, List<String> options, String answer) {
        this.question = question;
        this.options = options;
        this.answer = answer;
    }

    public boolean isCorrectAnswer(int optionIndex) {
        if (optionIndex < 0 || optionIndex >= options.size()) {
            return false;
        }
        int correctIndex = answer.charAt(0) - 'A';
        return optionIndex == correctIndex;
    }

    public int getFormattedAnswer(){
        return answer.charAt(0) - 'A';
    }
}

