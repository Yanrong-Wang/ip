package lizzy.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lizzy.Lizzy;

/**
 * Handles user interaction in Lizzy's main window.
 */
public class MainWindow {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    /**
     * Generates responses for commands entered in the window.
     */
    private Lizzy lizzy;

    /**
     * Keeps the newest dialog visible as the conversation grows.
     */
    @FXML
    private void initialize() {
        dialogContainer.heightProperty().addListener(
                observable -> scrollPane.setVvalue(1.0));
    }

    /**
     * Connects the window to a chatbot session and displays its greeting.
     *
     * @param lizzy the chatbot that will process user commands
     */
    public void setLizzy(Lizzy lizzy) {
        this.lizzy = lizzy;
        dialogContainer.getChildren().add(
                DialogBox.getLizzyDialog("Good day! I'm Elizabeth Bennet—Lizzy, if you please.\n"
                        + "Tell me, what shall we set in order?"));
    }

    /**
     * Places keyboard focus in the command field.
     */
    public void focusInput() {
        userInput.requestFocus();
    }

    /**
     * Sends the current text to Lizzy and displays both sides of the exchange.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().strip();
        if (input.isEmpty()) {
            return;
        }

        dialogContainer.getChildren().add(DialogBox.getUserDialog(input));
        Lizzy.Response response = lizzy.getResponseWithStatus(input);
        DialogBox responseDialog = response.isError()
                ? DialogBox.getErrorDialog(response.text())
                : DialogBox.getLizzyDialog(response.text());
        dialogContainer.getChildren().add(responseDialog);
        userInput.clear();
        userInput.requestFocus();
    }
}
