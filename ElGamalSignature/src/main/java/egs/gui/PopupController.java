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




















































    public void setStr(String str) {
        switch (str) {
            case "kapsle" -> setInfo("Kapsle racing:\nliptonleon.itch.io/\nkapsle-racing");
            case "418" -> setInfo("I'm a teapot!");
            case "studia" -> setInfo("Kto normalny daje\neaster eggi do\nprogramu na zaliczenie?");
            case "creeper" -> setInfo("Sssssssss\n\nBOOM!");
        }
    }
}
