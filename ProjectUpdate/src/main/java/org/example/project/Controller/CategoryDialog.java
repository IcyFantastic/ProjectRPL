package org.example.project.Controller;

import javafx.scene.layout.VBox;
import org.example.project.Model.Category;
import org.example.project.Util.Storage;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import java.util.List;

public class CategoryDialog extends Dialog<List<Category>> {

    private final String user;

    public CategoryDialog(String user, List<Category> cats) {
        this.user = user;
        setTitle("Kelola Kategori");

        // ListView menampilkan kategori milik user
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
            d.showAndWait().ifPresent(name -> {
                if (!name.trim().isEmpty()) {
                    // Buat category baru dengan user yang sesuai
                    Category newCat = new Category(name.trim(), this.user);

                    // Simpan ke database lewat Storage
                    if (Storage.addCategory(newCat)) {
                        lv.getItems().add(newCat);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Gagal menambahkan kategori ke database");
                        alert.showAndWait();
                    }
                }
            });
        });

        del.setOnAction(e -> {
            Category sel = lv.getSelectionModel().getSelectedItem();
            if (sel != null) {
                // Hapus dari database lewat Storage
                if (Storage.deleteCategory(sel)) {
                    lv.getItems().remove(sel);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Gagal menghapus kategori dari database");
                    alert.showAndWait();
                }
            }
        });

        setResultConverter(button -> {
            if (button == ok) {
                // Kalau perlu, bisa lakukan update list kategori di database di sini
                return lv.getItems();
            }
            return null;
        });
    }
}
