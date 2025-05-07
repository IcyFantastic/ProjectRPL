module org.example.project {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.google.gson;

    opens org.example.project to javafx.fxml;
    exports org.example.project;
    exports org.example.project.Controller;
    opens org.example.project.Controller to javafx.fxml;
    exports org.example.project.Model;
    opens org.example.project.Model to javafx.fxml;
}