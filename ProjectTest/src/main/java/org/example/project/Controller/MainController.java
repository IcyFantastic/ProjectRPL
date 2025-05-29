// MainController.java
package org.example.project.Controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.project.Model.Activity;
import org.example.project.Model.Category;
import org.example.project.Util.Storage;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MainController {
    @FXML
    private VBox activityList;
    @FXML
    private ComboBox<String> filterStatus;
    @FXML
    private ComboBox<String> filterPriority;
    @FXML
    private ComboBox<Category> filterCategory;
    @FXML
    private TextField searchField;

    private ObservableList<Activity> activities;
    private ObservableList<Category> categories;
    private String user;
    private Runnable logoutCallback;

    public void initData(String username) {
        this.user = username;

        categories = FXCollections.observableArrayList(Storage.getCategories().stream()
                .filter(c -> c.getUser().equals(user))
                .collect(Collectors.toList()));
        categories.add(new Category(""));
        filterCategory.setItems(categories);

        activities = FXCollections.observableArrayList(Storage.getActivities().stream()
                .filter(a -> a.getUser().equals(user))
                .collect(Collectors.toList()));

        filterStatus.setItems(FXCollections.observableArrayList("", "Selesai", "Belum"));
        filterPriority.setItems(FXCollections.observableArrayList("", "Tinggi", "Sedang", "Rendah"));

        renderActivityList(activities);
        checkReminders();
    }

    private void renderActivityList(List<Activity> activityData) {
        activityList.getChildren().clear();

        for (Activity a : activityData) {
            HBox item = new HBox(10);
            item.getStyleClass().add("activity-item");
            Label title = new Label(a.getTitle());
            Label date = new Label(a.getDate().toString());
            Label cat = new Label(a.getCategory() != null ? a.getCategory().getName() : "-");
            Label priority = new Label(a.getPriority());
            priority.getStyleClass().add("priority-" + a.getPriority().toLowerCase());
            Label status = new Label(a.isCompleted() ? "Selesai" : "Belum");
            status.getStyleClass().add(a.isCompleted() ? "status-selesai" : "status-belum");

            Button editBtn = new Button("Edit");
            editBtn.setOnAction(e -> {
                try {
                    editActivity(a);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            Button deleteBtn = new Button("Hapus");
            deleteBtn.setOnAction(e -> {
                deleteActivity(a);
            });

            Button toggleStatus = new Button("Toggle");
            toggleStatus.setOnAction(e -> {
                a.setCompleted(!a.isCompleted());
                Storage.saveAll();
                filterChanged(); // refresh
            });

            item.getChildren().addAll(title, date, cat, priority, status, editBtn, deleteBtn, toggleStatus);
            activityList.getChildren().add(item);
        }
    }

    private void checkReminders() {
        long cnt = activities.stream()
                .filter(a -> a.getDate().equals(LocalDate.now()) && !a.isCompleted())
                .count();
        if (cnt > 0) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("Ada " + cnt + " aktivitas hari ini");
            a.showAndWait();
        }
    }

    @FXML
    private void addActivity() throws Exception {
        Dialog<Activity> d = new ActivityDialog(user, null);
        d.showAndWait().ifPresent(a -> {
            a.setUser(user);
            Storage.addActivity(a);
            refreshActivityList();
        });
    }

    private void editActivity(Activity sel) throws Exception {
        Dialog<Activity> d = new ActivityDialog(user, sel);
        d.showAndWait().ifPresent(updated -> {
            List<Activity> all = Storage.getActivities();
            int index = all.indexOf(sel);
            if (index != -1) {
                updated.setUser(user);
                all.set(index, updated);
                Storage.saveAll();
            }
            refreshActivityList();
        });
    }

    private void deleteActivity(Activity sel) {
        Storage.getActivities().remove(sel);
        Storage.saveAll();
        refreshActivityList();
    }

    @FXML
    private void searchActivities() {
        filterChanged();
    }

    @FXML
    private void filterChanged() {
        String stext = searchField.getText().toLowerCase();
        String stat = filterStatus.getValue();
        String pr = filterPriority.getValue();
        Category cat = filterCategory.getValue();

        List<Activity> filtered = activities.stream().filter(a ->
                a.getTitle().toLowerCase().contains(stext) &&
                        (stat == null || stat.isEmpty() || ((stat.equals("Selesai")) == a.isCompleted())) &&
                        (pr == null || pr.isEmpty() || pr.equals(a.getPriority())) &&
                        (cat == null || cat.getName().isEmpty() || cat.equals(a.getCategory()))
        ).collect(Collectors.toList());

        renderActivityList(filtered);
    }

    @FXML
    private void manageCategories() {
        Dialog<List<Category>> d = new CategoryDialog(user, categories);
        d.showAndWait().ifPresent(list -> {
            list.forEach(cat -> {
                cat.setUser(user);
                Storage.addCategory(cat);
            });
            categories.setAll(Storage.getCategories().stream()
                    .filter(cat -> cat.getUser().equals(user))
                    .collect(Collectors.toList()));
        });
    }

    @FXML
    private void handleLogout() throws Exception {
        if (logoutCallback != null) logoutCallback.run();
        Stage s = (Stage) activityList.getScene().getWindow();
        s.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/login-view.fxml")))));
        s.setTitle("To-Do List");
    }

    private void refreshActivityList() {
        activities.setAll(Storage.getActivities().stream()
                .filter(a -> a.getUser().equals(user))
                .collect(Collectors.toList()));
        filterChanged(); // apply filter after data refresh
    }

    public void setOnLogout(Runnable callback) {
        this.logoutCallback = callback;
    }
}

