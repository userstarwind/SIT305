package com.example.quizapp;

public class Interest {
    private String name;
    private boolean isSelected;

    public Interest(String name, boolean isSelected) {
        this.name = name;
        this.isSelected = isSelected;
    }
    public Interest(String name) {
        this.name = name;
        this.isSelected = false;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Interest() {
    }
}

