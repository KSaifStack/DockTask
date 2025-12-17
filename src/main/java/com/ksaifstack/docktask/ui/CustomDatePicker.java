package com.ksaifstack.docktask.ui;//Ui for DatePicker class/actions
//Based off Javafx calendar
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CustomDatePicker extends Pane {
    private final DatePicker datePicker;
    private final ComboBox<Integer> hourBox;
    private final ComboBox<Integer> minuteBox;
    private final ComboBox<String> amPmBox;

    public CustomDatePicker() {
        setPrefSize(450, 100);

        datePicker = new DatePicker();
        hourBox = new ComboBox<>();
        minuteBox = new ComboBox<>();
        amPmBox = new ComboBox<>();

        for (int i = 1; i <= 12; i++) hourBox.getItems().add(i);
        for (int i = 0; i < 60; i += 5) minuteBox.getItems().add(i);
        amPmBox.getItems().addAll("AM", "PM");

        hourBox.setValue(12);
        minuteBox.setValue(0);
        amPmBox.setValue("AM");

        Font font = Font.font("Arial", 14);
        datePicker.setStyle(
                "-fx-text-fill: #1b1b1b;" +
                "-fx-border-color: #626262;" +
                "-fx-border-radius: 4px;" +
                "-fx-background-radius: 4px;" +
                "-fx-border-width: 1px;");

        datePicker.setOnMouseEntered(e -> datePicker.setStyle(
                "-fx-background-color: #d3d3d3; -fx-text-fill: #1b1b1b; -fx-border-color: #626262; -fx-border-radius: 4px; -fx-background-radius: 4px; -fx-border-width: 1px;"
        ));
        datePicker.setOnMouseExited(e -> datePicker.setStyle(
                "-fx-text-fill: #1b1b1b; -fx-border-color: #626262; -fx-border-radius: 4px; -fx-background-radius: 4px; -fx-border-width: 1px;"
        ));

        hourBox.setStyle(
                "-fx-text-fill: #1b1b1b;" +
                "-fx-border-color: #626262;" +
                "-fx-border-radius: 4px;" +
                "-fx-background-radius: 4px;" +
                "-fx-border-width: 1px;");

        hourBox.setOnMouseExited(e -> hourBox.setStyle(
                " -fx-text-fill: #1b1b1b; -fx-border-color: #626262; -fx-border-radius: 4px; -fx-background-radius: 4px; -fx-border-width: 1px;"
        ));

        minuteBox.setStyle(
                "-fx-text-fill: #1b1b1b;" +
                "-fx-border-color: #626262;" +
                "-fx-border-radius: 4px;" +
                "-fx-background-radius: 4px;" +
                "-fx-border-width: 1px;");

        minuteBox.setOnMouseExited(e -> minuteBox.setStyle(
                " -fx-text-fill: #1b1b1b; -fx-border-color: #626262; -fx-border-radius: 4px; -fx-background-radius: 4px; -fx-border-width: 1px;"
        ));

        amPmBox.setStyle(
                "-fx-text-fill: #1b1b1b;" +
                "-fx-border-color: #626262;" +
                "-fx-border-radius: 4px;" +
                "-fx-background-radius: 4px;" +
                "-fx-border-width: 1px;");
        amPmBox.setOnMouseEntered(e -> amPmBox.setStyle(
                " -fx-text-fill: #1b1b1b; -fx-border-color: #626262; -fx-border-radius: 4px; -fx-background-radius: 4px; -fx-border-width: 1px;"
        ));
        amPmBox.setOnMouseExited(e -> amPmBox.setStyle(
                " -fx-text-fill: #1b1b1b; -fx-border-color: #626262; -fx-border-radius: 4px; -fx-background-radius: 4px; -fx-border-width: 1px;"
        ));


        // Layout positions
        datePicker.setLayoutX(288);
        datePicker.setLayoutY(65);


        hourBox.setLayoutX(285);
        hourBox.setLayoutY(35);

        minuteBox.setLayoutX(345);
        minuteBox.setLayoutY(35);

        amPmBox.setLayoutX(405);
        amPmBox.setLayoutY(35);


        datePicker.setOnAction(e -> updateSelectedText());
        hourBox.setOnAction(e -> updateSelectedText());
        minuteBox.setOnAction(e -> updateSelectedText());
        amPmBox.setOnAction(e -> updateSelectedText());

        getChildren().addAll(datePicker,  hourBox, minuteBox, amPmBox);
    }

    private void updateSelectedText() {
        LocalDate date = datePicker.getValue();
        Integer hour12 = hourBox.getValue();
        Integer minute = minuteBox.getValue();
        String amPm = amPmBox.getValue();

        if (date != null && hour12 != null && minute != null && amPm != null) {
            int hour24 = convertTo24Hour(hour12, amPm);
            LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.of(hour24, minute));

        }
    }

    private int convertTo24Hour(int hour12, String amPm) {
        if (amPm.equalsIgnoreCase("AM")) {
            return hour12 == 12 ? 0 : hour12;
        } else { // PM
            return hour12 == 12 ? 12 : hour12 + 12;
        }
    }

    private int convertTo12Hour(int hour24) {
        int hour12 = hour24 % 12;
        return hour12 == 0 ? 12 : hour12;
    }

    public LocalDateTime getDateTime() {
        LocalDate date = datePicker.getValue();
        Integer hour12 = hourBox.getValue();
        Integer minute = minuteBox.getValue();
        String amPm = amPmBox.getValue();

        if (date != null && hour12 != null && minute != null && amPm != null) {
            int hour24 = convertTo24Hour(hour12, amPm);
            return LocalDateTime.of(date, LocalTime.of(hour24, minute));
        }
        return null;
    }

    public void setDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return;
        datePicker.setValue(dateTime.toLocalDate());
        int hour24 = dateTime.getHour();
        String amPm = hour24 < 12 ? "AM" : "PM";
        int hour12 = convertTo12Hour(hour24);
        hourBox.setValue(hour12);
        minuteBox.setValue(dateTime.getMinute());
        amPmBox.setValue(amPm);
        updateSelectedText();
    }
    public class DateTimeUtils {
        public static String formatDateTime(LocalDateTime dateTime) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd hh mm a");
            return dateTime.format(formatter);
        }
    }

}


