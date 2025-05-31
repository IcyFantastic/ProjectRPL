package org.example.project.Model;

import java.io.Serializable;
import java.time.LocalDate;

public class Activity implements Serializable {
    private int id;  // baru
    private String title;
    private LocalDate date;
    private Category category;
    private String priority;
    private boolean completed;
    private String user;
    private String description;
    private boolean done;

    // Constructor baru dengan id
    public Activity(int id, String title, LocalDate date, Category category, String priority, boolean completed, String user) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.category = category;
        this.priority = priority;
        this.completed = completed;
        this.user = user;
    }

    // Constructor lama tanpa id (misal untuk buat baru sebelum disimpan)
    public Activity(String title, LocalDate date, Category category, String priority, boolean completed, String user) {
        this(0, title, date, category, priority, completed, user);
    }

    // getter/setter id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // getter/setter lain tetap sama...
    // ...

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

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", category=" + (category != null ? category.getName() : "None") +
                ", priority='" + priority + '\'' +
                ", completed=" + completed +
                ", user='" + user + '\'' +
                '}';
    }
}
