package com.example.taskmanager;

import java.time.LocalDate;

public class Task implements Comparable<Task> {
    private final int id;
    private String title;
    private String description;
    private LocalDate dueDate;
    public Task(int id, String title, String description, LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public int compareTo(Task anotherTask) {
        return this.dueDate.compareTo(anotherTask.dueDate);
    }
}

