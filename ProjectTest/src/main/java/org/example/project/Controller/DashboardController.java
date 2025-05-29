package org.example.project.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.project.Model.Activity;
import org.example.project.Model.Category;

import java.time.LocalDate;

public class DashboardController {

    @FXML
    private ListView<Activity> activityListView;

    @FXML
    private TextField activityField;

    private ObservableList<Activity> activityList;

    public DashboardController(ListView<Activity> activityListView, TextField activityField) {
        this.activityListView = activityListView;
        this.activityField = activityField;
    }

    @FXML
    public void initialize() {
        activityList = FXCollections.observableArrayList();
        activityListView.setItems(activityList);

        // Menentukan cara menampilkan Activity dalam ListView
        activityListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Activity activity, boolean empty) {
                super.updateItem(activity, empty);
                if (empty || activity == null) {
                    setText(null);
                } else {
                    String status = activity.isCompleted() ? "[✓]" : "[ ]";
                    setText(status + " " + activity.getTitle());
                }
            }
        });
    }

    @FXML
    private void handleAddActivity() {
        String title = activityField.getText().trim();
        if (!title.isEmpty()) {
            Activity activity = new Activity(
                    title,
                    LocalDate.now(),
                    new Category("Default", "", ""),  // Contoh kategori default
                    "Medium",                         // Default priority
                    false,                            // Default belum selesai
                    "user1"                           // Contoh username
            );
            activityList.add(activity);
            activityField.clear();
        }
    }

    @FXML
    private void handleDeleteActivity() {
        Activity selected = activityListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            activityList.remove(selected);
        }
    }
}
