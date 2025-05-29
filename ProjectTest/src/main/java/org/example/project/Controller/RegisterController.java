package org.example.project.Controller;

import org.example.project.Model.User;
import org.example.project.Util.Storage;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;
import java.util.Objects;
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
        String u = usernameField.getText();
        String p = passwordField.getText();
        String c = confirmField.getText();

        if (!Pattern.matches("^[a-zA-Z0-9]{4,25}$", u)) {
            show("Username harus 4-25 alfanumerik");
            return;
        }

        if (!p.equals(c) || !Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{4,}$", p)) {
            show("Password tidak memenuhi kriteria (min 4 char, ada huruf besar, kecil, angka, simbol)");
            return;
        }

        List<User> users = Storage.getUsers();
        for (User user : users) {
            if (user.getUsername().equals(u)) {
                show("Username sudah terdaftar");
                return;
            }
        }

        Storage.addUser(new User(u, p));

        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setTitle("Registrasi Berhasil");
        success.setHeaderText(null);
        success.setContentText("Akun berhasil dibuat! Silakan login.");
        success.showAndWait();

        Stage s = (Stage) usernameField.getScene().getWindow();
        s.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/login-view.fxml")))));
        s.setTitle("Login");
    }

    @FXML
    private void backToLogin() throws Exception {
        showAlert();
        // TODO: Implementasi scene login-view.fxml
        Stage s = (Stage) usernameField.getScene().getWindow();
        s.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/login-view.fxml")))));
        s.setTitle("Login");
    }

    private void show(String m) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error");
        a.setHeaderText(null);
        a.setContentText(m);
        a.showAndWait();
    }

    private void showAlert() {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Create Account");
        a.setHeaderText(null);
        a.setContentText("Navigasi ke halaman login...");
        a.showAndWait();
    }
}
