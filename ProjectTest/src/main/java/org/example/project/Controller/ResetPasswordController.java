package org.example.project.Controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
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
    @FXML private TextField usernameField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML
    private void handleReset() throws Exception {
        String username = usernameField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!Pattern.matches("^[a-zA-Z0-9]{4,25}$", username)) {
            show("Username tidak valid");
            return;
        }

        if (!newPassword.equals(confirmPassword) ||
                !Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{4,}$", newPassword)) {
            show("Password baru tidak memenuhi kriteria atau tidak sama");
            return;
        }

        List<User> users = Storage.getUsers();
        boolean found = false;
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                user.setPassword(newPassword);
                found = true;
                break;
            }
        }

        if (!found) {
            show("Username tidak ditemukan");
            return;
        }

        Storage.saveUsers(users);

        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setTitle("Reset Password Berhasil");
        success.setHeaderText(null);
        success.setContentText("Password berhasil diubah. Silakan login kembali.");
        success.showAndWait();

        Stage s = (Stage) usernameField.getScene().getWindow();
        s.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/login-view.fxml")))));
        s.setTitle("Login");
    }

    @FXML
    private void backToLogin() throws Exception {
        Stage s = (Stage) usernameField.getScene().getWindow();
        s.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/login-view.fxml")))));
        s.setTitle("Login");
    }

    @FXML
    private VBox formVBox;

    @FXML
    private void initialize() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(500), formVBox);
        tt.setFromY(300);
        tt.setToY(0);
        tt.play();
    }


    private void show(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleResetPassword(ActionEvent actionEvent) {
    }
}
