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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.example.project.Model.User;
import org.example.project.Util.Storage;

import java.io.IOException;
import java.util.List;

public class LoginController {

    @FXML
    private AnchorPane formPane;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    public void initialize() {
        Storage.initializeStorage(); // load user data

        // FX: Fade in
        FadeTransition fade = new FadeTransition(Duration.millis(700), formPane);
        fade.setFromValue(0);
        fade.setToValue(1);

        // FX: Slide up
        TranslateTransition slide = new TranslateTransition(Duration.millis(700), formPane);
        slide.setFromY(50);
        slide.setToY(0);

        fade.play();
        slide.play();
    }

    @FXML
    private void handleLogin(ActionEvent event) throws Exception {
        String u = usernameField.getText();
        String p = passwordField.getText();
        List<User> users = Storage.getUsers(); // Ambil data dari memori

        for (User user : users) {
            if (user.getUsername().equals(u) && user.getPassword().equals(p)) {
                showAlert(Alert.AlertType.INFORMATION, "Login Berhasil", "Selamat datang, " + u + "!");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard-view.fxml"));
                Stage s = (Stage) usernameField.getScene().getWindow();
                s.setScene(new Scene(loader.load()));
                s.setTitle("To-Do List - " + u);

                MainController mc = loader.getController();
                mc.setPrimaryStage(s);
                mc.initData(u);
                mc.setOnLogout(() -> Storage.saveAll());

                s.setOnCloseRequest((WindowEvent we) -> Storage.saveAll());
                return;
            }
        }

        showAlert(Alert.AlertType.ERROR, "Login Gagal", "Username atau password salah.");
    }

    @FXML
    private void openRegister(ActionEvent event) throws Exception {
        showAlert(Alert.AlertType.INFORMATION, "Create Account", "Navigasi ke halaman buat akun...");
        // TODO: Implementasi scene register-view.fxml
        Stage s = (Stage) usernameField.getScene().getWindow();
        s.setScene(new Scene(FXMLLoader.load(getClass().getResource("/fxml/register-view.fxml"))));
        s.setTitle("Register");
    }

    @FXML
    private void openReset(ActionEvent event) throws IOException {
        showAlert(Alert.AlertType.INFORMATION, "Reset Password", "Navigasi ke halaman reset password...");
        // TODO: Implementasi scene reset-password-view.fxml
        Stage s = (Stage) usernameField.getScene().getWindow();
        s.setScene(new Scene(FXMLLoader.load(getClass().getResource("/fxml/reset-password-view.fxml"))));
        s.setTitle("Reset Password");
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) formPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void goToResetPassword() throws Exception {
        Stage s = (Stage) usernameField.getScene().getWindow();
        Scene resetScene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/reset-password-view.fxml")));
        s.setScene(resetScene);
        s.setTitle("Reset Password");
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
