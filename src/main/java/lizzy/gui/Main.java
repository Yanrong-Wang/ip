package lizzy.gui;

import java.io.IOException;
import java.io.InputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lizzy.Lizzy;

/**
 * Configures and displays Lizzy's JavaFX window.
 */
public class Main extends Application {
    private static final double DEFAULT_FONT_SIZE = 14.0;
    private static final double MINIMUM_WINDOW_HEIGHT = 480.0;
    private static final double MINIMUM_WINDOW_WIDTH = 400.0;
    private static final String[] FONT_RESOURCES = {
        "/fonts/SourceSerif4-Regular.ttf",
        "/fonts/SourceSerif4-Italic.ttf",
        "/fonts/SourceSerif4-Semibold.ttf"
    };
    private static final String MAIN_WINDOW_FXML = "/view/MainWindow.fxml";

    /**
     * Builds the main window and connects it to the chatbot.
     *
     * @param stage the primary JavaFX stage
     * @throws IOException if the FXML resource cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        registerApplicationFonts();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(MAIN_WINDOW_FXML));
        Parent root = fxmlLoader.load();
        MainWindow mainWindow = fxmlLoader.getController();
        mainWindow.setLizzy(new Lizzy());

        stage.setTitle("Lizzy");
        double availableScreenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
        stage.setMinHeight(Math.min(MINIMUM_WINDOW_HEIGHT, availableScreenHeight));
        stage.setMaxHeight(availableScreenHeight);
        stage.setResizable(true);
        stage.setScene(new Scene(root));
        stage.show();
        mainWindow.focusInput();
    }

    /**
     * Registers the bundled font faces before the scene's stylesheet is applied.
     *
     * @throws IOException if a font resource is missing or cannot be loaded
     */
    private static void registerApplicationFonts() throws IOException {
        for (String fontResource : FONT_RESOURCES) {
            try (InputStream fontStream = Main.class.getResourceAsStream(fontResource)) {
                if (fontStream == null || Font.loadFont(fontStream, DEFAULT_FONT_SIZE) == null) {
                    throw new IOException("Unable to load application font: " + fontResource);
                }
            }
        }
    }
}
