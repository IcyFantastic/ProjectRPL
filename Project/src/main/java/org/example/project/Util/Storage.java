package org.example.project.Util;

import org.example.project.Model.Activity;
import org.example.project.Model.Category;
import org.example.project.Model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private static final String BASE = System.getProperty("user.home") + File.separator + ".todoapp";
    private static final Gson gson = new Gson();

    public static void init() throws Exception {
        Files.createDirectories(Paths.get(BASE));
        File u = new File(BASE, "users.json");
        if (!u.exists()) new FileWriter(u).close();
    }

    public static List<User> loadUsers() throws Exception {
        File f = new File(BASE, "users.json");
        Type t = new TypeToken<List<User>>(){}.getType();
        try (FileReader r = new FileReader(f)) {
            List<User> list = gson.fromJson(r, t);
            return list != null ? list : new ArrayList<>();
        }
    }

    public static void saveUsers(List<User> users) throws Exception {
        try (FileWriter w = new FileWriter(new File(BASE, "users.json"))) {
            gson.toJson(users, w);
        }
    }

    public static List<Category> loadCategories(String user) throws Exception {
        File f = new File(BASE, user + "_cats.json");
        if (!f.exists()) new FileWriter(f).close();
        Type t = new TypeToken<List<Category>>(){}.getType();
        try (FileReader r = new FileReader(f)) {
            List<Category> list = gson.fromJson(r, t);
            return list != null ? list : new ArrayList<>();
        }
    }

    public static void saveCategories(String user, List<Category> list) throws Exception {
        try (FileWriter w = new FileWriter(new File(BASE, user + "_cats.json"))) {
            gson.toJson(list, w);
        }
    }

    public static List<Activity> loadActivities(String user) throws Exception {
        File f = new File(BASE, user + "_acts.json");
        if (!f.exists()) new FileWriter(f).close();
        Type t = new TypeToken<List<Activity>>(){}.getType();
        try (FileReader r = new FileReader(f)) {
            List<Activity> list = gson.fromJson(r, t);
            return list != null ? list : new ArrayList<>();
        }
    }

    public static void saveActivities(String user, List<Activity> list) throws Exception {
        try (FileWriter w = new FileWriter(new File(BASE, user + "_acts.json"))) {
            gson.toJson(list, w);
        }
    }

    public static void initializeStorage() {
    }
}
