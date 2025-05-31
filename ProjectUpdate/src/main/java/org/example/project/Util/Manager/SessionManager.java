package org.example.project.Util.Manager;

import org.example.project.Model.SessionData;

import java.io.*;

public class SessionManager {
    private static final String SESSION_FILE = "session.ser";
    public static SessionData currentSession;

    /**
     * Saves the user session to a .ser file
     *
     * @param userId The ID of the user to save in the session
     */
    public static void saveSession(int userId) {
        if (currentSession == null) {
            getActiveUserId(); // Load session if not loaded
        }

        if (currentSession == null) {
            currentSession = new SessionData(userId);
        } else {
            currentSession.setUserId(userId);
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SESSION_FILE))) {
            oos.writeObject(currentSession);
            System.out.println("Session saved successfully: " + currentSession);
        } catch (IOException e) {
            System.err.println("Failed to save session");
            e.printStackTrace();
        }
    }


    /**
     * Gets the user ID from the current session
     *
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
                // Validasi nilai setelah deserialisasi
                if (currentSession.getFontSize() <= 0) currentSession.setFontSize(12.0);
                if (currentSession.getUiScale() <= 0) currentSession.setUiScale(1.0);
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
     *
     * @return The font size or the default if no session exists
     */
    public static double getFontSize() {
        if (currentSession == null) {
            getActiveUserId();
        }
        double fontSize = currentSession != null ? currentSession.getFontSize() : 12.0;
        System.out.println("Font size loaded: " + fontSize);
        return fontSize;
    }

    /**
     * Sets the font size in the current session and saves it
     *
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
     *
     * @return The UI scale or the default if no session exists
     */
    public static double getUiScale() {
        if (currentSession == null) {
            getActiveUserId();
        }
        double uiScale = currentSession != null ? currentSession.getUiScale() : 1.0;
        System.out.println("UI scale loaded: " + uiScale);
        return uiScale;
    }

    /**
     * Sets the UI scale in the current session and saves it
     *
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

