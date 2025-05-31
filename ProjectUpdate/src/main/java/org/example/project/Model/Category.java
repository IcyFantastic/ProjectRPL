package org.example.project.Model;

import java.io.Serializable;

public class Category implements Serializable {
    private int id;             // id sebagai primary key database
    private String name;
    private String description;
    private String user;        // username pemilik kategori

    // Constructor lengkap
    public Category(int id, String name, String description, String user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.user = user;
    }

    // Constructor tanpa id (misal saat buat baru sebelum disimpan ke DB)
    public Category(String name, String description, String user) {
        this(0, name, description, user);
    }

    // Constructor minimal (sesuai yang sebelumnya)
    public Category(String name, String user) {
        this(0, name, "", user);
    }

    // Getter dan Setter untuk semua field

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
