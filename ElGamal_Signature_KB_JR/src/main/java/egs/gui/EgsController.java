package egs.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

public class EgsController {

    @FXML
    private Button testButton;

    @FXML
    private Label welcomeText;

    @FXML
    protected void onTestButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.showSaveDialog(testButton.getScene().getWindow());
    }
}