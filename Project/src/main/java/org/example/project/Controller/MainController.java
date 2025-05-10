// MainController.java
package org.example.project.Controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.project.Model.Activity;
import org.example.project.Model.Category;
import org.example.project.Util.Storage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {
    @FXML private TableView<Activity> activityTable;
    @FXML private TableColumn<Activity, String> colTitle;
    @FXML private TableColumn<Activity, LocalDate> colDate;
    @FXML private TableColumn<Activity, Category> colCategory;
    @FXML private TableColumn<Activity, String> colPriority;
    @FXML private TableColumn<Activity, String> colStatus;
    @FXML private ComboBox<String> filterStatus;
    @FXML private ComboBox<String> filterPriority;
    @FXML private ComboBox<Category> filterCategory;
    @FXML private TextField searchField;
    @FXML private TableColumn<Activity, String> colDescription;

    private ObservableList<Activity> activities;
    private ObservableList<Category> categories;
    private String user;
    private Runnable logoutCallback; // Untuk menyimpan data saat logout

    public void initData(String username) {
        this.user = username;

        // Ambil kategori yang terkait dengan pengguna
        categories = FXCollections.observableArrayList(Storage.getCategories().stream()
                .filter(c -> c.getUser().equals(user)) // Pastikan kategori milik pengguna
                .collect(Collectors.toList()));
        categories.add(new Category("")); // Tambahkan kategori kosong untuk filter
        filterCategory.setItems(categories);

        // Ambil aktivitas yang terkait dengan pengguna
        activities = FXCollections.observableArrayList(Storage.getActivities().stream()
                .filter(a -> a.getUser().equals(user)) // Pastikan aktivitas milik pengguna
                .collect(Collectors.toList()));

        // Atur kolom tabel
        colTitle.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().getTitle()));
        colDate.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().getDate()));
        colCategory.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().getCategory()));
        colPriority.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().getPriority()));
        colStatus.setCellValueFactory(d ->
                new ReadOnlyStringWrapper(d.getValue().isCompleted() ? "Selesai" : "Belum")
        );
        colDescription.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().getDescription()));
        activityTable.setItems(activities);

        // Atur filter
        filterStatus.setItems(FXCollections.observableArrayList("", "Selesai", "Belum"));
        filterPriority.setItems(FXCollections.observableArrayList("", "Tinggi", "Sedang", "Rendah"));

        // Cek pengingat aktivitas
        checkReminders();
    }

    public void setOnLogout(Runnable callback) {
        this.logoutCallback = callback;
    }

    private void checkReminders() {
        LocalDate today = LocalDate.now();
        long cnt = activities.stream().filter(a -> a.getDate().equals(today) && !a.isCompleted()).count();
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
            Storage.addActivity(a); // langsung simpan
            activities.setAll(Storage.getActivities().stream()
                    .filter(act -> act.getUser().equals(user))
                    .collect(Collectors.toList()));
        });
    }


    @FXML
    private void editActivity() throws Exception {
        Activity sel = activityTable.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        Dialog<Activity> d = new ActivityDialog(user, sel);
        d.showAndWait().ifPresent(updated -> {
            // Update data pada Storage
            List<Activity> all = Storage.getActivities();
            int index = all.indexOf(sel);
            if (index != -1) {
                updated.setUser(user); // pastikan user tetap diset
                all.set(index, updated);
                Storage.saveAll(); // simpan perubahan
            }

            // Refresh tampilan
            activities.setAll(Storage.getActivities().stream()
                    .filter(a -> a.getUser().equals(user))
                    .collect(Collectors.toList()));
        });
    }


    @FXML
    private void deleteActivity() {
        Activity sel = activityTable.getSelectionModel().getSelectedItem();
        if (sel != null) {
            Storage.getActivities().remove(sel); // hapus dari storage
            Storage.saveAll(); // simpan perubahan

            // Refresh tampilan
            activities.setAll(Storage.getActivities().stream()
                    .filter(a -> a.getUser().equals(user))
                    .collect(Collectors.toList()));
        }
    }


    @FXML
    private void markCompleted() {
        Activity sel = activityTable.getSelectionModel().getSelectedItem();
        if (sel != null) {
            sel.setCompleted(!sel.isCompleted());
            Storage.saveAll(); // simpan perubahan status
            activityTable.refresh();
        }
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
        activityTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    private void manageCategories() {
        Dialog<List<Category>> d = new CategoryDialog(user, categories);
        d.showAndWait().ifPresent(list -> {
            list.forEach(cat -> {
                cat.setUser(user);
                Storage.addCategory(cat); // langsung simpan
            });
            categories.setAll(Storage.getCategories().stream()
                    .filter(cat -> cat.getUser().equals(user))
                    .collect(Collectors.toList()));
        });
    }


    @FXML
    private void handleLogout() throws Exception {
        if (logoutCallback != null) logoutCallback.run();
        Stage s = (Stage) activityTable.getScene().getWindow();
        s.setScene(new Scene(FXMLLoader.load(getClass().getResource("/fxml/login-view.fxml"))));
        s.setTitle("To-Do List");
    }
}
