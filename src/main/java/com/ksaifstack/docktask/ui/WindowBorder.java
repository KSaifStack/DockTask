package com.ksaifstack.docktask.ui;

import com.ksaifstack.docktask.util.themeManager;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import xss.it.nfx.NfxStage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.io.InputStream;

/*
This class allows full access to full pane size.
Using this windows allows to make use of custom buttons.
It also allows for color changes.
 */
public class WindowBorder extends NfxStage {

    private HBox titleBar;
    private Button minBtn, closeBtn;
    private Button icon;
    private Label titleLabel;
    private static BorderPane rootPane;


    private double xOffset = 0;
    private double yOffset = 0;
    private static final Image whiteIcon = new Image(WindowBorder.class.getResourceAsStream("/images/lightIcon.png"));
    private static final Image darkIcon = new Image(WindowBorder.class.getResourceAsStream("/images/darkIcon.png"));
    private static final ImageView pic = new ImageView(whiteIcon);

    public WindowBorder(String title, Region content, double width, double height) {
        setTitle(title);

        // Root layout
        rootPane = new BorderPane();
        rootPane.setCenter(content);

        // Title bar
        titleBar = new HBox();
        titleBar.setPadding(new Insets(0, 10, 0, 10));
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.setSpacing(10);

        titleLabel = new Label(title);
        titleLabel.setAlignment(Pos.CENTER);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);

        //Icon
        icon = createButton("");
        if(themeManager.isDarkMode()){
            pic.setImage(darkIcon);
        }
        else if(!themeManager.isDarkMode()){
            pic.setImage(whiteIcon);
        }
        themeManager.addThemeChangeListener(() -> {
            if(themeManager.isDarkMode()){
                pic.setImage(darkIcon);
            }
            else if(!themeManager.isDarkMode()){
                pic.setImage(whiteIcon);
            }
        });


        pic.setFitHeight(32);
        pic.setFitWidth(32);
        icon.setPadding(Insets.EMPTY);
        icon.setTooltip(new Tooltip("DockTask"));
        icon.setGraphic(pic);


        minBtn = createButton("—");
        closeBtn = createButton("✕");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        titleBar.getChildren().addAll(icon,spacer, minBtn, closeBtn);
        rootPane.setTop(titleBar);

        // Drag support
        makeDraggable(titleBar);
        setResizable(false);


        // NFX native hit test support
        addClientAreas(titleBar);
        setMinControl(minBtn);   // PROTECTED: works because we are inside subclass
        setCloseControl(closeBtn);
        InputStream logoStream = getClass().getResourceAsStream("/images/logo.png");
        getIcons().add(new Image(logoStream));

        // Scene
        Scene scene = new Scene(rootPane, width, height);
        scene.setFill(null); // rounded corners support
        scene.getStylesheets().add(getClass().getResource("/css/LightTheme.css").toExternalForm());
        themeManager.setScene(scene);
        setScene(scene);

    }


    public void setTitleLabel(String titleLabel) {
        this.titleLabel.setText(titleLabel);
    }
    public void colorChange(String cssColor){
        rootPane.setStyle(cssColor);
    }

    private Button createButton(String text) {
        Button btn = new Button(text);
        btn.setId("noBorderBtn");
        btn.setFocusTraversable(false);


        return btn;
    }


    private void makeDraggable(HBox bar) {
        bar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        bar.setOnMouseDragged(event -> {
            setX(event.getScreenX() - xOffset);
            setY(event.getScreenY() - yOffset);
        });
    }


    @Override
    protected double getTitleBarHeight() {
        return 40;
    }

    public void setContent(Region newContent) {
        //String colors = null;
        rootPane.setCenter(newContent);

    }
    public static void logOut(Scene scene){
        scene.getStylesheets().add(WindowBorder.class.getResource("/css/LightTheme.css").toExternalForm());
    }



}
