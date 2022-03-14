package egs.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EgsController {

    @FXML
    private Button testButton;

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

}