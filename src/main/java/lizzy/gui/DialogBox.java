package lizzy.gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Displays one speaker label and one wrapped chat message.
 */
public class DialogBox extends HBox {
    private static final String DIALOG_BOX_FXML = "/view/DialogBox.fxml";

    @FXML
    private Label speaker;
    @FXML
    private Label dialog;

    /**
     * Loads the reusable dialog layout and fills it with one message.
     *
     * @param text the message to display
     * @param speakerName the name shown beside the message
     */
    private DialogBox(String text, String speakerName) {
        FXMLLoader fxmlLoader = new FXMLLoader(DialogBox.class.getResource(DIALOG_BOX_FXML));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load the dialog box layout", exception);
        }
        speaker.setText(speakerName);
        dialog.setText(text);
        dialog.maxWidthProperty().bind(widthProperty().multiply(0.78));
    }

    /**
     * Creates a right-aligned dialog for a user command.
     *
     * @param text the command entered by the user
     * @return a dialog styled for the user
     */
    public static DialogBox getUserDialog(String text) {
        DialogBox dialogBox = new DialogBox(text, "You");
        dialogBox.getChildren().add(dialogBox.getChildren().remove(0));
        dialogBox.setAlignment(Pos.TOP_RIGHT);
        dialogBox.speaker.setManaged(false);
        dialogBox.speaker.setVisible(false);
        dialogBox.getStyleClass().add("user-dialog");
        return dialogBox;
    }

    /**
     * Creates a left-aligned dialog for a chatbot response.
     *
     * @param text Lizzy's response
     * @return a dialog styled for Lizzy
     */
    public static DialogBox getLizzyDialog(String text) {
        DialogBox dialogBox = new DialogBox(text, "Lizzy");
        dialogBox.setAlignment(Pos.TOP_LEFT);
        dialogBox.getStyleClass().add("lizzy-dialog");
        return dialogBox;
    }

    /**
     * Creates a visually prominent dialog for an invalid command.
     *
     * @param text Lizzy's explanation of the invalid command
     * @return a dialog styled as an error
     */
    public static DialogBox getErrorDialog(String text) {
        DialogBox dialogBox = getLizzyDialog(text);
        dialogBox.speaker.setText("A gentle correction");
        dialogBox.getStyleClass().add("error-dialog");
        return dialogBox;
    }
}
