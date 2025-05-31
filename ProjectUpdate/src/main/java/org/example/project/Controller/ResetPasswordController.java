package org.example.project.Controller;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.project.Model.User;
import org.example.project.Util.Storage;

import java.net.URL;
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
        String petName = backupField.getText().trim();

        if (!Pattern.matches("^[a-zA-Z0-9]{4,25}$", username)) {
            show("Username tidak valid");
            return;
        }

        if (!newPassword.equals(confirmPassword) ||
                !Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{4,}$", newPassword)) {
            show("Password baru tidak memenuhi kriteria atau tidak sama");
            return;
        }

        if (petName.isEmpty()) {
            show("Nama hewan peliharaan tidak boleh kosong");
            return;
        }

        if (!Storage.userExists(username)) {
            show("Username tidak ditemukan");
            return;
        }

        if (!Storage.validatePetName(username, petName)) {
            show("Nama hewan peliharaan tidak sesuai");
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

    @FXML
    private void initialize() {
        // Set initial states
        formVBox.setOpacity(0);
        leftPanel.setOpacity(0);
        rightPanel.setOpacity(0);

        // Modern gradient-like effect for panels
        SequentialTransition sequence = new SequentialTransition();

        // Left panel fade in with subtle scale
        FadeTransition leftFade = new FadeTransition(Duration.millis(800), leftPanel);
        leftFade.setFromValue(0);
        leftFade.setToValue(1);

        ScaleTransition leftScale = new ScaleTransition(Duration.millis(1000), leftPanel);
        leftScale.setFromX(0.98);
        leftScale.setFromY(0.98);
        leftScale.setToX(1);
        leftScale.setToY(1);
        leftScale.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

        ParallelTransition leftAnimation = new ParallelTransition(leftFade, leftScale);

        // Right panel fade in with subtle scale (delayed)
        FadeTransition rightFade = new FadeTransition(Duration.millis(800), rightPanel);
        rightFade.setFromValue(0);
        rightFade.setToValue(1);

        ScaleTransition rightScale = new ScaleTransition(Duration.millis(1000), rightPanel);
        rightScale.setFromX(0.98);
        rightScale.setFromY(0.98);
        rightScale.setToX(1);
        rightScale.setToY(1);
        rightScale.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

        ParallelTransition rightAnimation = new ParallelTransition(rightFade, rightScale);

        // Form animation (most delayed)
        PauseTransition formDelay = new PauseTransition(Duration.millis(200));

        FadeTransition formFade = new FadeTransition(Duration.millis(900), formVBox);
        formFade.setFromValue(0);
        formFade.setToValue(1);

        TranslateTransition formSlide = new TranslateTransition(Duration.millis(900), formVBox);
        formSlide.setFromY(25);
        formSlide.setToY(0);
        formSlide.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

        ParallelTransition formAnimation = new ParallelTransition(formFade, formSlide);

        // Sequence all animations with slight delays
        sequence.getChildren().addAll(
            leftAnimation,
            new PauseTransition(Duration.millis(150)),
            rightAnimation,
            formDelay,
            formAnimation
        );

        sequence.play();

        // Add subtle shadow effect after animation completes
        sequence.setOnFinished(e -> {
            leftPanel.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.2)));
            rightPanel.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.2)));
        });
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
