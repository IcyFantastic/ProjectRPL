package org.example.project.Model;

import java.io.Serializable;

/**
 * Class to store session data that will be serialized to a .ser file
 */
public class SessionData implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer userId;
    private double fontSize = -1; // Tidak mengubah jika -1
    private double uiScale = -1;  // Tidak mengubah jika -1
    
    public SessionData(Integer userId) {
        this.userId = userId;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public double getFontSize() {
        return fontSize > 0 ? fontSize : 12.0; // Default 12.0 jika fontSize invalid
    }
    
    public void setFontSize(double fontSize) {
        this.fontSize = fontSize;
    }

    public double getUiScale() {
        return uiScale > 0 ? uiScale : 1.0; // Default 1.0 jika uiScale invalid
    }
    
    public void setUiScale(double uiScale) {
        this.uiScale = uiScale;
    }
}