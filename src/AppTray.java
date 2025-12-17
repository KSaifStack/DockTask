/*
Uses javas awt imports to allow the use of SystemTray calls.
These calls make it possible to iconfiy the app upon exit.
Full Support with nfx windows will be soon complete.
Known issues: Throws Nullpointerexception upon reopening from icon.
 */
import javafx.application.Platform;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import javafx.application.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.text.*;
import java.util.*;
public class AppTray {
    private static AppTray instance;
    private TrayIcon trayIcon;
    private boolean firstHide = true;

    private AppTray() {}

    public static AppTray getInstance() {
        if (instance == null) {
            instance = new AppTray();
        }
        return instance;
    }

    public TrayIcon getTrayIcon() {
        return trayIcon;
    }

    public void setup(Stage stage, String tooltip, String iconPath) {
        if (!SystemTray.isSupported()) {
            System.out.println("System tray not supported");
            return;
        }

        SystemTray tray = SystemTray.getSystemTray();

        // Check globally
        if (trayIcon != null) {
            System.out.println("Tray icon already exists, skipping setup...");
            return;
        }

        // Load icon
        URL iconUrl = getClass().getResource(iconPath);
        if (iconUrl == null) {
            System.out.println("Tray icon image not found: " + iconPath);
            return;
        }
        Image trayImage = Toolkit.getDefaultToolkit().getImage(iconUrl);

        // Popup menu
        PopupMenu popup = new PopupMenu();

        MenuItem openItem = new MenuItem("Open");
        openItem.addActionListener(e -> Platform.runLater(() -> {
            stage.setIconified(false);
            stage.show();
            stage.toFront();
            stage.requestFocus();
        }));

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(e -> {
            tray.remove(trayIcon);
            Platform.exit();
            System.exit(0);
        });
        popup.addSeparator();
        popup.add(openItem);
        popup.addSeparator();
        popup.add(exitItem);
        popup.addSeparator();

        trayIcon = new TrayIcon(trayImage, tooltip, popup);
        trayIcon.setImageAutoSize(true);
        trayIcon.addActionListener(e -> Platform.runLater(() -> {
            try {
                if (stage.isShowing() && stage.getOpacity() > 0) {
                    hideToTray(stage);
                } else {
                    stage.setOpacity(1);
                    stage.setIconified(false);
                    stage.show();
                    stage.requestFocus();

                }
            }
            catch(Exception bad){
                System.out.println("Error while opening taskui: "+bad.getMessage());
            }
        }));

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("Unable to add tray icon: " + e.getMessage());
        }
    }

    public void hideToTray(Stage stage) {
        stage.setIconified(true);
        stage.setOpacity(0);
        if (firstHide && trayIcon != null) {
            trayIcon.displayMessage("DockTask",
                    "DockTask is still running in the background.",
                    TrayIcon.MessageType.INFO);
            firstHide = false;
        }
    }
}
