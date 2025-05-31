//package org.example.project.Controller;
//
//import javafx.collections.FXCollections;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//import org.example.project.Model.Activity;
//import org.example.project.Model.Category;
//import org.example.project.Util.Manager.SessionManager;
//import org.example.project.Util.Storage;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//public class MainController {
//
//    @FXML private TableView<Activity> activityTable;
//    @FXML private TableColumn<Activity, String> colTitle;
//    @FXML private TableColumn<Activity, LocalDate> colDate;
//    @FXML private TableColumn<Activity, String> colCategory;
//    @FXML private TableColumn<Activity, String> colPriority;
//    @FXML private TableColumn<Activity, Boolean> colStatus;
//    @FXML private TableColumn<Activity, String> colDescription;
//    @FXML private TextField searchField;
//    @FXML private ComboBox<String> filterStatus;
//    @FXML private ComboBox<String> filterPriority;
//    @FXML private ComboBox<Category> filterCategory;
//    @FXML private Label welcomeLabel;
//
//    private String user;
//
//    @FXML
//    private void initialize() {
//        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
//        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
//        colCategory.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
//        colPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
//        colStatus.setCellValueFactory(new PropertyValueFactory<>("completed"));
//        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
//
//        // Initialize filter comboboxes
//        filterStatus.setItems(FXCollections.observableArrayList("Semua", "Selesai", "Belum Selesai"));
//        filterPriority.setItems(FXCollections.observableArrayList("Semua", "Tinggi", "Sedang", "Rendah"));
//    }
//
//    public void initData(String username) {
//        this.user = username;
//        welcomeLabel.setText("Selamat datang, " + user + "!");
//
//        filterCategory.setItems(FXCollections.observableArrayList(Storage.loadCategories(user)));
//
//        activityTable.setItems(FXCollections.observableArrayList(
//                new ArrayList<>(Storage.getActivities(user))
//        ));
//    }
//
//    private void filterByCategory() {
//        Category selected = filterCategory.getSelectionModel().getSelectedItem();
//        if (selected == null) return;
//
//        activityTable.setItems(FXCollections.observableArrayList(
//                Storage.getActivities(user).stream()
//                        .filter(a -> a.getCategory().getName().equals(selected.getName()))
//                        .collect(Collectors.toList())
//        ));
//    }
//
//    @FXML
//    private void filterChanged() {
//        String status = filterStatus.getValue();
//        String priority = filterPriority.getValue();
//        Category category = filterCategory.getValue();
//
//        // Get all activities for the user
//        List<Activity> activities = new ArrayList<>(Storage.getActivities(user));
//
//        // Apply filters
//        if (status != null && !status.equals("Semua")) {
//            boolean completed = status.equals("Selesai");
//            activities = activities.stream()
//                    .filter(a -> a.isCompleted() == completed)
//                    .collect(Collectors.toList());
//        }
//
//        if (priority != null && !priority.equals("Semua")) {
//            activities = activities.stream()
//                    .filter(a -> priority.equals(a.getPriority()))
//                    .collect(Collectors.toList());
//        }
//
//        if (category != null) {
//            activities = activities.stream()
//                    .filter(a -> a.getCategory().getName().equals(category.getName()))
//                    .collect(Collectors.toList());
//        }
//
//        activityTable.setItems(FXCollections.observableArrayList(activities));
//    }
//
//    @FXML
//    private void searchActivities() {
//        String searchText = searchField.getText().toLowerCase();
//        if (searchText.isEmpty()) {
//            activityTable.setItems(FXCollections.observableArrayList(Storage.getActivities(user)));
//            return;
//        }
//
//        List<Activity> filteredList = Storage.getActivities(user).stream()
//                .filter(activity ->
//                    activity.getTitle().toLowerCase().contains(searchText) ||
//                    activity.getDescription().toLowerCase().contains(searchText) ||
//                    activity.getCategory().getName().toLowerCase().contains(searchText))
//                .collect(Collectors.toList());
//
//        activityTable.setItems(FXCollections.observableArrayList(filteredList));
//    }
//
//    @FXML
//    private void handleAdd() throws Exception {
//        ActivityDialog dialog = new ActivityDialog(user, null);
//        Optional<Activity> result = dialog.showAndWait();
//        result.ifPresent(activity -> {
//            Storage.addActivity(activity);
//            activityTable.getItems().add(activity);
//        });
//    }
//
//    @FXML
//    private void handleEdit() throws Exception {
//        Activity selected = activityTable.getSelectionModel().getSelectedItem();
//        if (selected == null) return;
//
//        ActivityDialog dialog = new ActivityDialog(user, selected);
//        Optional<Activity> result = dialog.showAndWait();
//        result.ifPresent(updated -> {
//            selected.setTitle(updated.getTitle());
//            selected.setDate(updated.getDate());
//            selected.setCategory(updated.getCategory());
//            selected.setPriority(updated.getPriority());
//            selected.setDescription(updated.getDescription());
//            Storage.updateActivity(selected);
//            activityTable.refresh();
//        });
//    }
//
//    @FXML
//    private void handleDelete() {
//        Activity selected = activityTable.getSelectionModel().getSelectedItem();
//        if (selected == null) return;
//
//        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
//        confirm.setTitle("Konfirmasi");
//        confirm.setHeaderText(null);
//        confirm.setContentText("Yakin ingin menghapus aktivitas?");
//
//        Optional<ButtonType> result = confirm.showAndWait();
//        if (result.isPresent() && result.get() == ButtonType.OK) {
//            Storage.deleteActivity(selected);
//            activityTable.getItems().remove(selected);
//        }
//    }
//
//    @FXML
//    private void handleLogout() throws Exception {
//        SessionManager.clearSession();
//        Stage s = (Stage) activityTable.getScene().getWindow();
//        s.setScene(new Scene(FXMLLoader.load(getClass().getResource("/fxml/login-view.fxml"))));
//        s.setTitle("Login");
//    }
//
//    @FXML
//    private void handleMarkDone() {
//        Activity selected = activityTable.getSelectionModel().getSelectedItem();
//        if (selected == null) return;
//        selected.setCompleted(true);
//        Storage.updateActivity(selected);
//        activityTable.refresh();
//    }
//
//    @FXML
//    private void manageCategories() throws Exception {
//        // Get current categories for the user
//        List<Category> categories = Storage.loadCategories(user);
//
//        // Create and show the category dialog
//        CategoryDialog dialog = new CategoryDialog(user, categories);
//        Optional<List<Category>> result = dialog.showAndWait();
//
//        // If the dialog was confirmed and categories were updated
//        result.ifPresent(updatedCategories -> {
//            // Update the category filter combobox
//            filterCategory.setItems(FXCollections.observableArrayList(Storage.loadCategories(user)));
//
//            // Refresh the activity table to reflect any category changes
//            activityTable.refresh();
//        });
//    }
//}
