package org.example.project.Controller;

import org.example.project.Util.Storage;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.regex.Pattern;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmField;
    @FXML private VBox formVBox;

    @FXML
    private void initialize() {
        playFormEntryAnimation();
    }

    private void playFormEntryAnimation() {
        formVBox.setTranslateY(50); // mulai sedikit di bawah
        TranslateTransition transition = new TranslateTransition(Duration.millis(500), formVBox);
        transition.setFromY(50);
        transition.setToY(0);
        transition.setCycleCount(1);
        transition.setAutoReverse(false);
        transition.play();
    }

    @FXML
    private void handleRegister() throws Exception {
        String u = usernameField.getText().trim();
        String p = passwordField.getText();
        String c = confirmField.getText();

        if (!Pattern.matches("^[a-zA-Z0-9]{4,25}$", u)) {
            show("Username harus 4-25 karakter alfanumerik.");
            return;
        }

        if (!p.equals(c) || !Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{4,}$", p)) {
            show("Password tidak memenuhi kriteria:\n- Minimal 4 karakter\n- Ada huruf besar, kecil, angka, simbol");
            return;
        }

        if (Storage.userExists(u)) {
            show("Username sudah terdaftar.");
            return;
        }

        boolean success = Storage.addUser(u, p);
        if (!success) {
            show("Gagal menyimpan user ke database.");
            return;
        }

        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Registrasi Berhasil");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Akun berhasil dibuat! Silakan login.");
        successAlert.showAndWait();

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

    private void show(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Kesalahan");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
