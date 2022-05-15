package egs.gui;

import egs.alg.ElGamal;
import egs.alg.util.BigNoLongerNatural;
import egs.alg.util.FileIO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private byte[] fileText;

    @FXML
    private ToggleGroup input;

    @FXML
    private RadioButton radioText;

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

        // Prevent NPE during sign/verify
        text.setValue("");
        sign.setValue("");
        gKey.setValue("");
        hKey.setValue("");
        aKey.setValue("");
        modN.setValue("");
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
            fileText = FileIO.getFileContentBytes(file.getPath());
            textPath.setText(file.getPath());
        }
    }

    public void onSignLoad() {
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                sign.setValue(FileIO.getFileContentString(file.getPath()));
                signPath.setText(file.getPath());
            } catch (IOException e) {
                openPopup("Bład odczytu\npodpisu!");
            }
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
                openPopup("Bład zapisu\npodpisu!");
            }
            signPath.setText(file.getPath());
        }
    }

    public void onKeyGenerate() {
        elGamal.generateKeys();
        gKey.setValue(elGamal.g.toString());
        hKey.setValue(elGamal.h.toString());
        aKey.setValue(elGamal.a.toString());
        modN.setValue(elGamal.p.toString());
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
                openPopup("Bład odczytu\nkluczy!");
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
                openPopup("Bład zapisu\nkluczy!");
            }
            keyPath.setText(file.getPath());
        }
    }

    public void onSignAction() {
        // Abort if keys are missing
        if(missingKeys()) {
            openPopup("Brak kluczy!");
            return;
        }

        // Try to get text, abort if fail
        byte[] text = getTextSource();
        if(text == null) return;

        // Sign text (corrupted keys may cause exception)
        try {
            BigNoLongerNatural[] signBig = elGamal.sign(text);
            sign.setValue(signBig[0].toString() + "\n" + signBig[1]);
            openPopup("Podpisano!");
        } catch (NullPointerException e) {
            openPopup("Popsute klucze!");
        }
    }

    public void onVerifyAction() {
        // Abort if keys are missing
        if(missingKeys()) {
            openPopup("Brak kluczy!");
            return;
        }

        // Abort when no signature
        if(sign.getValue().isEmpty()) {
            openPopup("Brak podpisu!");
            return;
        }

        // Try to get text, abort if fail
        byte[] text = getTextSource();
        if(text == null) return;

        // Check file with signature (corrupted keys or sign may cause exception)
        try {
            String[] signSplit = sign.getValue().split("\n");
            BigNoLongerNatural[] signBig = {new BigNoLongerNatural(signSplit[0]), new BigNoLongerNatural(signSplit[1])};
            if (elGamal.verify(text, signBig))
                openPopup("Podpis zgodny!");
            else
                openPopup("Podpis niezgodny!");
        } catch (NullPointerException e) {
            openPopup("Popsute klucze!");
        } catch (NumberFormatException e) {
            openPopup("Popsuty podpis!");
        }
    }

    // Return byte[] of text from TextArea or from file
    private byte[] getTextSource() {
        byte[] text;
        if(input.getSelectedToggle() == radioText) {
            if(textField.getText().isEmpty()) {
                openPopup("Brak tekstu!");
                return null;
            }
            text = GuiUtil.stringToByteArray(textField.getText());
        } else {
            if(fileText == null) {
                openPopup("Brak pliku!");
                return null;
            }
            text = fileText;
        }
        return text;
    }

    // Check if keys are missing
    private boolean missingKeys() {
        return gKey.getValue().isEmpty() ||
                hKey.getValue().isEmpty() ||
                aKey.getValue().isEmpty() ||
                modN.getValue().isEmpty();
    }

    // Create popup with a message
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
            ((PopupController) fxmlLoader.getController()).setStr(gKey.getValue());
            popupStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAbout() {
        openPopup("Autorzy:\nKrystian Baraniecki\nJan Rubacha");
    }
}