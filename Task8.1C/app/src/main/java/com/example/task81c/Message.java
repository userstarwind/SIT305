package com.example.task81c;

public class Message {
    private String direction;
    private String content;

    public Message(String direction, String content) {
        this.direction = direction;
        this.content = content;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
