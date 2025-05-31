package org.example.project.Controller;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
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
        // Set initial opacity to 0
        formPane.setOpacity(0);
        brandingPane.setOpacity(0);

        // Modern fade-in for branding pane with slight scale
        FadeTransition brandingFade = new FadeTransition(Duration.millis(800), brandingPane);
        brandingFade.setFromValue(0);
        brandingFade.setToValue(1);

        // Scale transition for branding pane (subtle zoom in)
        ScaleTransition brandingScale = new ScaleTransition(Duration.millis(1000), brandingPane);
        brandingScale.setFromX(0.95);
        brandingScale.setFromY(0.95);
        brandingScale.setToX(1);
        brandingScale.setToY(1);

        // Play branding animations
        ParallelTransition brandingAnimation = new ParallelTransition(brandingFade, brandingScale);
        brandingAnimation.play();

        // Delayed animation for form pane
        PauseTransition pause = new PauseTransition(Duration.millis(300));
        pause.setOnFinished(e -> {
            // Subtle fade in for form
            FadeTransition formFade = new FadeTransition(Duration.millis(800), formPane);
            formFade.setFromValue(0);
            formFade.setToValue(1);

            // Subtle slide up with easing
            TranslateTransition formSlide = new TranslateTransition(Duration.millis(800), formPane);
            formSlide.setFromY(20);
            formSlide.setToY(0);
            formSlide.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

            // Play form animations together
            ParallelTransition formAnimation = new ParallelTransition(formFade, formSlide);
            formAnimation.play();
        });
        pause.play();
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        Integer userId = Storage.validateUser(username, password);
        if (userId != null) {
            // Validasi login user, lalu ...
            SessionManager.saveSession(userId);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard-view.fxml"));
                Parent root = loader.load();

                // 👇 Ini bagian No 2 — TARUH DI SINI
                double fontSize = SessionManager.getFontSize();
                double uiScale = SessionManager.getUiScale();

                root.setStyle("-fx-font-size: " + fontSize + "px;");
                root.setScaleX(uiScale);
                root.setScaleY(uiScale);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);

                // Load dashboard CSS
                URL cssURL = getClass().getResource("/css/dashboard.css");
                if (cssURL != null) {
                    scene.getStylesheets().add(cssURL.toExternalForm());
                } else {
                    System.err.println("File dashboard.css tidak ditemukan.");
                }

                stage.setScene(scene);
                stage.setTitle("Dashboard");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
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
