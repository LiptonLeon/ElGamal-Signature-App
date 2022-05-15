package egs.gui;

import egs.alg.ElGamal;
import egs.alg.util.FileIO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class EgsController implements Initializable {

    // ElGamal
    private final ElGamal elGamal = new ElGamal();

    // Strings
    private final StringProperty gKey = new SimpleStringProperty();
    private final StringProperty hKey = new SimpleStringProperty();
    private final StringProperty aKey = new SimpleStringProperty();
    private final StringProperty modN = new SimpleStringProperty();
    private final StringProperty text = new SimpleStringProperty();
    private final StringProperty sign = new SimpleStringProperty();
    private String fileText;

    @FXML
    public ToggleGroup input;

    @FXML
    private Label textPath;

    @FXML
    private Label signPath;

    @FXML
    private Label keyPath;

    @FXML
    private TextField gKeyField;

    @FXML
    private TextField hKeyField;

    @FXML
    private TextField aKeyField;

    @FXML
    private TextField modNField;

    @FXML
    private TextArea textField;

    @FXML
    private TextArea signField;

    private final FileChooser fileChooser = new FileChooser();
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Bind TextFields with properties
        gKeyField.textProperty().bindBidirectional(gKey);
        hKeyField.textProperty().bindBidirectional(hKey);
        aKeyField.textProperty().bindBidirectional(aKey);
        modNField.textProperty().bindBidirectional(modN);
        textField.textProperty().bindBidirectional(text);
        signField.textProperty().bindBidirectional(sign);
    }

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

    public void onTextLoad() throws IOException {
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            fileText = FileIO.getFileContentString(file.getPath());
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

    public void onSignLoad() throws IOException {
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            sign.setValue(FileIO.getFileContentString(file.getPath()));
            signPath.setText(file.getPath());
        }
    }

    public void onSignSave() {
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                Writer writer = new FileWriter(file);
                writer.write(sign.getValue());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            signPath.setText(file.getPath());
        }
    }

    public void onKeyLoad() {
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                Scanner scanner = new Scanner(file);
                gKey.setValue(scanner.nextLine());
                hKey.setValue(scanner.nextLine());
                aKey.setValue(scanner.nextLine());
                modN.setValue(scanner.nextLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
            keyPath.setText(file.getPath());
        }
    }

    public void onKeySave() {
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                Writer writer = new FileWriter(file);
                writer.write(gKey.getValue());
                writer.write("\n");
                writer.write(hKey.getValue());
                writer.write("\n");
                writer.write(aKey.getValue());
                writer.write("\n");
                writer.write(modN.getValue());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            keyPath.setText(file.getPath());
        }
    }

    public void onSignAction() {
        openPopup("Podpisywanie\nniezaimplementowane!");
    }

    public void onVerifyAction() {
        openPopup("Weryfikacja niezaimplementowana!");
    }

    private void openPopup(String info) {
        Stage popupStage = new Stage();
        popupStage.initStyle(StageStyle.TRANSPARENT);
        popupStage.setOpacity(0.95);
        popupStage.initModality(Modality.APPLICATION_MODAL);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(EgsApplication.class.getResource("popup-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.setFill(Color.TRANSPARENT);
            popupStage.setScene(scene);
            ((PopupController) fxmlLoader.getController()).setInfo(info);
            popupStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}