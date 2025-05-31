package org.example.project;

import org.example.project.Controller.DashboardController;
import org.example.project.Util.Manager.SessionManager;
import org.example.project.Util.Storage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        try {
            // Inisialisasi database
            Storage.initializeStorage();

            // Cek apakah ada sesi login
            Integer userId = SessionManager.getActiveUserId();
            String username = null;
            if (userId != null) {
                username = Storage.getUsernameById(userId);
            }

            URL fxmlLocation;
            if (username != null) {
                // Langsung ke dashboard jika sudah login
                fxmlLocation = getClass().getResource("/fxml/dashboard-view.fxml");
                if (fxmlLocation == null) {
                    fxmlLocation = getClass().getResource("dashboard-view.fxml");
                }
            } else {
                // Tampilkan login jika belum login
                fxmlLocation = getClass().getResource("/fxml/login-view.fxml");
                if (fxmlLocation == null) {
                    fxmlLocation = getClass().getResource("login-view.fxml");
                }
            }

            if (fxmlLocation == null) {
                throw new IOException("FXML file tidak ditemukan.");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            // If loading dashboard, set the primary stage in the controller
            if (username != null) {
                DashboardController controller = loader.getController();
                controller.setPrimaryStage(stage);
            }

            Scene scene = new Scene(root);

            // Load appropriate CSS based on the view
            String cssFile = username != null ? "/css/dashboard.css" : "/css/login.css";
            URL cssURL = getClass().getResource(cssFile);
            if (cssURL != null) {
                scene.getStylesheets().add(cssURL.toExternalForm());
            } else {
                System.err.println("File " + cssFile + " tidak ditemukan.");
            }

            // Apply UI settings from session
            applyUiSettings(scene, root);

            stage.setTitle("To-Do List");
            stage.setScene(scene);
            stage.show();

            // Save window size and position in session when closing
            stage.setOnCloseRequest(event -> {
                // You could add code here to save window size/position if needed
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Applies UI settings from the session to the scene
     * @param scene The scene to apply settings to
     * @param root The root node of the scene
     */
    private void applyUiSettings(Scene scene, Parent root) {
        // Get UI settings from session
        double fontSize = SessionManager.getFontSize();
        double uiScale = SessionManager.getUiScale();

        // Apply font size to the root node
        root.setStyle("-fx-font-size: " + fontSize + "px;");

        // Apply UI scale using CSS transform
        root.setScaleX(uiScale);
        root.setScaleY(uiScale);

        // Center the scaled content
        root.setTranslateX((root.getBoundsInLocal().getWidth() * (uiScale - 1)) / 2);
        root.setTranslateY((root.getBoundsInLocal().getHeight() * (uiScale - 1)) / 2);

        System.out.println("Applied UI settings: fontSize=" + fontSize + ", uiScale=" + uiScale);
    }



    public static void main(String[] args) {
        launch();
    }
}
