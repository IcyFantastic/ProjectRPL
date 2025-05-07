package org.example.project.Controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import org.example.project.Model.Activity;
import org.example.project.Model.Category;
import org.example.project.Util.Storage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {
    @FXML private TableView<Activity> activityTable;
    @FXML private TableColumn<Activity, String> colTitle;
    @FXML private TableColumn<Activity, LocalDate> colDate;
    @FXML private TableColumn<Activity, Category> colCategory;
    @FXML private TableColumn<Activity, String> colPriority;
    @FXML private TableColumn<Activity, Boolean> colStatus;
    @FXML private ComboBox<String> filterStatus;
    @FXML private ComboBox<String> filterPriority;
    @FXML private ComboBox<Category> filterCategory;
    @FXML private TextField searchField;

    private ObservableList<Activity> activities;
    private ObservableList<Category> categories;
    private String user;

    public void initData(String username) throws Exception {
        this.user = username;
        List<Category> cats = Storage.loadCategories(user);
        categories = FXCollections.observableArrayList(cats);
        categories.add(new Category(""));
        filterCategory.setItems(categories);
        List<Activity> acts = Storage.loadActivities(user);
        activities = FXCollections.observableArrayList(acts);
        colTitle.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().getTitle()));
        colDate.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().getDate()));
        colCategory.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().getCategory()));
        colPriority.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().getPriority()));
        colStatus.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().isCompleted()));
        activityTable.setItems(activities);
        filterStatus.setItems(FXCollections.observableArrayList("","Selesai","Belum"));
        filterPriority.setItems(FXCollections.observableArrayList("","Tinggi","Sedang","Rendah"));
        checkReminders();
    }

    private void checkReminders() {
        LocalDate today = LocalDate.now();
        long cnt = activities.stream().filter(a -> a.getDate().equals(today) && !a.isCompleted()).count();
        if (cnt>0) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("Ada " + cnt + " aktivitas hari ini");
            a.showAndWait();
        }
    }

    @FXML
    private void addActivity() throws Exception {
        Dialog<Activity> d = new ActivityDialog(user, null);
        d.showAndWait().ifPresent(a -> { activities.add(a); save(); });
    }

    @FXML
    private void editActivity() throws Exception {
        Activity sel = activityTable.getSelectionModel().getSelectedItem();
        if (sel==null) return;
        Dialog<Activity> d = new ActivityDialog(user, sel);
        d.showAndWait().ifPresent(a -> { activities.set(activities.indexOf(sel), a); save(); });
    }

    @FXML
    private void deleteActivity() {
        Activity sel = activityTable.getSelectionModel().getSelectedItem();
        if (sel!=null) { activities.remove(sel); save(); }
    }

    @FXML
    private void markCompleted() {
        Activity sel = activityTable.getSelectionModel().getSelectedItem();
        if (sel!=null) {
            sel.setCompleted(!sel.isCompleted());
            activityTable.refresh();
            save();
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
                        (stat==null||stat.isEmpty()||((stat.equals("Selesai"))==a.isCompleted())) &&
                        (pr==null||pr.isEmpty()||pr.equals(a.getPriority())) &&
                        (cat==null||cat.getName().isEmpty()||cat.equals(a.getCategory()))
        ).collect(Collectors.toList());
        activityTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    private void manageCategories() {
        Dialog<List<Category>> d = new CategoryDialog(user, categories);
        d.showAndWait().ifPresent(list -> { categories.setAll(list); saveCats(); });
    }

    @FXML
    private void handleLogout() throws Exception {
        Stage s = (Stage) activityTable.getScene().getWindow();
        s.setScene(new Scene(FXMLLoader.load(getClass().getResource("/fxml/login_view.fxml"))));
        s.setTitle("To-Do List");
    }

    private void save() {
        try { Storage.saveActivities(user, activities); } catch (Exception e) {}
    }

    private void saveCats() {
        try { Storage.saveCategories(user, categories); } catch (Exception e) {}
    }
}
