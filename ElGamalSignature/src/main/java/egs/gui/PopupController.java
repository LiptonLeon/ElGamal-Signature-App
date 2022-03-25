package egs.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PopupController {

    @FXML
    private Label infoLabel;

    public void setInfo (String info) {
        infoLabel.setText(info);
    }

    public void onOkClicked () {
        ((Stage) infoLabel.getScene().getWindow()).close();
    }

}
