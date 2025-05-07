package org.example.project;

import org.example.project.Util.Storage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Storage.init();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/login_view.fxml"));
        Scene scene = new Scene(loader.load());
//        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        primaryStage.setTitle("To-Do List");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}