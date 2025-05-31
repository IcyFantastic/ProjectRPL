package org.example.project.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.project.Util.Manager.SessionManager;
import org.example.project.Util.Storage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

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
