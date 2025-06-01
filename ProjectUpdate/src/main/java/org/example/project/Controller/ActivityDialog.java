package org.example.project.Controller;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.example.project.Model.Activity;
import org.example.project.Model.Category;
import org.example.project.Util.Storage;

import java.time.LocalDate;
import java.util.List;

public class ActivityDialog extends Dialog<Activity> {

    private TextField titleField;
    private DatePicker datePicker;
    private ComboBox<Category> categoryCombo;
    private ComboBox<String> priorityCombo;
    private TextArea descriptionArea;
    private CheckBox completedCheck;

    public ActivityDialog(String user, Activity activityToEdit) {
        setTitle(activityToEdit == null ? "Tambah Aktivitas" : "Edit Aktivitas");

        // Set dialog style
        DialogPane dialogPane = getDialogPane();
        dialogPane.getStyleClass().add("form-pane");
        dialogPane.setPrefWidth(450);
        dialogPane.setPadding(new Insets(20));
        dialogPane.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 2);");

        // Add button types
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Create title label with styling
        Label titleLabel = new Label("Tambah Aktivitas Baru");
        titleLabel.getStyleClass().add("page-title");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2e3440;");

        // Create form container
        VBox formContainer = new VBox(15);
        formContainer.setPadding(new Insets(20, 0, 10, 0));
        formContainer.getStyleClass().add("form-container");
        formContainer.setStyle("-fx-background-color: white;");

        // Create form fields with styling
        titleField = new TextField();
        titleField.setPromptText("Judul aktivitas");
        titleField.getStyleClass().add("input-field");
        titleField.setStyle("-fx-background-color: #eceff4; -fx-background-radius: 6px; -fx-padding: 12px 15px; -fx-font-size: 14px;");

        datePicker = new DatePicker(LocalDate.now());
        datePicker.getStyleClass().add("input-field");
        datePicker.setStyle("-fx-background-color: #eceff4; -fx-background-radius: 6px;");

        List<Category> categories = Storage.loadCategories(user);
        categoryCombo = new ComboBox<>(FXCollections.observableArrayList(categories));
        categoryCombo.setPromptText("Pilih kategori (opsional)");
        categoryCombo.getStyleClass().add("input-field");
        categoryCombo.setStyle("-fx-background-color: #eceff4; -fx-background-radius: 6px;");

        priorityCombo = new ComboBox<>(FXCollections.observableArrayList("Low", "Medium", "High"));
        priorityCombo.setPromptText("Pilih prioritas (opsional)");
        priorityCombo.getStyleClass().add("input-field");
        priorityCombo.setStyle("-fx-background-color: #eceff4; -fx-background-radius: 6px;");

        descriptionArea = new TextArea();
        descriptionArea.setPromptText("Deskripsi (opsional)");
        descriptionArea.setPrefRowCount(3);
        descriptionArea.getStyleClass().add("input-field");
        descriptionArea.setStyle("-fx-background-color: #eceff4; -fx-background-radius: 6px; -fx-font-size: 14px;");

        completedCheck = new CheckBox("Selesai");
        completedCheck.getStyleClass().add("check-field");
        completedCheck.setStyle("-fx-text-fill: #2e3440;");

        // Create field containers with labels
        VBox titleContainer = createFieldContainer("Judul:", titleField);
        VBox dateContainer = createFieldContainer("Tanggal:", datePicker);
        VBox categoryContainer = createFieldContainer("Kategori:", categoryCombo);
        VBox priorityContainer = createFieldContainer("Prioritas:", priorityCombo);
        VBox descriptionContainer = createFieldContainer("Deskripsi:", descriptionArea);

        // Add completed checkbox with some spacing
        HBox completedContainer = new HBox(10);
        completedContainer.setPadding(new Insets(5, 0, 0, 0));
        completedContainer.getChildren().add(completedCheck);

        // Add all field containers to form container
        formContainer.getChildren().addAll(
            titleContainer,
            dateContainer,
            categoryContainer,
            priorityContainer,
            descriptionContainer,
            completedContainer
        );

        // Create main layout
        VBox mainLayout = new VBox(15);
        mainLayout.getChildren().addAll(titleLabel, formContainer);

        dialogPane.setContent(mainLayout);

        // Set field values if editing existing activity
        if (activityToEdit != null) {
            titleLabel.setText("Edit Aktivitas");
            titleField.setText(activityToEdit.getTitle());
            datePicker.setValue(activityToEdit.getDate());
            categoryCombo.getSelectionModel().select(activityToEdit.getCategory());
            priorityCombo.getSelectionModel().select(activityToEdit.getPriority());
            descriptionArea.setText(activityToEdit.getDescription());
            completedCheck.setSelected(activityToEdit.isCompleted());
        }

        // Style the buttons
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.getStyleClass().add("primary-button");
        okButton.setStyle("-fx-background-color: #5e81ac; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 6px;");

        Button cancelButton = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
        cancelButton.getStyleClass().add("cancel-button");
        cancelButton.setStyle("-fx-background-color: #4c566a; -fx-text-fill: white; -fx-background-radius: 6px;");

        // Enable OK button if any of the fields is filled
        okButton.setDisable(activityToEdit == null && isAllFieldsEmpty());

        // Add listeners to all relevant fields to check if any is filled
        titleField.textProperty().addListener((obs, oldVal, newVal) -> {
            okButton.setDisable(isAllFieldsEmpty());
        });

        categoryCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            okButton.setDisable(isAllFieldsEmpty());
        });

        priorityCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            okButton.setDisable(isAllFieldsEmpty());
        });

        descriptionArea.textProperty().addListener((obs, oldVal, newVal) -> {
            okButton.setDisable(isAllFieldsEmpty());
        });

        setResultConverter(new Callback<ButtonType, Activity>() {
            @Override
            public Activity call(ButtonType b) {
                if (b == ButtonType.OK) {
                    String title = titleField.getText().trim();
                    LocalDate date = datePicker.getValue();
                    Category category = categoryCombo.getSelectionModel().getSelectedItem();
                    String priority = priorityCombo.getSelectionModel().getSelectedItem();
                    String description = descriptionArea.getText().trim();
                    boolean completed = completedCheck.isSelected();

                    if (activityToEdit != null) {
                        activityToEdit.setTitle(title);
                        activityToEdit.setDate(date != null ? date : LocalDate.now());
                        activityToEdit.setCategory(category); // Can be null
                        activityToEdit.setPriority(priority); // Can be null
                        activityToEdit.setDescription(description);
                        activityToEdit.setCompleted(completed);
                        return activityToEdit;
                    } else {
                        return new Activity(0, title, 
                                           date != null ? date : LocalDate.now(), 
                                           category, // Can be null
                                           priority, // Can be null
                                           completed, user)
                                .setDescription(description);
                    }
                }
                return null;
            }
        });
    }

    /**
     * Checks if all fields are empty
     * @return true if all fields are empty, false otherwise
     */
    private boolean isAllFieldsEmpty() {
        boolean titleEmpty = titleField.getText().trim().isEmpty();
        boolean categoryEmpty = categoryCombo.getSelectionModel().getSelectedItem() == null;
        boolean priorityEmpty = priorityCombo.getSelectionModel().getSelectedItem() == null;
        boolean descriptionEmpty = descriptionArea.getText().trim().isEmpty();

        // Return true only if all fields are empty
        return titleEmpty && categoryEmpty && priorityEmpty && descriptionEmpty;
    }

    /**
     * Creates a container for a field with its label
     * @param labelText The text for the label
     * @param field The control to add
     * @return A VBox containing the label and field
     */
    private VBox createFieldContainer(String labelText, Control field) {
        VBox container = new VBox(5);
        Label label = new Label(labelText);
        label.getStyleClass().add("field-label");
        label.setStyle("-fx-font-weight: bold; -fx-text-fill: #2e3440;");

        container.getChildren().addAll(label, field);

        // Make the field take up all available width
        VBox.setVgrow(field, Priority.NEVER);
        field.setMaxWidth(Double.MAX_VALUE);

        return container;
    }
}
