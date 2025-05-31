package org.example.project.Util;

import org.example.project.Model.Activity;
import org.example.project.Model.Category;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Storage {

    public static void initializeStorage() {
        String sqlCreateUsers = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL" +
                ");";

        String sqlCreateCategories = "CREATE TABLE IF NOT EXISTS categories (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "description TEXT," +
                "username TEXT NOT NULL" +
                ");";

        String sqlCreateActivities = "CREATE TABLE IF NOT EXISTS activities (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "description TEXT," +
                "date TEXT NOT NULL," +
                "priority TEXT," +
                "completed INTEGER NOT NULL," +
                "username TEXT NOT NULL," +
                "category_name TEXT NOT NULL" +
                ");";

        String sqlCreateSession = "CREATE TABLE IF NOT EXISTS session (" +
                "id INTEGER PRIMARY KEY," +
                "user_id INTEGER NOT NULL" +
                ");";

        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sqlCreateUsers);
            stmt.execute(sqlCreateCategories);
            stmt.execute(sqlCreateActivities);
            stmt.execute(sqlCreateSession);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- User management ---

    public static boolean addUser(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updatePassword(String username, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPassword);
            stmt.setString(2, username);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Integer validateUser(String username, String password) {
        String sql = "SELECT id FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean userExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getUsernameById(int userId) {
        String sql = "SELECT username FROM users WHERE id = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("username");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // --- Category management ---

    public static List<Category> loadCategories(String username) {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT name, description FROM categories WHERE username = ?";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String desc = rs.getString("description");
                categories.add(new Category(name, desc, username));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    public static boolean addCategory(Category category) {
        String sql = "INSERT INTO categories (name, description, username) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setString(3, category.getUser());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteCategory(Category category) {
        String sql = "DELETE FROM categories WHERE name = ? AND username = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getName());
            stmt.setString(2, category.getUser());
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- Activity management ---

    public static List<Activity> getActivities(String username) {
        List<Activity> activities = new ArrayList<>();
        String sql = "SELECT id, title, description, date, priority, completed, category_name FROM activities WHERE username = ?";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String desc = rs.getString("description");
                LocalDate date = LocalDate.parse(rs.getString("date"));
                String priority = rs.getString("priority");
                boolean completed = rs.getInt("completed") == 1;
                String categoryName = rs.getString("category_name");

                Category category = new Category(categoryName, username);
                Activity activity = new Activity(id, title, date, category, priority, completed, username)
                        .setDescription(desc);
                activities.add(activity);
            }

        } catch (SQLException e) {
            // kamu bisa log ke file kalau mau, tapi tidak print ke console
        }

        return activities;
    }


    public static boolean addActivity(Activity activity) {
        String sql = "INSERT INTO activities (title, description, date, priority, completed, category_name, username) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, activity.getTitle());
            pstmt.setString(2, activity.getDescription());
            pstmt.setString(3, activity.getDate().toString());
            pstmt.setString(4, activity.getPriority());
            pstmt.setInt(5, activity.isCompleted() ? 1 : 0);
            pstmt.setString(6, activity.getCategory().getName());
            pstmt.setString(7, activity.getUser());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }

            // Ambil last inserted id
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) {
                    activity.setId(rs.getInt(1));
                }
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    public static boolean updateActivity(Activity activity) {
        String sql = "UPDATE activities SET title=?, description=?, date=?, priority=?, completed=?, category_name=? WHERE id=?";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, activity.getTitle());
            stmt.setString(2, activity.getDescription());
            stmt.setString(3, activity.getDate().toString());
            stmt.setString(4, activity.getPriority());
            stmt.setInt(5, activity.isCompleted() ? 1 : 0);
            stmt.setString(6, activity.getCategory().getName());
            stmt.setInt(7, activity.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // Logging error jika diperlukan
            return false;
        }
    }



    public static boolean deleteActivity(Activity activity) {
        String sql = "DELETE FROM activities WHERE id = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, activity.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
