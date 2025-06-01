package org.example.project.Controller;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
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

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        titleField = new TextField();
        titleField.setPromptText("Judul aktivitas");

        datePicker = new DatePicker(LocalDate.now());

        List<Category> categories = Storage.loadCategories(user);
        categoryCombo = new ComboBox<>(FXCollections.observableArrayList(categories));
        categoryCombo.setPromptText("Pilih kategori");

        priorityCombo = new ComboBox<>(FXCollections.observableArrayList("Low", "Medium", "High"));
        priorityCombo.setPromptText("Pilih prioritas");

        descriptionArea = new TextArea();
        descriptionArea.setPromptText("Deskripsi (opsional)");
        descriptionArea.setPrefRowCount(3);

        completedCheck = new CheckBox("Selesai");

        grid.add(new Label("Judul:"), 0, 0);
        grid.add(titleField, 1, 0);

        grid.add(new Label("Tanggal:"), 0, 1);
        grid.add(datePicker, 1, 1);

        grid.add(new Label("Kategori:"), 0, 2);
        grid.add(categoryCombo, 1, 2);

        grid.add(new Label("Prioritas:"), 0, 3);
        grid.add(priorityCombo, 1, 3);

        grid.add(new Label("Deskripsi:"), 0, 4);
        grid.add(descriptionArea, 1, 4);

        grid.add(completedCheck, 1, 5);

        getDialogPane().setContent(grid);

        if (activityToEdit != null) {
            titleField.setText(activityToEdit.getTitle());
            datePicker.setValue(activityToEdit.getDate());
            categoryCombo.getSelectionModel().select(activityToEdit.getCategory());
            priorityCombo.getSelectionModel().select(activityToEdit.getPriority());
            descriptionArea.setText(activityToEdit.getDescription());
            completedCheck.setSelected(activityToEdit.isCompleted());
        }

        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(false); // langsung enabled karena sudah ada judul lama
        titleField.textProperty().addListener((obs, oldVal, newVal) -> {
            okButton.setDisable(newVal.trim().isEmpty());
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
                        activityToEdit.setDate(date);
                        activityToEdit.setCategory(category);
                        activityToEdit.setPriority(priority);
                        activityToEdit.setDescription(description);
                        activityToEdit.setCompleted(completed);
                        return activityToEdit;
                    } else {
                        return new Activity(0, title, date, category, priority, completed, user)
                                .setDescription(description);
                    }
                }
                return null;
            }
        });
    }
}
