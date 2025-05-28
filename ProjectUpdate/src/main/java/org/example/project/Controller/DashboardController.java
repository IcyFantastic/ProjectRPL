package org.example.project.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DashboardController {

    @FXML
    private VBox navMenu;

    @FXML
    private Button addButton;

    @FXML
    public void initialize() {
        System.out.println("TodoController initialized!");

        // Event: Sidebar navigation clicked
        navMenu.getChildren().forEach(node -> {
            if (node instanceof Label label) {
                label.setOnMouseClicked(event -> {
                    navMenu.getChildren().forEach(n -> n.getStyleClass().remove("selected"));
                    label.getStyleClass().add("selected");
                    System.out.println("Navigated to: " + label.getText());
                });
            }
        });

        // Event: "+" button clicked
        addButton.setOnAction(e -> {
            System.out.println("Add button clicked!");
            // Nanti bisa tampilkan popup form untuk input task baru
        });
    }
}
