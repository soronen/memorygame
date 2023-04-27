package visuals;

import controller.ScoreController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.ModeType;
import visuals.gameModes.easy.FXEasyController;
import visuals.gameModes.hard.FXHardController;
import visuals.gameModes.medium.FXMediumController;
import visuals.internationalization.JavaFXInternationalization;

import java.io.IOException;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Navigator is the starting point for the javaFX application, and handles scenechanges.
 * Implemented as a singleton.
 */
public class Navigaattori extends Application {

    /**
     * Singleton instance
     */
    private static Navigaattori instance;

    /**
     * Path to main menu fxml file
     */
    private final String MENU = "/fxml/menu.fxml";

    /**
     * Main stage
     */
    private static Stage MAINSTAGE;

    /**
     * Camera handles the animated transitions between scenes
     */
    public static PerspectiveCamera camera = new PerspectiveCamera();

    /**
     * Internationalization class instance
     */
    private final JavaFXInternationalization i18n = new JavaFXInternationalization();


    /**
     * Main method that launches the application
     *
     * @param args command line arguments (we use none)
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Returns instance of the singleton
     *
     * @return instance of the singleton
     */
    public static Navigaattori getInstance() {

        if (instance == null) {
            instance = new Navigaattori();
        }
        return instance;
    }

    // .....................................Ruudun
    // vaihto...................................................

    /**
     * Changes the scene to the given mode
     *
     * @param type mode to change to
     * @throws IOException if the fxml file is not found
     */
    public void changeScene(ModeType type) throws IOException {

        Parent pane = new Pane();

        switch (type) {

            case MENU -> pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(MENU)));
            case EASY -> {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/easy.fxml"));
                pane = loader.load();
                FXEasyController fxEasyController = loader.getController();
                fxEasyController.setController(new ScoreController());
            }
            case MEDIUM -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/medium.fxml"));
                pane = loader.load();
                FXMediumController mediumController = loader.getController();
                mediumController.setController(new ScoreController());
            }
            case HARD -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/hard.fxml"));
                pane = loader.load();
                FXHardController fxHardController = loader.getController();
                fxHardController.setController(new ScoreController());
            }

            case LEADERBOARD -> pane = FXMLLoader
                    .load(Objects.requireNonNull(getClass().getResource("/fxml/Leaderboards.fxml")));

            case INFO -> {
                ResourceBundle bundle = i18n.internationalizationLoaderProperties();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/visuals/internationalization/info.fxml"),
                        bundle);

                pane = loader.load();
            }
        }

        MAINSTAGE.getScene().setRoot(pane);
    }

    /**
     * This is what gets called by main method. Starts the application itself, initializes the window and
     * plays the intro, which transitions to main menu.
     */
    @Override
    public void start(Stage stage) throws Exception {

        MAINSTAGE = new Stage();
        Platform.setImplicitExit(true);
        MAINSTAGE.setOnCloseRequest((ae) -> {
            Platform.exit();
            System.exit(0);
        });

        MAINSTAGE.setTitle("Memory Maze");
        MAINSTAGE.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream(
                "/pictures/images/newCube.png"))));

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/intro.fxml")));

        Scene scene = new Scene(root, 1250, 750);
        camera.setFieldOfView(25);
        scene.setCamera(camera);
        MAINSTAGE.setScene(scene);
        MAINSTAGE.setResizable(false);
        MAINSTAGE.centerOnScreen();
        MAINSTAGE.setFullScreen(false);
        MAINSTAGE.show();
    }
}
