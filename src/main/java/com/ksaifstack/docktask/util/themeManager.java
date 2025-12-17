package com.ksaifstack.docktask.util;

import javafx.scene.Scene;
import java.util.ArrayList;
import java.util.List;

// Ensures that dark mode and light mode themes are applied in real time.
public class themeManager {
    private static boolean darkMode = false;
    private static Scene mainScene;
    private static final List<Runnable> listeners = new ArrayList<>();

    public static void setScene(Scene scene) {
        mainScene = scene;
    }

    public static void changeTheme() {
        if (mainScene == null) return;

        // flip first
        darkMode = !darkMode;

        // now apply matching stylesheet
        mainScene.getStylesheets().clear();
        if (darkMode) {
            mainScene.getStylesheets().add(
                    themeManager.class.getResource("/css/DarkTheme.css").toExternalForm()
            );
        } else {
            mainScene.getStylesheets().add(
                    themeManager.class.getResource("/css/LightTheme.css").toExternalForm()
            );
        }

        notifyListeners();
    }

    public static boolean isDarkMode() {
        return darkMode;
    }

    public static void addThemeChangeListener(Runnable listener) {
        listeners.add(listener);
    }

    private static void notifyListeners() {
        for (Runnable r : listeners) r.run();
    }
}
