package org.example.project.Model;

import java.time.LocalDate;

public class Activity {
    private String title;
    private String description;
    private LocalDate date;
    private Category category;
    private String priority;
    private boolean completed;

    public Activity() {}
    public Activity(String title, String description, LocalDate date, Category category, String priority) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.category = category;
        this.priority = priority;
        this.completed = false;
    }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
