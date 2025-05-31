package org.example.project.Controller;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.project.Util.Manager.SessionManager;
import org.example.project.Util.Storage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private VBox brandingPane;
    @FXML
    private AnchorPane formPane;      // Pane yang akan di-fade/slide masuk

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Fade in formPane
        FadeTransition fade = new FadeTransition(Duration.millis(700), formPane);
        fade.setFromValue(0);
        fade.setToValue(1);

        // Slide up formPane
        TranslateTransition slideForm = new TranslateTransition(Duration.millis(700), formPane);
        slideForm.setFromY(50);
        slideForm.setToY(0);

        // Slide brandingPane dari kanan
        if (brandingPane != null) {
            TranslateTransition slideBranding = new TranslateTransition(Duration.millis(700), brandingPane);
            slideBranding.setFromX(300);
            slideBranding.setToX(0);
            slideBranding.play();
        }

        fade.play();
        slideForm.play();
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        Integer userId = Storage.validateUser(username, password);
        if (userId != null) {
            // Simpan sesi login
            SessionManager.saveSession(userId);

            // Login berhasil, ganti scene ke dashboard
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard-view.fxml"));
                Parent root = loader.load();

                // Dapatkan stage dari event
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                // Set scene baru
                stage.setScene(new Scene(root));

                // Optional: Set judul stage (window)
                stage.setTitle("Dashboard");

                // Tampilkan
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                // Bisa tampilkan alert error juga jika perlu
            }
        } else {
            // Login gagal, tampilkan alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Gagal");
            alert.setHeaderText(null);
            alert.setContentText("Username atau password salah.");
            alert.showAndWait();
        }
    }

    @FXML
    private void openRegister(ActionEvent event) {
        // Contoh buka register-view.fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Bisa tambahkan error handling
        }
    }

    @FXML
    private void openReset(ActionEvent event) {
        // Contoh buka reset-password-view.fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/reset-password-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Bisa tambahkan error handling
        }
    }

}
