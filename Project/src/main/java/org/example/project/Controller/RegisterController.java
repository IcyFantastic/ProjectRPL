package org.example.project.Controller;

import org.example.project.Model.User;
import org.example.project.Util.Storage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.List;
import java.util.regex.Pattern;

public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmField;

    @FXML
    private void handleRegister() throws Exception {
        String u = usernameField.getText();
        String p = passwordField.getText();
        String c = confirmField.getText();

        if (!Pattern.matches("^[a-zA-Z0-9]{4,25}$", u)) {
            show("Username harus 4-25 alfanumerik"); return;
        }
        if (!p.equals(c) ||
                !Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{4,}$", p)) {
            show("Password tidak memenuhi kriteria"); return;
        }

        List<User> users = Storage.getUsers(); // Ambil dari memori
        for (User user : users) {
            if (user.getUsername().equals(u)) {
                show("Username sudah terdaftar");
                return;
            }
        }

        Storage.addUser(new User(u, p)); // Tambahkan dan simpan otomatis

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

    private void show(String m) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(m);
        a.showAndWait();
    }
}
