package visuals.stats;

import controller.ScoreController;
import controller.UserController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import model.ModeType;
import model.Score;
import visuals.Navigaattori;
import visuals.audio.AudioMemory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static model.ModeType.LEADERBOARD;

public class LeaderboardsController {

    @FXML
    public Button buttonReturn;

    @FXML
    public Button buttonEasy;

    @FXML
    public Button buttonMedium;

    @FXML
    public Button buttonHard;

    @FXML
    public Button buttonUserGlobal;

    @FXML
    public TableView<Score> scoreTable;

    @FXML
    public AnchorPane anchorLbs;

    @FXML
    public Label labelTitle;

    @FXML
    public Button buttonRefresh;

    @FXML
    public Label labelInfo;
    private ScoreController scoreController;

    private UserController userController;

    private boolean showUserOnly;

    private ModeType currentMode;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

        Platform.runLater(() -> AudioMemory.getInstance().playSong(LEADERBOARD));

        scoreController = new ScoreController();
        userController = new UserController();

        currentMode = ModeType.EASY;
        showUserOnly = false;

        if (!userController.isLoggedIn()) {
            buttonUserGlobal.setText("Not logged in");
            buttonUserGlobal.setDisable(true);
        }

        // load leaderboards.jpeg from resources and set it as background
        String imageurl = Objects.requireNonNull(getClass().getClassLoader().getResource("images/leaderboards.jpg")).toExternalForm();
        anchorLbs.setStyle("-fx-background-image: url('" + imageurl + "'); " +
                "-fx-background-position: center center; " +
                "-fx-background-repeat: stretch;");

        initView();
        updateTable(ModeType.EASY, false);
        updateLabelInfo();
    }


    /**
     * Initializes the fxml elements in view.
     * calls initTable(), sets styles.
     */
    private void initView() {
        Font.loadFont(Objects.requireNonNull(getClass().getClassLoader().getResource("fonts/VCR_OSD_MONO_1.001.ttf")).toExternalForm(), 14);

        initTable();

        styleButton(buttonReturn);
        styleButton(buttonEasy);
        styleButton(buttonMedium);
        styleButton(buttonHard);
        styleButton(buttonUserGlobal);
        styleButton(buttonRefresh);

        // label set font and background should be dark purple with white font
        labelTitle.setStyle("-fx-font: 48px \"VCR OSD Mono\"; -fx-text-fill: white;");
        labelInfo.setStyle("-fx-font: 24px \"VCR OSD Mono\"; -fx-text-fill: white;");
    }


    /**
     * set fonts
     * get the font from resources folder
     * buttons set font and background should be dark purple with white font
     * also add hover light purple hover effect
     *
     * @param b Button to style
     */
    private void styleButton(Button b) {
        // get hex for dark purple and light purple and save them to variables
        String darkPurple = "#800080";
        String lightPurple = "#cc00cc";
        b.setFont(Font.font("VCR OSD Mono", 14));
        b.setStyle("-fx-background-color: " + darkPurple + "; -fx-text-fill: white;");
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: " + lightPurple + " ; -fx-text-fill: white;"));
        b.setOnMouseExited(e -> b.setStyle("-fx-background-color: " + darkPurple + "; -fx-text-fill: white;"));
    }

    /**
     * initializes the tableview, adds columns, fills them and styles the whole thing
     */
    private void initTable() {
        TableColumn<Score, String> nameCol = new TableColumn<>("Username");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Score, Integer> scoreCol = new TableColumn<>("Points");
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("points"));


        TableColumn<Score, Double> timeCol = new TableColumn<>("Time (s)");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        timeCol.setCellFactory(column -> new TableCell<Score, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                }
            }
        });


        TableColumn<Score, String> gradeCol = new TableColumn<>("Grade");
        gradeCol.setCellValueFactory(new PropertyValueFactory<>("grade"));
        gradeCol.setCellFactory(column -> new TableCell<Score, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    int stars = item.length();
                    HBox hBox = new HBox();
                    for (int i = 0; i < stars; i++) {
                        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getClassLoader()
                                .getResourceAsStream("images/smol_star.png"))));
                        imageView.setFitHeight(20);
                        imageView.setFitWidth(20);
                        hBox.getChildren().add(imageView);
                    }
                    setGraphic(hBox);
                }
            }
        });



        TableColumn<Score, Date> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        dateCol.setCellFactory(column -> {
            return new TableCell<>() {
                @Override
                protected void updateItem(Date date, boolean empty) {
                    super.updateItem(date, empty);
                    if (empty || date == null) {
                        setText(null);
                    } else {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                        LocalDateTime ldt = date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
                        setText(ldt.format(formatter));
                    }
                }
            };
        });

        // add mouse click events to nodes. prints score object to console
        scoreTable.setRowFactory(tv -> {
            TableRow<Score> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    Score rowData = row.getItem();
                    System.out.println(rowData);
                }
            });
            return row;
        });


        scoreTable.getColumns().clear();
        scoreTable.getColumns().addAll(nameCol, scoreCol, timeCol, gradeCol, dateCol);

        // center text on columns
        scoreTable.getColumns().forEach(column -> column.setStyle("-fx-alignment: CENTER;"));

        nameCol.setMinWidth(140);
        nameCol.setMaxWidth(140);
        scoreCol.setMinWidth(90);
        scoreCol.setMaxWidth(90);
        timeCol.setMinWidth(90);
        timeCol.setMaxWidth(90);
        gradeCol.setMinWidth(90);
        gradeCol.setMaxWidth(90);
        dateCol.setMinWidth(150);
        dateCol.setMaxWidth(150);

        scoreTable.setMinWidth(590);
        scoreTable.setMaxWidth(590);

        scoreTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        scoreTable.setStyle("-fx-font: 14px \"VCR OSD Mono\";");

    }


    /**
     * Updates the tableview with new scores
     *
     * @param mode     difficulty mode
     * @param userOnly if true, only shows user scores, else shows all scores
     */
    private void updateTable(ModeType mode, boolean userOnly) {
        ArrayList<Score> scoreList;
        if (userOnly) {
            scoreList = scoreController.getUserScoresRaw(mode);
            if (scoreList == null || scoreList.isEmpty()) {
                scoreController.fetchScores(mode);
                scoreList = scoreController.getUserScoresRaw(mode);
            }
        } else {
            scoreList = scoreController.getScoresRaw(mode);
            if (scoreList == null || scoreList.isEmpty()) {
                scoreController.fetchScores(mode);
                scoreList = scoreController.getScoresRaw(mode);
            }
        }


        ObservableList<Score> observableScoreList = FXCollections.observableArrayList(scoreList);
        scoreTable.setItems(observableScoreList);
    }

    /**
     * sets the current mode to easy and updates the tableview
     *
     * @param event button click event
     */
    @FXML
    public void setButtonEasy(ActionEvent event) {
        currentMode = ModeType.EASY;
        updateTable(ModeType.EASY, showUserOnly);
        updateLabelInfo();
    }

    /**
     * sets the current mode to medium and updates the tableview
     *
     * @param event button click event
     */
    @FXML
    public void setButtonMedium(ActionEvent event) {
        currentMode = ModeType.MEDIUM;
        updateTable(ModeType.MEDIUM, showUserOnly);
        updateLabelInfo();
    }


    /**
     * sets the current mode to hard and updates the tableview
     *
     * @param event button click event
     */
    @FXML
    public void setButtonHard(ActionEvent event) {
        currentMode = ModeType.HARD;
        updateTable(ModeType.HARD, showUserOnly);
        updateLabelInfo();
    }


    /**
     * toggles between showing user scores and global scores
     *
     * @param event button click event
     */
    @FXML
    public void setButtonUserGlobal(ActionEvent event) {

        showUserOnly = !showUserOnly;
        buttonUserGlobal.setText(showUserOnly ? "Global Scores" : "Personal Scores");
        updateTable(currentMode, showUserOnly);
        updateLabelInfo();
    }


    /**
     * returns to main menu
     *
     * @param event button click event
     */
    @FXML
    public void setButtonReturn(ActionEvent event) {

        Platform.runLater(() -> AudioMemory.getInstance().stopSong(LEADERBOARD));
        try {
            Navigaattori.getInstance().changeScene(ModeType.MENU);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * updates the info label to reflect currently displayed scores
     */
    private void updateLabelInfo() {
        String mode = currentMode.toString().toLowerCase();
        //Capitalize first letter
        mode = mode.substring(0, 1).toUpperCase() + mode.substring(1);

        String username = userController.getUsername();
        username = username.substring(0, 1).toUpperCase() + username.substring(1);

        String text = showUserOnly ? username + "'s Scores - " : "Global Scores - ";
        text += mode;
        labelInfo.setText(text);
    }


    /**
     * reloads scores from server
     *
     * @param event button click event
     */
    @FXML
    public void setButtonRefresh(ActionEvent event) {
        buttonRefresh.setDisable(true);
        buttonRefresh.setText("Reloading...");

        // new thread to fetch scores
        Thread thread = new Thread(() -> {
            scoreController.fetchScores(ModeType.EASY);
            scoreController.fetchScores(ModeType.MEDIUM);
            scoreController.fetchScores(ModeType.HARD);
            scoreController.fetchPersonalScores();
        });
        thread.start();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    buttonRefresh.setDisable(false);
                    buttonRefresh.setText("Reload Scores");
                    updateTable(currentMode, showUserOnly);
                });
            }
        }, 5000);
    }
}


