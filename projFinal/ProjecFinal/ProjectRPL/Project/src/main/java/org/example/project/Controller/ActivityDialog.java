package org.example.project.Controller;

import org.example.project.Model.Activity;
import org.example.project.Model.Category;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.example.project.Util.Storage;

import java.time.LocalDate;

public class ActivityDialog extends Dialog<Activity> {
    public ActivityDialog(String user, Activity act) throws Exception {
        // Set judul dialog berdasarkan operasi (Tambah/Edit)
        setTitle(act == null ? "Tambah Aktivitas" : "Edit Aktivitas");

        // Komponen input untuk form
        Label t1 = new Label("Judul:");
        TextField tf1 = new TextField(act == null ? "" : act.getTitle());

        Label t2 = new Label("Tanggal:");
        DatePicker dp = new DatePicker(act == null ? LocalDate.now() : act.getDate());

        Label t3 = new Label("Kategori:");
        ComboBox<Category> cb = new ComboBox<>(FXCollections.observableArrayList(Storage.loadCategories(user)));
        if (act != null) cb.setValue(act.getCategory());

        Label t4 = new Label("Prioritas:");
        ComboBox<String> cp = new ComboBox<>(FXCollections.observableArrayList("Tinggi", "Sedang", "Rendah"));
        if (act != null) cp.setValue(act.getPriority());

        Label t5 = new Label("Deskripsi:");
        TextArea ta = new TextArea(act == null ? "" : act.getDescription());

        // GridPane untuk tata letak form
        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);
        gp.addRow(0, t1, tf1);
        gp.addRow(1, t2, dp);
        gp.addRow(2, t3, cb);
        gp.addRow(3, t4, cp);
        gp.addRow(4, t5, ta);

        getDialogPane().setContent(gp);

        // Tombol OK dan Cancel
        ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);

        // Hasil dialog
        setResultConverter(button -> {
            if (button == ok) {
                return new Activity(
                        tf1.getText(),
                        dp.getValue(),
                        cb.getValue(),
                        cp.getValue(),
                        false, // Aktivitas baru default belum selesai
                        user // Mengaitkan aktivitas dengan pengguna
                ).setDescription(ta.getText()); // Tambahkan deskripsi jika ada
            }
            return null;
        });
    }
}
