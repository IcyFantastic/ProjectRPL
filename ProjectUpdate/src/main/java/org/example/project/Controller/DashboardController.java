package org.example.project.Controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.example.project.Util.Manager.SessionManager.currentSession;
import static org.example.project.Util.Manager.SessionManager.getActiveUserId;

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

    private Stage primaryStage;

    private static final int BASE_HEIGHT = 500;
    private static final int ROW_HEIGHT = 55;
    private static final int MAX_VISIBLE_ACTIVITIES = 6;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    public void initialize() {
        // Get current user from session
        Integer userId = getActiveUserId();
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

        // >>> Apply UI scale and font size preferences from session
        double fontSize = SessionManager.getFontSize(); // misal default 12
        double uiScale = SessionManager.getUiScale();   // misal default 1.0

        // Terapkan ke root VBox jika mungkin
        if (activityList.getScene() != null && activityList.getScene().getRoot() != null) {
            activityList.getScene().getRoot().setStyle("-fx-font-size: " + fontSize + "px;");
            activityList.getScene().getRoot().setScaleX(uiScale);
            activityList.getScene().getRoot().setScaleY(uiScale);
        } else {
            // Jika belum tersedia (karena initialize dipanggil sebelum scene dimuat), jalankan nanti
            activityList.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null && newScene.getRoot() != null) {
                    newScene.getRoot().setStyle("-fx-font-size: " + fontSize + "px;");
                    newScene.getRoot().setScaleX(uiScale);
                    newScene.getRoot().setScaleY(uiScale);
                }
            });
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

        // Check for today's activities
        checkReminders();
    }

    private void checkReminders() {
        List<Activity> activities = Storage.getActivities(currentUser);
        long count = activities.stream()
                .filter(a -> a.getDate().equals(LocalDate.now()) && !a.isCompleted())
                .count();

        if (count > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Pengingat Aktivitas");
            alert.setHeaderText(null);

            // Style the alert dialog
            styleAlertDialog(alert, "Pengingat Aktivitas", "Ada " + count + " aktivitas hari ini");

            alert.showAndWait();
        }
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

        // Adjust window height based on number of activities
        adjustWindowHeight();
    }

    private void adjustWindowHeight() {
        if (primaryStage == null) return;

        int activityCount = activityList.getChildren().size();
        int visibleCount = Math.min(activityCount, MAX_VISIBLE_ACTIVITIES);

        double newHeight = BASE_HEIGHT + (visibleCount * ROW_HEIGHT);
        primaryStage.setHeight(newHeight);

        // Ensure minimum width for the new layout with sidebar and wider action column
        if (primaryStage.getWidth() < 950) {
            primaryStage.setWidth(950);
        }

        // Make sure the window is centered on the screen
        primaryStage.centerOnScreen();
    }

    private HBox createActivityItem(Activity activity) {
        HBox item = new HBox(10);
        item.getStyleClass().add("activity-item");
        item.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        // Activity details
        Label titleLabel = new Label(activity.getTitle());
        titleLabel.setPrefWidth(200);
        titleLabel.setMaxWidth(200);
        titleLabel.setMinWidth(200);
        titleLabel.setWrapText(true);

        Label dateLabel = new Label(activity.getDate().toString());
        dateLabel.setPrefWidth(100);
        dateLabel.setMaxWidth(100);
        dateLabel.setMinWidth(100);

        Label categoryLabel = new Label(activity.getCategory() != null ? activity.getCategory().getName() : "-");
        categoryLabel.setPrefWidth(120);
        categoryLabel.setMaxWidth(120);
        categoryLabel.setMinWidth(120);

        String priorityText = activity.getPriority() != null ? activity.getPriority() : "-";
        Label priorityLabel = new Label(priorityText);
        priorityLabel.setPrefWidth(100);
        priorityLabel.setMaxWidth(100);
        priorityLabel.setMinWidth(100);
        if (activity.getPriority() != null) {
            priorityLabel.getStyleClass().add("priority-" + activity.getPriority().toLowerCase());
        }

        Label statusLabel = new Label(activity.isCompleted() ? "Selesai" : "Belum");
        statusLabel.setPrefWidth(100);
        statusLabel.setMaxWidth(100);
        statusLabel.setMinWidth(100);
        statusLabel.getStyleClass().add(activity.isCompleted() ? "status-selesai" : "status-belum");

        // Action buttons in an HBox with more space
        HBox actionBox = new HBox(8);
        actionBox.setAlignment(javafx.geometry.Pos.CENTER);
        actionBox.setPrefWidth(250);
        actionBox.setMaxWidth(250);
        actionBox.setMinWidth(250);

        // Create buttons with better styling
        Button editButton = new Button("Edit");
        editButton.getStyleClass().addAll("manage-button", "action-button");
        editButton.setOnAction(e -> editActivity(activity));
        editButton.setPrefWidth(70);

        Button deleteButton = new Button("Hapus");
        deleteButton.getStyleClass().addAll("logout-button", "action-button");
        deleteButton.setOnAction(e -> deleteActivity(activity));
        deleteButton.setPrefWidth(70);

        Button toggleButton = new Button(activity.isCompleted() ? "Tandai Belum" : "Selesai");
        toggleButton.getStyleClass().addAll("add-button", "action-button");
        toggleButton.setOnAction(e -> toggleActivityStatus(activity));
        toggleButton.setPrefWidth(90);

        actionBox.getChildren().addAll(editButton, deleteButton, toggleButton);

        // Add all components to the item
        item.getChildren().addAll(
            titleLabel, dateLabel, categoryLabel, priorityLabel, statusLabel, actionBox
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
        confirm.setTitle("Konfirmasi Hapus");
        confirm.setHeaderText(null);

        // Style the confirmation dialog
        styleAlertDialog(confirm, "Konfirmasi Hapus", "Apakah Anda yakin ingin menghapus aktivitas ini?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = Storage.deleteActivity(activity);

            if (success) {
                loadActivities(); // Refresh the list
            } else {
                showAlert("Error", "Gagal menghapus aktivitas dari database.");
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

            // Load login CSS
            URL cssURL = getClass().getResource("/css/login.css");
            if (cssURL != null) {
                loginScene.getStylesheets().add(cssURL.toExternalForm());
            } else {
                System.err.println("File login.css tidak ditemukan.");
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to logout: " + e.getMessage());
        }
    }

    /**
     * Helper method to style an alert dialog to match the UI of other screens
     * @param alert The alert dialog to style
     * @param title The title of the alert
     * @param message The message to display
     */
    private void styleAlertDialog(Alert alert, String title, String message) {
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStyleClass().add("form-pane");
        dialogPane.setPrefWidth(400);
        dialogPane.setPadding(new Insets(20));
        dialogPane.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 2);");

        // Style the buttons
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        if (okButton != null) {
            okButton.getStyleClass().add("primary-button");
            okButton.setStyle("-fx-background-color: #5e81ac; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 6px;");
        }

        // Style cancel button if present
        Button cancelButton = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
        if (cancelButton != null) {
            cancelButton.setStyle("-fx-background-color: #4c566a; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 6px;");
        }

        // Add a custom header with icon and title
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2e3440;");

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #4c566a; -fx-wrap-text: true;");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(360);

        header.getChildren().addAll(titleLabel, messageLabel);

        // Replace the default content with our custom content
        dialogPane.setContent(header);

        // Remove the default content text since we're using our custom label
        alert.setContentText(null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);

        // Style the alert dialog
        styleAlertDialog(alert, title, message);

        alert.showAndWait();
    }

    public static void initializeSession() {
        getActiveUserId(); // Load session data on startup
        System.out.println("Session initialized: " + currentSession);
    }

}
