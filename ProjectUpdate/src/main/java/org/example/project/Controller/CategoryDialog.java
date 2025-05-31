package org.example.project.Controller;

import javafx.scene.layout.VBox;
import org.example.project.Model.Category;
import org.example.project.Util.Storage;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import java.util.List;

public class CategoryDialog extends Dialog<List<Category>> {

    public CategoryDialog(String user, List<Category> cats) {
        setTitle("Kelola Kategori");
        ListView<Category> lv = new ListView<>(FXCollections.observableArrayList(cats));
        Button add = new Button("Tambah");
        Button del = new Button("Hapus");
        HBox hb = new HBox(10, add, del);
        VBox vb = new VBox(10, lv, hb);
        getDialogPane().setContent(vb);
        ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);
        add.setOnAction(e -> {
            TextInputDialog d = new TextInputDialog();
            d.setHeaderText("Nama kategori");
            d.showAndWait().ifPresent(n -> lv.getItems().add(new Category(n)));
        });
        del.setOnAction(e -> {
            Category sel = lv.getSelectionModel().getSelectedItem();
            if (sel!=null) lv.getItems().remove(sel);
        });
        setResultConverter(b -> b==ok?lv.getItems():null);
    }
}
