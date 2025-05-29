package org.example.project.Controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.project.Model.User;
import org.example.project.Util.Storage;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class ResetPasswordController {
    @FXML private TextField usernameField; // Changed from emailField to usernameField

    @FXML
    private void handleResetPassword(ActionEvent event) throws Exception {
        String username = usernameField.getText();

        // Validasi format username (sesuai model User)
        if (!Pattern.matches("^[a-zA-Z0-9]{4,25}$", username)) {
            showAlert("Username harus 4-25 karakter alfanumerik");
            return;
        }

        // Cek apakah username ada di database
        List<User> users = Storage.getUsers();
        boolean userExists = users.stream()
                .anyMatch(user -> user.getUsername() != null &&
                        user.getUsername().equalsIgnoreCase(username));

        if (!userExists) {
            showAlert("Username tidak terdaftar dalam sistem");
            return;
        }

        // Tampilkan pesan sukses ( tanpa email)
        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setTitle("Reset Password");
        success.setHeaderText(null);
        success.setContentText("Instruksi reset password telah dikirim untuk username: " + username);
        success.showAndWait();

        // Kembali ke login
        backToLogin();
    }

    @FXML
    private void backToLogin() throws Exception {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(
                Objects.requireNonNull(getClass().getResource("/fxml/login-view.fxml")))));
        stage.setTitle("Login");
    }

    @FXML
    private VBox formVBox;

    @FXML
    private void initialize() {
        // Animasi saat form muncul
        TranslateTransition tt = new TranslateTransition(Duration.millis(500), formVBox);
        tt.setFromY(300);
        tt.setToY(0);
        tt.play();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
