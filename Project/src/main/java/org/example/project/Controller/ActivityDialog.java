package org.example.project.Controller;

import org.example.project.Model.Activity;
import org.example.project.Model.Category;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import org.example.project.Util.Storage;

import java.time.LocalDate;
import java.util.List;

public class ActivityDialog extends Dialog<Activity> {
    public ActivityDialog(String user, Activity act) throws Exception {
        setTitle(act==null?"Tambah Aktivitas":"Edit Aktivitas");
        Label t1 = new Label("Judul:");
        TextField tf1 = new TextField(act==null?"":act.getTitle());
        Label t2 = new Label("Deskripsi:");
        TextArea ta = new TextArea(act==null?"":act.getDescription());
        Label t3 = new Label("Tanggal:");
        DatePicker dp = new DatePicker(act==null?LocalDate.now():act.getDate());
        Label t4 = new Label("Kategori:");
        ComboBox<Category> cb = new ComboBox<>(FXCollections.observableArrayList(Storage.loadCategories(user)));
        if (act!=null) cb.setValue(act.getCategory());
        Label t5 = new Label("Prioritas:");
        ComboBox<String> cp = new ComboBox<>(FXCollections.observableArrayList("Tinggi","Sedang","Rendah"));
        if (act!=null) cp.setValue(act.getPriority());
        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);
        gp.addRow(0,t1,tf1);
        gp.addRow(1,t2,ta);
        gp.addRow(2,t3,dp);
        gp.addRow(3,t4,cb);
        gp.addRow(4,t5,cp);
        getDialogPane().setContent(gp);
        ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);
        setResultConverter(b -> {
            if (b==ok) {
                Activity a = new Activity(tf1.getText(), ta.getText(), dp.getValue(), cb.getValue(), cp.getValue());
                if (act!=null) a.setCompleted(act.isCompleted());
                return a;
            }
            return null;
        });
    }
}
