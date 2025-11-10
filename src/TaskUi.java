// Main ui page
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.awt.*;
import java.io.InputStream;
import javafx.scene.input.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

public class TaskUi extends Application {
    private final StackPane rootStack = new StackPane();
    private static final String FONT_PATH = "/resources/fonts/Lexend.ttf";
    private Font lexend32 = null;
    private Font lexend14 = null;
    private Font lexend12 = null;

    private Boolean keyCheck = false;
    private String username;
    private TrayIcon trayIcon;
    private boolean darkMode = true;
    private final Map<String, String> lastNotifiedStage = new HashMap<>();
    private final CalendarUi calendarUi = new CalendarUi();
    private final AppTray tray = AppTray.getInstance();
    private ScrollPane scrollPane;

    // UI state
    private VBox taskListContainer;
    private Timeline clockTimeline;
    private Timeline globalCountdownTimeline;
    private Stage primaryStage;
    private CreateTaskUi createTaskUi;
    private Pane createOverlay;
    private final Image sunImg = new Image(getClass().getResourceAsStream("/images/sun.png"));
    private final Image moonImg = new Image(getClass().getResourceAsStream("/images/moon.png"));
    private final Image whiteSunImg = new Image(getClass().getResourceAsStream("/images/whiteSun.png"));
    private final Image whiteMoonImg = new Image(getClass().getResourceAsStream("/images/whiteMoon.png"));


    private static class TaskEntry {
        final Button backgroundButton;
        final Label dueLabel;
        final Label warningLabel;
        final String taskName;
        LocalDateTime dueTime;

        TaskEntry(Button backgroundButton, Label dueLabel, Label warningLabel, String taskName, LocalDateTime dueTime) {
            this.backgroundButton = backgroundButton;
            this.dueLabel = dueLabel;
            this.warningLabel = warningLabel;
            this.taskName = taskName;
            this.dueTime = dueTime;
        }
    }

    private final List<TaskEntry> visibleTasks = new ArrayList<>();



    public TaskUi(String username) {
        this.username = username;
    }

    public TaskUi() {}

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        try (InputStream fontStream = getClass().getResourceAsStream("/resources/fonts/Lato.ttf")) {
            if (fontStream == null) {
                System.err.println("Font resource not found!");
                lexend14 = Font.font("System", 14);
            } else {
                lexend14 = Font.loadFont(fontStream, 14);
            }
        } catch (Exception e) {
            lexend14 = Font.font("System", 14);
        }
        try {
            lexend32 = Font.loadFont(getClass().getResourceAsStream(FONT_PATH), 32);
            lexend12 = Font.loadFont(getClass().getResourceAsStream(FONT_PATH), 12);
        } catch (Exception ignored) {
            if (lexend32 == null) lexend32 = Font.font("System", 32);
            if (lexend12 == null) lexend12 = Font.font("System", 12);
        }

        Pane pane = new Pane();

        InputStream logoStream = getClass().getResourceAsStream("/images/logo.png");
        if (logoStream != null) {
            primaryStage.getIcons().add(new Image(logoStream));
        }

        primaryStage.setTitle("PlanForge - Home");
        pane.setPrefSize(766, 378);
        primaryStage.setResizable(false);

        tray.setup(primaryStage, "PlanForge", "/images/logo.png");
        trayIcon = tray.getTrayIcon();

        calendarUi.setCalendar(username);
        Pane calander = calendarUi.getPane();

        Platform.setImplicitExit(false);
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            Platform.runLater(primaryStage::hide);
        });

        rootStack.getChildren().addAll(pane);

        StackPane taskContainerWrapper = new StackPane();
        taskContainerWrapper.setLayoutX(12);
        taskContainerWrapper.setLayoutY(66);
        taskContainerWrapper.setPrefSize(208, 359);

        taskListContainer = new VBox(10);
        taskListContainer.setLayoutY(100);
        taskListContainer.setLayoutX(12.0);
        taskListContainer.setPrefWidth(220);
        taskListContainer.setPrefHeight(359);

        // Load tasks and populate the UI
        refreshTaskList();

        ScrollPane scrollPane = new ScrollPane(taskListContainer);
        scrollPane.setPrefSize(400, 380);
        scrollPane.setMaxSize(400, 380);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-border-color: #626262; -fx-border-radius: 2px; -fx-border-width: 1px;");

        String[][] tasksArr = UserData.ReturnData(username);
        if (tasksArr.length > 3) {
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        }
        scrollPane.setPannable(false);

        taskContainerWrapper.getChildren().addAll(scrollPane);
        pane.getChildren().add(taskContainerWrapper);

        // Welcome label
        String firstWelcome = username != null && username.length() > 0 ? username.substring(0, 1).toUpperCase() : "";
        String secondWelcome = username != null && username.length() > 1 ? username.substring(1) : "";
        Label Welcome = new Label("Welcome " + firstWelcome + secondWelcome + "!");
        Welcome.setLayoutX(12);
        Welcome.setLayoutY(8.1640625);
        Welcome.setPrefWidth(158);
        Welcome.setPrefHeight(33);
        Welcome.setFont(lexend32);
        Welcome.setStyle("-fx-font-size: 16px;");
        pane.getChildren().add(Welcome);

        Button lobutton = new Button("Logout");
        lobutton.setLayoutX(13.19);
        lobutton.setLayoutY(451.50);
        lobutton.setPrefWidth(67.00);
        lobutton.setPrefHeight(30.00);
        pane.getChildren().add(lobutton);
        lobutton.setOnAction(e -> {
            System.out.println("Logout Button was pressed.");
            if(themeManager.isDarkMode()) {
                themeManager.changeTheme();
            }
            LoginUi LoginUi = new LoginUi();
            LoginUi.start(primaryStage);
        });

        pane.getChildren().add(calander);

        Button SettingsIcon = new Button("Settings");
        SettingsIcon.setLayoutX(893.00);
        SettingsIcon.setLayoutY(4.46);
        SettingsIcon.setPrefWidth(76.00);
        SettingsIcon.setPrefHeight(30.00);
        pane.getChildren().add(SettingsIcon);
        SettingsIcon.setOnAction(e -> {
            System.out.println("Settings Button was pressed.");
            Settings Settings = new Settings(username);
            Pane SettingPane = Settings.getContent(lexend14, lexend32);
            pane.getChildren().add(SettingPane);
        });

        Button PlButton = new Button("Plugins");
        PlButton.setLayoutX(819.00);
        PlButton.setLayoutY(4.46);
        PlButton.setPrefWidth(70.00);
        PlButton.setPrefHeight(30.00);
        //Removed until api calls and whatnot are worked on
        //pane.getChildren().add(PlButton);
        PlButton.setOnAction(e -> {
            System.out.println("Plugin Button was pressed.");
            Plugins Plugins = new Plugins(username);
            Pane pluginPane = Plugins.getContent(lexend14, lexend32);
            pane.getChildren().add(pluginPane);
        });

        Button createtaskb = new Button("+");
        createtaskb.setFont(Font.font(60));
        createtaskb.setLayoutX(775);
        createtaskb.setLayoutY(140.00);
        createtaskb.setPrefWidth(150);
        createtaskb.setPrefHeight(150);
        pane.getChildren().add(createtaskb);

        createtaskb.setOnAction(e -> {
            if (createTaskUi == null) {
                createTaskUi = new CreateTaskUi(username, () -> {
                    refreshTaskList();
                    calendarUi.updateCal();
                    rootStack.getChildren().remove(createOverlay);
                });
                createOverlay = createTaskUi.getContent(lexend14);
            }

            if (!createTaskUi.isOpen()) {
                createOverlay.setVisible(true);
                if (!rootStack.getChildren().contains(createOverlay)) {
                    rootStack.getChildren().add(createOverlay);
                }
            }
        });


        Label Createtasktext = new Label("(Create Task)");
        Createtasktext.setFont(Font.font(14));
        Createtasktext.setLayoutX(810);
        Createtasktext.setLayoutY(300);
        pane.getChildren().add(Createtasktext);

        // Clock + sun/moon
        HBox timeLabelBox = new HBox();
        timeLabelBox.setLayoutX(255.00);
        timeLabelBox.setPrefWidth(455);
        timeLabelBox.setLayoutY(8);
        timeLabelBox.setAlignment(Pos.CENTER);
        Label timeLabel = new Label(LocalDateTime.now().format(DateTimeFormatter.ofPattern("M/d/yyyy h:mm:ss a")));
        LocalDateTime current = LocalDateTime.now();
        int currentHour = current.getHour();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm:ss a");



        ImageView sun = new ImageView();
        ImageView moon = new ImageView();
        ImageView iconView = new ImageView();
        iconView.setPreserveRatio(false);

        if (themeManager.isDarkMode()) {
            sun.setImage(whiteSunImg);
            moon.setImage(whiteMoonImg);
        } else {
            sun.setImage(sunImg);
            moon.setImage(moonImg);
        }

        themeManager.addThemeChangeListener(() -> {
            if (themeManager.isDarkMode()) {
                sun.setImage(whiteSunImg);
                moon.setImage(whiteMoonImg);
            } else {
                sun.setImage(sunImg);
                moon.setImage(moonImg);
            }
            int hour = LocalDateTime.now().getHour();
            if(hour >= 18 || hour < 6){
                iconView.setImage(moon.getImage());
                iconView.setFitWidth(55);
                iconView.setFitHeight(55);
                iconView.setTranslateY(-2);
            } else {
                iconView.setImage(sun.getImage());
                iconView.setFitWidth(65);
                iconView.setFitHeight(65);
                iconView.setTranslateY(-4);
            }

        });

        // sun and moon sizes revamped
        sun.setFitWidth(65);
        sun.setFitHeight(65);
        sun.setPreserveRatio(false);
        moon.setFitWidth(55);
        moon.setFitHeight(55);
        moon.setPreserveRatio(false);
        HBox.setMargin(sun, new Insets(-9, 0, 0, 0));
        HBox.setMargin(moon, new Insets(-7, 0, 0, 0));
        timeLabel.setPrefWidth(351);
        timeLabel.setPrefHeight(45);
        timeLabel.setStyle("-fx-font-size: 34px;");
        timeLabel.setFont(lexend32);
        timeLabelBox.getChildren().add(timeLabel);



        if(currentHour>=18){
            timeLabelBox.setSpacing(2);
            iconView.setImage(moon.getImage());
            iconView.setTranslateY(-4);
            iconView.setFitWidth(55);
            iconView.setFitHeight(55);
            timeLabelBox.setTranslateY(0);
        } else {
            iconView.setImage(sun.getImage());
            iconView.setTranslateY(-4);
            iconView.setFitWidth(65);
            iconView.setFitHeight(65);
            timeLabelBox.setTranslateY(-5);
        }
        timeLabelBox.getChildren().add(iconView);
        clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e ->{
            String[][] tasks = UserData.ReturnData(username);
            if(scrollPane!=null) {
                if (tasks.length > 3) {
                    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                }
                else{
                    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                }
            }
            timeLabel.setText(LocalDateTime.now().format(formatter));
            int x =timeLabel.getText().length();
            int hour= LocalDateTime.now().getHour();
            if(timeLabel.getText().length()>21){
                System.out.println(x);
                timeLabel.setPrefWidth(380);
            }
            else{
                timeLabel.setPrefWidth(351);
            }
            if(hour >= 18 || hour < 6){
                iconView.setImage(moon.getImage());
                iconView.setFitWidth(55);
                iconView.setFitHeight(55);
                iconView.setTranslateY(-4);
                timeLabelBox.setTranslateY(0);
            } else {
                iconView.setImage(sun.getImage());
                iconView.setFitWidth(65);
                iconView.setFitHeight(65);
                iconView.setTranslateY(-4);
                timeLabelBox.setTranslateY(-5);
            }


        }));
        clockTimeline.setCycleCount(Timeline.INDEFINITE);
        clockTimeline.play();

        pane.getChildren().add(timeLabelBox);






        pane.setPrefSize(980, 493);
        Scene scene = new Scene(rootStack, 980, 493);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.isControlDown() && ev.isShiftDown() && ev.getCode() == KeyCode.C) {
                if (createTaskUi == null || !createTaskUi.isOpen()) {
                    createtaskb.fire();
                }
                ev.consume();
            }
        });

        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("LightTheme.css").toExternalForm());
        themeManager.setScene(scene);
        primaryStage.setScene(scene);
        primaryStage.show();

        startGlobalCountdown();

        primaryStage.iconifiedProperty().addListener((obs, wasIcon, isIcon) -> {
            if (globalCountdownTimeline != null) {
                if (isIcon) globalCountdownTimeline.pause();
                else globalCountdownTimeline.play();
            }
            if (clockTimeline != null) {
                if (isIcon) clockTimeline.pause();
                else clockTimeline.play();
            }
        });

    }


    private void refreshTaskList() {
        // Clear UI and data
        if (taskListContainer != null) {
            taskListContainer.getChildren().clear();
        } else {
            taskListContainer = new VBox(10);
        }
        visibleTasks.clear();

        String[][] tasks = UserData.ReturnData(username);
        if (tasks == null || tasks.length == 0) {
            Label nuller = new Label("No Tasks found...");
            nuller.setFont(Font.font(14));
            nuller.setTranslateX(50);
            nuller.setPrefSize(208, 50);
            taskListContainer.getChildren().add(nuller);
            return;
        }

        // Keep original sorting behavior (group then priority)
        Arrays.sort(tasks, (a, b) -> {
            int groupCompare = b[3].compareToIgnoreCase(a[3]);
            if (groupCompare != 0) return groupCompare;
            return groupCompare;
        });
        Arrays.sort(tasks, (a, b) -> {
            int pa = Integer.parseInt(a[2]);
            int pb = Integer.parseInt(b[2]);
            return Integer.compare(pb, pa);
        });

        for (String[] task : tasks) {
            Button pane = createTaskPane(task);
            taskListContainer.getChildren().add(pane);
        }

    }

    private Button createTaskPane(String[] task) {
        Label group = new Label(task[3]);
        group.setTranslateX(-10);
        Label duetime = new Label("----");
        Label warning = new Label("----");
        Label name = new Label(task[0]);

        LocalDateTime timec = UserData.DataCheckerUI(task[0]);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox topRow = new HBox(group, spacer, duetime);
        topRow.setPrefWidth(184);
        topRow.setAlignment(Pos.CENTER_LEFT);
        topRow.setTranslateY(-10);

        warning.setTranslateY(10);
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        HBox bottomRow = new HBox(spacer2, warning);
        bottomRow.setAlignment(Pos.CENTER_RIGHT);

        VBox content = new VBox(4, topRow, name, bottomRow);
        content.setPrefSize(184, 69);
        content.setPadding(new Insets(8));
        content.setAlignment(Pos.TOP_LEFT);

        Button background = new Button();
        background.setPrefSize(184, 69);

        String bgColor = GroupGiver(task[3]);
        if (timec.isBefore(LocalDateTime.now())) {
            bgColor = "#FF4C4C";
        }
        background.setStyle("-fx-border-color:" + bgColor + "; ");

        String[][] dtasks = UserData.ReturnData(username);
        if (dtasks.length > 3) {
            background.setTranslateX(3);
        } else {
            background.setTranslateX(10);
            background.setTranslateY(5);
        }

        background.setGraphic(content);

        background.setOnMouseClicked((MouseEvent e) -> {
            final Pane[] updateOverlay = new Pane[1];
            UpdateTaskUi updateTaskUi = new UpdateTaskUi(username, task[0], () -> {
                rootStack.getChildren().remove(updateOverlay[0]);
                refreshTaskList();
                calendarUi.updateCal();
            });
            updateOverlay[0] = updateTaskUi.getContent(lexend14);
            rootStack.getChildren().add(updateOverlay[0]);
        });

        TaskEntry entry = new TaskEntry(background, duetime, warning, task[0], timec);
        visibleTasks.add(entry);

        return background;
    }

    private void startGlobalCountdown() {
        if (globalCountdownTimeline != null) {
            globalCountdownTimeline.stop();
        }

        globalCountdownTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            LocalDateTime now = LocalDateTime.now();

            lastNotifiedStage.keySet().removeIf(key -> {
                String[] parts = key.split("_", 2);
                if (parts.length < 2) return false;
                try {
                    LocalDateTime due = LocalDateTime.parse(parts[1]);
                    String stage = lastNotifiedStage.get(key);
                    return stage != null && !stage.equals("Overdue") && due.isBefore(now.minusHours(1));
                } catch (Exception ignored) {
                    return false;
                }
            });

            for (TaskEntry entry : visibleTasks) {
                LocalDateTime due = entry.dueTime;
                long secondsUntilDue = now.until(due, ChronoUnit.SECONDS);

                if (secondsUntilDue > 0) {
                    long hours = secondsUntilDue / 3600;
                    long minutes = (secondsUntilDue % 3600) / 60;
                    long seconds = secondsUntilDue % 60;
                    entry.dueLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
                } else {
                    entry.dueLabel.setText("00:00:00");
                }

                String stage = null;
                if (secondsUntilDue <= 0) {
                    stage = "Overdue";
                    entry.warningLabel.setText("Overdue");
                } else if (secondsUntilDue <= 60) {
                    stage = "1min";
                    entry.warningLabel.setText("1 minute!");
                } else if (secondsUntilDue <= 10 * 60) {
                    stage = "10min";
                    entry.warningLabel.setText("10 minutes left!");
                } else if (secondsUntilDue <= 30 * 60) {
                    stage = "30min";
                    entry.warningLabel.setText("30 minutes left!");
                } else if (secondsUntilDue <= 60 * 60) {
                    stage = "1hr";
                    entry.warningLabel.setText("1 hour!");
                } else if (secondsUntilDue <= 5 * 60 * 60) {
                    stage = "5hr";
                    entry.warningLabel.setText("5 hours");
                } else if (secondsUntilDue <= 24 * 60 * 60) {
                    stage = "24hr";
                    entry.warningLabel.setText("24 hours");
                } else {
                    entry.warningLabel.setText("Due Soon!");
                }

                String taskKey = entry.taskName + "_" + due.toString();
                if (stage != null && !stage.equals(lastNotifiedStage.get(taskKey))) {
                    lastNotifiedStage.put(taskKey, stage);
                    showNotification("PlanForge - " + entry.taskName, buildMessage(stage, entry.taskName));
                }
            }
        }));

        globalCountdownTimeline.setCycleCount(Timeline.INDEFINITE);
        globalCountdownTimeline.play();
    }

    private String GroupGiver(String groupName) {
        String[] colors = {"#FFA500", "#228B22", "#000080"};
        int index = Math.abs(groupName.toLowerCase().hashCode()) % colors.length;
        return colors[index];
    }

    private void showNotification(String title, String message) {
        if (trayIcon != null) {
            trayIcon.displayMessage(title, message, TrayIcon.MessageType.WARNING);
        }
    }

    private String buildMessage(String stage, String taskname) {
        switch (stage) {
            case "Overdue" -> {
                return taskname + " is overdue!";
            }
            case "1min" -> {
                return taskname + " is due in 1 minute! Lock in.";
            }
            case "10min" -> {
                return taskname + " is due in 10 minutes! Lock in.";
            }
            case "30min" -> {
                return taskname + " is due in 30 minutes! Lock in.";
            }
            case "1hr" -> {
                return taskname + " is due in 1 hour! Lock in.";
            }
            case "5hr" -> {
                return taskname + " is due in 5 hours! Lock in.";
            }
            case "24hr" -> {
                return taskname + " is due in 24 hours! Lock in.";
            }
            default -> {
                return taskname + " is due soon!";
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
