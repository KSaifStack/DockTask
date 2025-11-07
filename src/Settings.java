//UI for setting features
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import java.util.List;
import java.util.ArrayList;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Settings {
    private String username = null;
    //Runnable
    public Settings(String username){
        this.username=username;
    }
    public Pane getContent(Font lexend14,Font lexend32){
        Pane pane = new Pane();
        pane.setPrefSize(980, 493);
        pane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.15);");
        //--
        Region background = new Region();
        background.setLayoutX(165);
        background.setLayoutY(10);
        background.setPrefSize(605, 372);
        background.getStyleClass().add("region");

        // full height to avoid bottom gap
        pane.getChildren().add(background);

        Button settingsLabel = new Button("Settings");
        settingsLabel.setPrefSize(192,59);
        settingsLabel.setFont(lexend32);
        settingsLabel.setLayoutX(369);
        settingsLabel.setLayoutY(17);
        settingsLabel.setId("noHoverBtn");

        pane.getChildren().add(settingsLabel);

        //--
        Button back = new Button("Back");
        back.setPrefSize(73,60);
        back.setFont(lexend14);
        back.setLayoutX(172);
        back.setLayoutY(317);


        back.setOnAction(e->{
            pane.setVisible(false);
        });

        pane.getChildren().add(back);
        //--
        Button darkswitch =new Button("Dark Mode");
        darkswitch.setPrefSize(164,68);
        darkswitch.setFont(lexend14);
        darkswitch.setLayoutX(195);
        darkswitch.setLayoutY(100);
        darkswitch.setOnAction(e->{
            if(themeManager.isDarkMode()==false){
                themeManager.changeTheme();
                darkswitch.setText("Dark Mode");
            } else{
                themeManager.changeTheme();
                darkswitch.setText("Light Mode");
            }
        });


        pane.getChildren().add(darkswitch);
        //--
        Button version = new Button("0.2v");
        version.setPrefSize(50,29);
        version.setFont(lexend14);
        version.setLayoutX(710);
        version.setLayoutY(347);
        version.setId("noHoverBtn");
        pane.getChildren().add(version);

        return pane;
    }


}
