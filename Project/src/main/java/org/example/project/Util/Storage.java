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

    public static void initializeStorage() {
        if (users.isEmpty()) {
            users.add(new User("admin", "Admin123!")); // Contoh data awal
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
    }

    public static void addCategory(Category category) {
        categories.add(category);
    }

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }


    public static List<User> loadUsers() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("users.dat"))) {
            return (List<User>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>(); // Jika file tidak ada atau gagal dibaca, kembalikan list kosong
        }
    }

    public static void saveUsers(List<User> users) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("users.dat"))) {
            out.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Category> loadCategories(String username) {
        return categories.stream()
                .filter(c -> c.getUser().equals(username))
                .collect(Collectors.toList());
    }
}
