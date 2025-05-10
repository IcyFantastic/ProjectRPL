package org.example.project.Model;

public class Category {
    private String name;
    private String description;
    private String user; // Atribut untuk mendukung multi-akun

    public Category(String name) {
        this(name, "", "");
    }

    public Category(String name, String description, String user) {
        this.name = name;
        this.description = description;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return name;
    }
}
