package org.example.project.Model;

import java.io.Serializable;

/**
 * Class to store session data that will be serialized to a .ser file
 */
public class SessionData implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer userId;
    private double fontSize = 12.0; // Default font size
    private double uiScale = 1.0;   // Default UI scale
    
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
        return fontSize;
    }
    
    public void setFontSize(double fontSize) {
        this.fontSize = fontSize;
    }
    
    public double getUiScale() {
        return uiScale;
    }
    
    public void setUiScale(double uiScale) {
        this.uiScale = uiScale;
    }
}