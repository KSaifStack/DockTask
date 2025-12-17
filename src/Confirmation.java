import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

/**
 *This class will open panes to help the user pick actions within the ui.
 *
 **/
public class Confirmation {


    /**
     * {@code check}
     *  This functions allows the user to check if they agree to the selected action or not.
     * @return Pane
     */
    public Pane check(String question, String note, Font font, Runnable onConfirm) {

        Pane pane = new Pane();
        pane.setPrefSize(980, 493);
        pane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.15);");

        // Background dialog box
        Pane background = new Pane();
        background.setLayoutX(325);
        background.setLayoutY(140);
        background.setPrefSize(310, 176);
        background.getStyleClass().add("region");
        pane.getChildren().add(background);

        // Question label
        Label questionLabel = new Label("Are you sure you would like to " + question + "?");
        questionLabel.setAlignment(Pos.CENTER);
        questionLabel.setFont(font);
        questionLabel.setPrefSize(425, 95);
        questionLabel.setLayoutX(265);
        questionLabel.setLayoutY(115);
        pane.getChildren().add(questionLabel);

        // Optional note/context
        Label noteLabel = new Label("'" + note + "'");
        noteLabel.setAlignment(Pos.BOTTOM_CENTER);
        noteLabel.setLayoutX(10);
        noteLabel.setLayoutY(35);
        noteLabel.setFont(font);
        noteLabel.setVisible(note != null && !note.isEmpty());
        noteLabel.setWrapText(true);
        noteLabel.setMaxWidth(300);
        background.getChildren().add(noteLabel);

        // Yes button
        Button yesButton = new Button("Yes");
        yesButton.setLayoutX(350);
        yesButton.setLayoutY(220);
        yesButton.setFont(font);
        yesButton.setPrefSize(99, 68);
        yesButton.setOnAction(e -> {
            pane.setVisible(false);
            if (onConfirm != null) {
                onConfirm.run();
            }
        });

        // No button
        Button noButton = new Button("No");
        noButton.setLayoutX(510);
        noButton.setLayoutY(220);
        noButton.setFont(font);
        noButton.setPrefSize(99, 68);
        noButton.setOnAction(e -> pane.setVisible(false));

        pane.getChildren().addAll(yesButton, noButton);

        return pane;
    }
}