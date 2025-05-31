package org.example.project.Controller;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.project.Util.Storage;

import java.net.URL;
import java.util.regex.Pattern;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmField;
    @FXML private PasswordField backupfield;
    @FXML private VBox formVBox;
    @FXML private VBox brandingPane;

    @FXML
    private void initialize() {
        playFormEntryAnimation();
    }

    private void playFormEntryAnimation() {
        // Set initial states
        formVBox.setOpacity(0);
        if (brandingPane != null) {
            brandingPane.setOpacity(0);
        }

        // Create animations for branding pane
        if (brandingPane != null) {
            // Modern fade-in with blur effect simulation
            FadeTransition brandingFade = new FadeTransition(Duration.millis(900), brandingPane);
            brandingFade.setFromValue(0);
            brandingFade.setToValue(1);

            // Subtle rotation effect for modern look
            RotateTransition brandingRotate = new RotateTransition(Duration.millis(1200), brandingPane);
            brandingRotate.setFromAngle(-2);
            brandingRotate.setToAngle(0);
            brandingRotate.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

            // Play branding animations together
            ParallelTransition brandingAnimation = new ParallelTransition(
                brandingFade, brandingRotate
            );
            brandingAnimation.play();
        }

        // Staggered animation for form elements
        PauseTransition pause = new PauseTransition(Duration.millis(250));
        pause.setOnFinished(e -> {
            // Smooth fade in
            FadeTransition formFade = new FadeTransition(Duration.millis(850), formVBox);
            formFade.setFromValue(0);
            formFade.setToValue(1);

            // Subtle slide with bounce effect
            TranslateTransition formSlide = new TranslateTransition(Duration.millis(850), formVBox);
            formSlide.setFromY(15);
            formSlide.setToY(0);
            formSlide.setInterpolator(javafx.animation.Interpolator.SPLINE(0.25, 0.1, 0.25, 1));

            // Play form animations
            ParallelTransition formAnimation = new ParallelTransition(
                formFade, formSlide
            );
            formAnimation.play();
        });
        pause.play();
    }

    @FXML
    private void handleRegister() throws Exception {
        String u = usernameField.getText().trim();
        String p = passwordField.getText();
        String c = confirmField.getText();
        String petName = backupfield.getText().trim();

        if (!Pattern.matches("^[a-zA-Z0-9]{4,25}$", u)) {
            show("Username harus 4-25 karakter alfanumerik.");
            return;
        }

        if (!p.equals(c) || !Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{4,}$", p)) {
            show("Password tidak memenuhi kriteria:\n- Minimal 4 karakter\n- Ada huruf besar, kecil, angka, simbol");
            return;
        }

        if (petName.isEmpty()) {
            show("Nama hewan peliharaan tidak boleh kosong.");
            return;
        }

        if (Storage.userExists(u)) {
            show("Username sudah terdaftar.");
            return;
        }

        boolean success = Storage.addUser(u, p, petName);
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
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login-view.fxml"));
        Scene scene = new Scene(root);

        // Load login CSS
        URL cssURL = getClass().getResource("/css/login.css");
        if (cssURL != null) {
            scene.getStylesheets().add(cssURL.toExternalForm());
        } else {
            System.err.println("File login.css tidak ditemukan.");
        }

        s.setScene(scene);
        s.setTitle("Login");
    }

    @FXML
    private void backToLogin() throws Exception {
        Stage s = (Stage) usernameField.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login-view.fxml"));
        Scene scene = new Scene(root);

        // Load login CSS
        URL cssURL = getClass().getResource("/css/login.css");
        if (cssURL != null) {
            scene.getStylesheets().add(cssURL.toExternalForm());
        } else {
            System.err.println("File login.css tidak ditemukan.");
        }

        s.setScene(scene);
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
