package org.example.project.Controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.project.Model.Activity;
import org.example.project.Model.Category;
import org.example.project.Util.Manager.SessionManager;
import org.example.project.Util.Storage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DashboardController {

    @FXML
    private ComboBox<String> filterStatus;

    @FXML
    private ComboBox<String> filterPriority;

    @FXML
    private ComboBox<Category> filterCategory;

    @FXML
    private TextField searchField;

    @FXML
    private VBox activityList;

    private String currentUser;

    @FXML
    public void initialize() {
        // Get current user from session
        Integer userId = SessionManager.getActiveUserId();
        if (userId != null) {
            // Get username from user ID
            String username = Storage.getUsernameById(userId);
            if (username != null) {
                currentUser = username;
            } else {
                currentUser = "guest"; // Default if username not found
            }
        } else {
            currentUser = "guest"; // Default user if no session
        }

        // Initialize filter comboboxes
        filterStatus.setItems(FXCollections.observableArrayList("Semua", "Selesai", "Belum Selesai"));
        filterStatus.getSelectionModel().selectFirst();

        filterPriority.setItems(FXCollections.observableArrayList("Semua", "High", "Medium", "Low"));
        filterPriority.getSelectionModel().selectFirst();

        // Load categories for current user
        List<Category> categories = Storage.loadCategories(currentUser);
        filterCategory.setItems(FXCollections.observableArrayList(categories));

        // Load activities
        loadActivities();
    }

    private void loadActivities() {
        // Clear current activities
        activityList.getChildren().clear();

        // Get activities from database
        List<Activity> activities = Storage.getActivities(currentUser);

        // Apply filters
        String statusFilter = filterStatus.getValue();
        String priorityFilter = filterPriority.getValue();
        Category categoryFilter = filterCategory.getValue();
        String searchText = searchField.getText().toLowerCase();

        if (statusFilter != null && !statusFilter.equals("Semua")) {
            boolean completed = statusFilter.equals("Selesai");
            activities = activities.stream()
                    .filter(a -> a.isCompleted() == completed)
                    .collect(Collectors.toList());
        }

        if (priorityFilter != null && !priorityFilter.equals("Semua")) {
            activities = activities.stream()
                    .filter(a -> priorityFilter.equals(a.getPriority()))
                    .collect(Collectors.toList());
        }

        if (categoryFilter != null) {
            activities = activities.stream()
                    .filter(a -> a.getCategory().getName().equals(categoryFilter.getName()))
                    .collect(Collectors.toList());
        }

        if (searchText != null && !searchText.isEmpty()) {
            activities = activities.stream()
                    .filter(a -> 
                        a.getTitle().toLowerCase().contains(searchText) ||
                        (a.getDescription() != null && a.getDescription().toLowerCase().contains(searchText)))
                    .collect(Collectors.toList());
        }

        // Create activity items
        for (Activity activity : activities) {
            HBox activityItem = createActivityItem(activity);
            activityList.getChildren().add(activityItem);
        }
    }

    private HBox createActivityItem(Activity activity) {
        HBox item = new HBox(10);
        item.setPadding(new Insets(5));
        item.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #e0e0e0; -fx-border-radius: 5;");

        // Activity details
        Label titleLabel = new Label(activity.getTitle());
        titleLabel.setPrefWidth(150);

        Label dateLabel = new Label(activity.getDate().toString());
        dateLabel.setPrefWidth(100);

        Label categoryLabel = new Label(activity.getCategory().getName());
        categoryLabel.setPrefWidth(100);

        Label priorityLabel = new Label(activity.getPriority());
        priorityLabel.setPrefWidth(80);

        Label statusLabel = new Label(activity.isCompleted() ? "Selesai" : "Belum");
        statusLabel.setPrefWidth(80);

        // Action buttons
        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> editActivity(activity));

        Button deleteButton = new Button("Hapus");
        deleteButton.setOnAction(e -> deleteActivity(activity));

        Button toggleButton = new Button(activity.isCompleted() ? "Batal" : "Selesai");
        toggleButton.setOnAction(e -> toggleActivityStatus(activity));

        // Add all components to the item
        item.getChildren().addAll(
            titleLabel, dateLabel, categoryLabel, priorityLabel, statusLabel,
            editButton, deleteButton, toggleButton
        );

        return item;
    }

    @FXML
    public void addActivity() {
        try {
            ActivityDialog dialog = new ActivityDialog(currentUser, null);
            Optional<Activity> result = dialog.showAndWait();

            if (result.isPresent()) {
                Activity newActivity = result.get();
                boolean success = Storage.addActivity(newActivity);

                if (success) {
                    loadActivities(); // Refresh the list
                } else {
                    showAlert("Error", "Failed to add activity to database.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred: " + e.getMessage());
        }
    }

    private void editActivity(Activity activity) {
        try {
            // Store the original title before editing
            String originalTitle = activity.getTitle();

            ActivityDialog dialog = new ActivityDialog(currentUser, activity);
            Optional<Activity> result = dialog.showAndWait();

            if (result.isPresent()) {
                Activity updatedActivity = result.get();
                // Pass the original title to updateActivity
                boolean success = Storage.updateActivity(updatedActivity);

                if (success) {
                    loadActivities(); // Refresh the list
                } else {
                    showAlert("Error", "Failed to update activity in database.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred: " + e.getMessage());
        }
    }

    private void deleteActivity(Activity activity) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to delete this activity?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = Storage.deleteActivity(activity);

            if (success) {
                loadActivities(); // Refresh the list
            } else {
                showAlert("Error", "Failed to delete activity from database.");
            }
        }
    }

    private void toggleActivityStatus(Activity activity) {
        activity.setCompleted(!activity.isCompleted());
        boolean success = Storage.updateActivity(activity); // hanya kirim 1 parameter

        if (success) {
            loadActivities(); // Refresh the list
        } else {
            showAlert("Error", "Failed to update activity status in database.");
        }
    }


    @FXML
    public void manageCategories() {
        try {
            List<Category> categories = Storage.loadCategories(currentUser);
            CategoryDialog dialog = new CategoryDialog(currentUser, categories);
            Optional<List<Category>> result = dialog.showAndWait();

            if (result.isPresent()) {
                // Refresh category filter
                filterCategory.setItems(FXCollections.observableArrayList(
                    Storage.loadCategories(currentUser)
                ));

                // Refresh activities (in case category names changed)
                loadActivities();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred: " + e.getMessage());
        }
    }

    @FXML
    public void filterChanged() {
        loadActivities();
    }

    @FXML
    public void searchActivities() {
        loadActivities();
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        try {
            // Clear session
            SessionManager.clearSession();

            // Return to login screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login-view.fxml"));
            Parent loginRoot = loader.load();
            Scene loginScene = new Scene(loginRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to logout: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
