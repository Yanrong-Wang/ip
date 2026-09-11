package lizzy.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lizzy.Lizzy;

/**
 * Configures and displays Lizzy's JavaFX window.
 */
public class Main extends Application {
    private static final String MAIN_WINDOW_FXML = "/view/MainWindow.fxml";

    /**
     * Builds the main window and connects it to the chatbot.
     *
     * @param stage the primary JavaFX stage
     * @throws IOException if the FXML resource cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(MAIN_WINDOW_FXML));
        Parent root = fxmlLoader.load();
        MainWindow mainWindow = fxmlLoader.getController();
        mainWindow.setLizzy(new Lizzy());

        stage.setTitle("Lizzy");
        stage.setMinWidth(400);
        stage.setMinHeight(480);
        stage.setResizable(true);
        stage.setScene(new Scene(root));
        stage.show();
        mainWindow.focusInput();
    }
}
