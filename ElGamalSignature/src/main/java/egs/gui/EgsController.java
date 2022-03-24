package egs.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class EgsController {

    @FXML
    private Label textPath;

    @FXML
    private Label signPath;

    @FXML
    private Label keyPath;

    private final FileChooser fileChooser = new FileChooser();
    private Stage stage;

    // FXResizeHelper can be initialized after passing stage to controller
    public void setStage(Stage stage) {
        this.stage = stage;
        new FXResizeHelper(stage, 15, 15);
    }

    @FXML
    protected void onExitClicked() {
        stage.close();
    }

    @FXML
    protected void onMaximizeClicked() {
        stage.setMaximized(!stage.isMaximized());
    }

    @FXML
    protected void onMinimizeClicked() {
        stage.setIconified(true);
    }

    public void onTextLoad() {
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            //TODO: Load text
            textPath.setText(file.getPath());
        }
    }

//    public void onTextSave() {
//        File file = fileChooser.showSaveDialog(stage);
//        if (file != null) {
//            //TODO: Save text
//            textPath.setText(file.getPath());
//        }
//    }

    public void onSignLoad() {
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            //TODO: Load signature
            signPath.setText(file.getPath());
        }
    }

    public void onSignSave() {
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            //TODO: Save signature
            signPath.setText(file.getPath());
        }
    }

    public void onKeyLoad() {
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            //TODO: Load keys
            keyPath.setText(file.getPath());
        }
    }

    public void onKeySave() {
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            //TODO: Save keys
            keyPath.setText(file.getPath());
        }
    }
}