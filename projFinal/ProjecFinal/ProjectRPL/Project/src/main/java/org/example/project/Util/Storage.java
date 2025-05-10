package org.example.project.Util;

import org.example.project.Model.User;
import org.example.project.Model.Category;
import org.example.project.Model.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.io.*;

public class Storage {
    private static final List<User> users = new ArrayList<>();
    private static final List<Category> categories = new ArrayList<>();
    private static final List<Activity> activities = new ArrayList<>();
    private static final String FILE_NAME = "data.dat";

    public static void initializeStorage() {
        loadAll(); // Coba load dari file
        if (users.isEmpty()) {
            users.add(new User("admin", "Admin123!")); // Data awal default
            saveAll(); // Simpan langsung agar file tercipta
        }
    }

    public static List<User> getUsers() {
        return users;
    }

    public static List<Category> getCategories() {
        return categories;
    }

    public static List<Activity> getActivities() {
        return activities;
    }

    public static void addUser(User user) {
        users.add(user);
        saveAll();
    }

    public static void addCategory(Category category) {
        categories.add(category);
        saveAll();
    }

    public static void addActivity(Activity activity) {
        activities.add(activity);
        saveAll();
    }

    public static List<Category> loadCategories(String username) {
        return categories.stream()
                .filter(c -> c.getUser().equals(username))
                .collect(Collectors.toList());
    }

    public static void saveAll() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(new StorageData(users, categories, activities));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadAll() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            StorageData data = (StorageData) in.readObject();
            users.clear(); users.addAll(data.getUsers());
            categories.clear(); categories.addAll(data.getCategories());
            activities.clear(); activities.addAll(data.getActivities());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Data file belum ada atau gagal dibaca. Menggunakan data default.");
        }
    }
}
