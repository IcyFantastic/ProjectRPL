package org.example.project.Controller;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.project.Model.User;
import org.example.project.Util.Storage;

import java.util.List;
import java.util.regex.Pattern;

public class ResetPasswordController {
    @FXML private TextField usernameField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField backupField;
    @FXML private VBox formVBox;
    @FXML private AnchorPane leftPanel;
    @FXML private AnchorPane rightPanel;

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

        if (!Storage.userExists(username)) {
            show("Username tidak ditemukan");
            return;
        }

        boolean updateSuccess = Storage.updatePassword(username, newPassword);

        if (!updateSuccess) {
            show("Gagal mengubah password");
            return;
        }

        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setTitle("Reset Password Berhasil");
        success.setHeaderText(null);
        success.setContentText("Password berhasil diubah. Silakan login kembali.");
        success.showAndWait();

        Stage s = (Stage) usernameField.getScene().getWindow();
        s.setScene(new Scene(FXMLLoader.load(getClass().getResource("/fxml/login-view.fxml"))));
        s.setTitle("Login");
    }

    @FXML
    private void backToLogin() throws Exception {
        Stage s = (Stage) usernameField.getScene().getWindow();
        s.setScene(new Scene(FXMLLoader.load(getClass().getResource("/fxml/login-view.fxml"))));
        s.setTitle("Login");
    }

    @FXML
    private void initialize() {
        // Slide Up animasi form
        TranslateTransition tt = new TranslateTransition(Duration.millis(500), formVBox);
        tt.setFromY(300);
        tt.setToY(0);
        tt.play();

        // Fade in/pulse background kiri-kanan
        FadeTransition leftFade = new FadeTransition(Duration.seconds(1.5), leftPanel);
        leftFade.setFromValue(0.9);
        leftFade.setToValue(1.0);
        leftFade.setAutoReverse(true);
        leftFade.setCycleCount(FadeTransition.INDEFINITE);
        leftFade.play();

        FadeTransition rightFade = new FadeTransition(Duration.seconds(1.5), rightPanel);
        rightFade.setFromValue(0.9);
        rightFade.setToValue(1.0);
        rightFade.setAutoReverse(true);
        rightFade.setCycleCount(FadeTransition.INDEFINITE);
        rightFade.play();
    }


    private void show(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleResetPassword(ActionEvent actionEvent) throws Exception {
        handleReset();
    }
}
