package org.example.project.Model;
import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private String petName;

    public User() {

    }
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String petName) {
        this.username = username;
        this.password = password;
        this.petName = petName;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }
}
