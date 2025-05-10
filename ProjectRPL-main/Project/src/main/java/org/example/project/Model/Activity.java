package org.example.project.Model;

import java.time.LocalDate;

public class Activity {
    private String title;
    private LocalDate date;
    private Category category;
    private String priority;
    private boolean completed;
    private String user;
    private String description;

    public Activity(String title, LocalDate date, Category category, String priority, boolean completed, String user) {
        this.title = title;
        this.date = date;
        this.category = category;
        this.priority = priority;
        this.completed = completed;
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public Activity setDescription(String description) {
        this.description = description;
        return this;
    }


    @Override
    public String toString() {
        return "Activity{" +
                "title='" + title + '\'' +
                ", date=" + date +
                ", category=" + (category != null ? category.getName() : "None") +
                ", priority='" + priority + '\'' +
                ", completed=" + completed +
                ", user='" + user + '\'' +
                '}';
    }


}
