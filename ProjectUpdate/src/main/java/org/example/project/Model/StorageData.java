package org.example.project.Model;

import java.io.Serializable;
import java.util.List;

public class StorageData implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<User> users;
    private List<Category> categories;
    private List<Activity> activities;

    public StorageData(List<User> users, List<Category> categories, List<Activity> activities) {
        this.users = users;
        this.categories = categories;
        this.activities = activities;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Activity> getActivities() {
        return activities;
    }
}
