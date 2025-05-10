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
import javafx.stage.WindowEvent;
import java.util.List;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    public void initialize() {
        Storage.initializeStorage(); // Memuat semua data dari data.dat
    }

    @FXML
    private void handleLogin() throws Exception {
        String u = usernameField.getText();
        String p = passwordField.getText();
        List<User> users = Storage.getUsers(); // Ambil data dari memori

        for (User user : users) {
            if (user.getUsername().equals(u) && user.getPassword().equals(p)) {
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Login Berhasil");
                success.setHeaderText(null);
                success.setContentText("Selamat datang, " + u + "!");
                success.showAndWait();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main-view.fxml"));
                Stage s = (Stage) usernameField.getScene().getWindow();
                s.setScene(new Scene(loader.load()));
                s.setTitle("To-Do List - " + u);
                MainController mc = loader.getController();
                mc.initData(u);
                mc.setOnLogout(() -> Storage.saveAll());

                // Simpan saat aplikasi ditutup
                s.setOnCloseRequest((WindowEvent we) -> Storage.saveAll());
                return;
            }
        }

        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText("Username atau password salah");
        a.showAndWait();
    }

    @FXML
    private void openRegister() throws Exception {
        Stage s = (Stage) usernameField.getScene().getWindow();
        s.setScene(new Scene(FXMLLoader.load(getClass().getResource("/fxml/register-view.fxml"))));
        s.setTitle("Register");
    }
}
