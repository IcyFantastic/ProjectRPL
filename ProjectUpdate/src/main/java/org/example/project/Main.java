package org.example.project;

import org.example.project.Util.Storage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        try {
            // Inisialisasi database
            Storage.initializeStorage();

            // Load FXML login
            URL fxmlLocation = getClass().getResource("/fxml/login-view.fxml");
            if (fxmlLocation == null) {
                fxmlLocation = getClass().getResource("login-view.fxml");
            }

            if (fxmlLocation == null) {
                throw new IOException("File /fxml/login-view.fxml tidak ditemukan.");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            // Buat scene dan sambungkan CSS
            Scene scene = new Scene(root);
            URL cssURL = getClass().getResource("/css/login.css");
            if (cssURL != null) {
                scene.getStylesheets().add(cssURL.toExternalForm());
            } else {
                System.err.println("File login.css tidak ditemukan.");
            }

            stage.setTitle("To-Do List");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
