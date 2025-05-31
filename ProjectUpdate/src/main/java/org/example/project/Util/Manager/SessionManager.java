package org.example.project.Util.Manager;

import org.example.project.Model.SessionData;

import java.io.*;

public class SessionManager {
    private static final String SESSION_FILE = "session.ser";
    private static SessionData currentSession;

    /**
     * Saves the user session to a .ser file
     * @param userId The ID of the user to save in the session
     */
    public static void saveSession(int userId) {
        try {
            SessionData sessionData = new SessionData(userId);

            // If we already have a session loaded, preserve UI settings
            if (currentSession != null) {
                sessionData.setFontSize(currentSession.getFontSize());
                sessionData.setUiScale(currentSession.getUiScale());
            }

            currentSession = sessionData;

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SESSION_FILE))) {
                oos.writeObject(sessionData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the user ID from the current session
     * @return The user ID or null if no session exists
     */
    public static Integer getActiveUserId() {
        if (currentSession != null) {
            return currentSession.getUserId();
        }

        try {
            File sessionFile = new File(SESSION_FILE);
            if (!sessionFile.exists()) {
                return null;
            }

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(sessionFile))) {
                currentSession = (SessionData) ois.readObject();
                return currentSession.getUserId();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Clears the current session by deleting the session file
     */
    public static void clearSession() {
        File sessionFile = new File(SESSION_FILE);
        if (sessionFile.exists()) {
            sessionFile.delete();
        }
        currentSession = null;
    }

    /**
     * Gets the current font size from the session
     * @return The font size or the default if no session exists
     */
    public static double getFontSize() {
        if (currentSession == null) {
            getActiveUserId(); // Load session if not loaded
        }
        return currentSession != null ? currentSession.getFontSize() : 12.0;
    }

    /**
     * Sets the font size in the current session and saves it
     * @param fontSize The font size to save
     */
    public static void setFontSize(double fontSize) {
        if (currentSession == null) {
            getActiveUserId(); // Load session if not loaded
        }

        if (currentSession != null) {
            currentSession.setFontSize(fontSize);
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SESSION_FILE))) {
                oos.writeObject(currentSession);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets the current UI scale from the session
     * @return The UI scale or the default if no session exists
     */
    public static double getUiScale() {
        if (currentSession == null) {
            getActiveUserId(); // Load session if not loaded
        }
        return currentSession != null ? currentSession.getUiScale() : 1.0;
    }

    /**
     * Sets the UI scale in the current session and saves it
     * @param uiScale The UI scale to save
     */
    public static void setUiScale(double uiScale) {
        if (currentSession == null) {
            getActiveUserId(); // Load session if not loaded
        }

        if (currentSession != null) {
            currentSession.setUiScale(uiScale);
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SESSION_FILE))) {
                oos.writeObject(currentSession);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
