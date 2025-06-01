package org.example.project.Controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import org.example.project.Model.Category;
import org.example.project.Util.Storage;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import java.util.List;
import java.util.Optional;

public class CategoryDialog extends Dialog<List<Category>> {

    private final String user;

    public CategoryDialog(String user, List<Category> cats) {
        this.user = user;
        setTitle("Kelola Kategori");

        // Set dialog style
        DialogPane dialogPane = getDialogPane();
        dialogPane.getStyleClass().add("form-pane");
        dialogPane.setPrefWidth(450);
        dialogPane.setPadding(new Insets(20));
        dialogPane.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 2);");

        // Create title label with styling
        Label titleLabel = new Label("Kelola Kategori");
        titleLabel.getStyleClass().add("page-title");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2e3440;");

        // ListView menampilkan kategori milik user
        ListView<Category> lv = new ListView<>(FXCollections.observableArrayList(cats));
        lv.setPrefHeight(250);
        lv.getStyleClass().add("category-list");
        lv.setStyle("-fx-background-color: #eceff4; -fx-background-radius: 6px; -fx-border-color: transparent;");

        // Create styled buttons
        Button add = new Button("Tambah Kategori");
        add.getStyleClass().add("primary-button");
        add.setStyle("-fx-background-color: #5e81ac; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 6px;");

        Button del = new Button("Hapus Kategori");
        del.getStyleClass().add("logout-button");
        del.setStyle("-fx-background-color: #bf616a; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 6px;");

        // Create button container with proper spacing
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(15, 0, 0, 0));
        buttonBox.getChildren().addAll(add, del);

        // Create main layout
        VBox mainLayout = new VBox(15);
        mainLayout.getChildren().addAll(titleLabel, lv, buttonBox);
        VBox.setVgrow(lv, Priority.ALWAYS);

        dialogPane.setContent(mainLayout);

        // Add button types with styling
        ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(ok, ButtonType.CANCEL);

        // Style the buttons
        Button okButton = (Button) dialogPane.lookupButton(ok);
        okButton.getStyleClass().add("primary-button");
        okButton.setStyle("-fx-background-color: #5e81ac; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 6px;");

        Button cancelButton = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
        cancelButton.getStyleClass().add("cancel-button");
        cancelButton.setStyle("-fx-background-color: #4c566a; -fx-text-fill: white; -fx-background-radius: 6px;");

        // Add category action
        add.setOnAction(e -> {
            // Create a custom dialog for adding category
            Dialog<String> inputDialog = new Dialog<>();
            inputDialog.setTitle("Tambah Kategori");

            DialogPane inputDialogPane = inputDialog.getDialogPane();
            inputDialogPane.getStyleClass().add("form-pane");
            inputDialogPane.setPadding(new Insets(20));
            inputDialogPane.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 2);");

            Label inputTitleLabel = new Label("Tambah Kategori Baru");
            inputTitleLabel.getStyleClass().add("page-title");
            inputTitleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2e3440;");

            TextField nameField = new TextField();
            nameField.setPromptText("Nama kategori");
            nameField.getStyleClass().add("input-field");
            nameField.setStyle("-fx-background-color: #eceff4; -fx-background-radius: 6px; -fx-padding: 12px 15px; -fx-font-size: 14px;");

            // Create field container with label
            VBox nameContainer = createFieldContainer("Nama Kategori:", nameField);

            // Create form container
            VBox formContainer = new VBox(15);
            formContainer.setPadding(new Insets(10, 0, 10, 0));
            formContainer.getStyleClass().add("form-container");
            formContainer.setStyle("-fx-background-color: white;");
            formContainer.getChildren().add(nameContainer);

            VBox inputLayout = new VBox(15);
            inputLayout.getChildren().addAll(inputTitleLabel, formContainer);
            inputDialogPane.setContent(inputLayout);

            ButtonType addButtonType = new ButtonType("Tambah", ButtonBar.ButtonData.OK_DONE);
            inputDialogPane.getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

            Button addButton = (Button) inputDialogPane.lookupButton(addButtonType);
            addButton.getStyleClass().add("primary-button");
            addButton.setStyle("-fx-background-color: #5e81ac; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 6px;");

            Button cancelInputButton = (Button) inputDialogPane.lookupButton(ButtonType.CANCEL);
            cancelInputButton.getStyleClass().add("cancel-button");
            cancelInputButton.setStyle("-fx-background-color: #4c566a; -fx-text-fill: white; -fx-background-radius: 6px;");

            // Disable add button if field is empty
            addButton.setDisable(true);
            nameField.textProperty().addListener((obs, oldVal, newVal) -> {
                addButton.setDisable(newVal.trim().isEmpty());
            });

            inputDialog.setResultConverter(dialogButton -> {
                if (dialogButton == addButtonType) {
                    return nameField.getText().trim();
                }
                return null;
            });

            inputDialog.showAndWait().ifPresent(name -> {
                if (!name.isEmpty()) {
                    // Create new category with appropriate user
                    Category newCat = new Category(name, user);

                    // Save to database via Storage
                    if (Storage.addCategory(newCat)) {
                        lv.getItems().add(newCat);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Gagal menambahkan kategori ke database");
                        alert.showAndWait();
                    }
                }
            });
        });

        // Delete category action
        del.setOnAction(e -> {
            Category sel = lv.getSelectionModel().getSelectedItem();
            if (sel != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Konfirmasi Hapus");
                confirm.setHeaderText(null);
                confirm.setContentText("Apakah Anda yakin ingin menghapus kategori ini?");

                Optional<ButtonType> result = confirm.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // Delete from database via Storage
                    if (Storage.deleteCategory(sel)) {
                        lv.getItems().remove(sel);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Gagal menghapus kategori dari database");
                        alert.showAndWait();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informasi");
                alert.setHeaderText(null);
                alert.setContentText("Silakan pilih kategori yang ingin dihapus");
                alert.showAndWait();
            }
        });

        setResultConverter(button -> {
            if (button == ok) {
                return lv.getItems();
            }
            return null;
        });
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
