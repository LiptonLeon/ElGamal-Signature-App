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
import java.util.Locale;
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

    // File choosers
    private final FileChooser keyChooser = new FileChooser();
    private final FileChooser signChooser = new FileChooser();

    // Resources
    private final Locale enLocale = new Locale("en", "EN");
    private final Locale plLocale = new Locale("pl", "PL");
    private final ResourceBundle enBundle = ResourceBundle.getBundle("egs/gui/lang", enLocale);
    private final ResourceBundle plBundle = ResourceBundle.getBundle("egs/gui/lang", plLocale);
    private boolean isPolish = false;

    @FXML
    private ToggleGroup input;

    @FXML
    private RadioButton radioText;

    @FXML
    private RadioButton radioFile;

    @FXML
    private Button loadButton1;

    @FXML
    private Button loadButton2;

    @FXML
    private Button loadButton3;

    @FXML
    private Button saveButton1;

    @FXML
    private Button saveButton2;

    @FXML
    private Button signButton;

    @FXML
    private Button verifyButton;

    @FXML
    private Button generateButton;

    @FXML
    private Label gLabel;

    @FXML
    private Label hLabel;

    @FXML
    private Label aLabel;

    @FXML
    private Label nLabel;

    @FXML
    private Label textTitle;

    @FXML
    private Label fileTitle;

    @FXML
    private Label signatureTitle;

    @FXML
    private TitledPane keysTitle;

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

    @FXML
    private Button languageButton;

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

        //Extension filters for FileChoosers
        FileChooser.ExtensionFilter defaultExt = new FileChooser.ExtensionFilter("Inny plik (*.*)", "*.*");
        keyChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Plik z kluczami (*.key)", "*.key"));
        keyChooser.getExtensionFilters().add(defaultExt);
        signChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Plik z podpisem (*.sign)", "*.sign"));
        signChooser.getExtensionFilters().add(defaultExt);

        setText();
    }

    // Set labels
    private void setText() {
        textTitle.setText(setString("text"));
        fileTitle.setText(setString("file"));
        signatureTitle.setText(setString("signature"));
        keysTitle.setText(setString("keys"));
        gLabel.setText(setString("g"));
        hLabel.setText(setString("h"));
        aLabel.setText(setString("a"));
        nLabel.setText(setString("n"));
        radioText.setText(setString("text"));
        radioFile.setText(setString("file"));
        loadButton1.setText(setString("load"));
        loadButton2.setText(setString("load"));
        loadButton3.setText(setString("load"));
        saveButton1.setText(setString("save"));
        saveButton2.setText(setString("save"));
        generateButton.setText(setString("generate"));
        signButton.setText(setString("sign") + " →");
        verifyButton.setText("← " + setString("verify"));
    }

    @FXML
    protected void onLanguage() {
        if(isPolish) {
            isPolish = false;
            languageButton.setText("PL");
        } else {
            isPolish = true;
            languageButton.setText("EN");
        }
        setText();
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

    public void onTextLoad() {
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                fileText = FileIO.getFileContentBytes(file.getPath());
                textPath.setText(file.getPath());
            } catch (IOException e) {
                openPopup(setString("io.file_read_fail"));
            }
        }
    }

    public void onSignLoad() {
        File file = signChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                sign.setValue(FileIO.getFileContentString(file.getPath()));
                signPath.setText(file.getPath());
            } catch (IOException e) {
                openPopup(setString("io.sign_read_fail"));
            }
        }
    }

    public void onSignSave() {
        File file = signChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                Writer writer = new FileWriter(file);
                writer.write(sign.getValue());
                writer.close();
            } catch (IOException e) {
                openPopup(setString("io.sign_save_fail"));
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
        File file = keyChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                Scanner scanner = new Scanner(file);
                gKey.setValue(scanner.nextLine());
                hKey.setValue(scanner.nextLine());
                aKey.setValue(scanner.nextLine());
                modN.setValue(scanner.nextLine());
            } catch (IOException e) {
                openPopup(setString("io.key_read_fail"));
            }
            keyPath.setText(file.getPath());
        }
    }

    public void onKeySave() {
        File file = keyChooser.showSaveDialog(stage);
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
                openPopup(setString("io.key_save_fail"));
            }
            keyPath.setText(file.getPath());
        }
    }

    // Sign
    public void onSignAction() {
        // Abort if keys are missing
        if(missingKeysSign()) {
            openPopup(setString("action.keys_missing"));
            return;
        }

        // Try to get text, abort if fail
        byte[] text = getTextSource();
        if(text == null) return;

        // Update keys in ElGamal
        try {
            setKeysSign();
        } catch (NumberFormatException e) {
            openPopup(setString("action.keys_broken"));
            return;
        }

        // Verify keys
        if(!elGamal.verifyG()) {
            openPopup(setString("action.bad_g"));
            return;
        }
        if(!elGamal.verifyH()) {
            openPopup(setString("action.bad_h"));
            return;
        }
        if(!elGamal.verifyA()) {
            openPopup(setString("action.bad_a"));
            return;
        }

        // Sign text (corrupted keys may cause exception)
        try {
            BigNoLongerNatural[] signBig = elGamal.sign(text);
            sign.setValue(signBig[0].toString() + "\n" + signBig[1]);
            openPopup(setString("action.signed"));
        } catch (NullPointerException e) {
            e.printStackTrace();
            openPopup(setString("action.keys_broken"));
        }
    }

    // Verify
    public void onVerifyAction() {
        // Abort if keys are missing
        if(missingKeysVerify()) {
            openPopup(setString("action.keys_missing"));
            return;
        }

        // Abort when no signature
        if(sign.getValue().isEmpty()) {
            openPopup(setString("action.sign_missing"));
            return;
        }

        // Try to get text, abort if fail
        byte[] text = getTextSource();
        if(text == null) return;

        // Update keys in ElGamal
        try {
            setKeysVerify();
        } catch (NumberFormatException e) {
            openPopup(setString("action.keys_broken"));
            return;
        }

        // Verify keys
        if(!elGamal.verifyG()) {
            openPopup(setString("action.bad_g"));
            return;
        }
        if(elGamal.a != null){
            if(!elGamal.verifyH()) {
                openPopup(setString("action.bad_h"));
                return;
            }
        }

        // Check file with signature (corrupted keys or sign may cause exception)
        try {
            String[] signSplit = sign.getValue().split("\n");
            BigNoLongerNatural[] signBig = {new BigNoLongerNatural(signSplit[0]), new BigNoLongerNatural(signSplit[1])};
            if (elGamal.verify(text, signBig))
                openPopup(setString("action.valid"));
            else
                openPopup(setString("action.invalid"));
        } catch (NullPointerException e) {
            openPopup(setString("action.keys_broken"));
        } catch (NumberFormatException e) {
            openPopup(setString("action.sign_broken"));
        }
    }

    // Return byte[] of text from TextArea or from file
    private byte[] getTextSource() {
        byte[] text;
        if(input.getSelectedToggle() == radioText) {
            if(textField.getText().isEmpty()) {
                openPopup(setString("action.text_missing"));
                return null;
            }
            text = GuiUtil.stringToByteArray(textField.getText());
        } else {
            if(fileText == null) {
                openPopup(setString("action.file_missing"));
                return null;
            }
            text = fileText;
        }
        return text;
    }

    // Check if keys are missing
    private boolean missingKeysVerify() {
        return gKey.getValue().isEmpty() ||
                hKey.getValue().isEmpty() ||
                modN.getValue().isEmpty();
    }

    private boolean missingKeysSign() {
        return missingKeysVerify() ||
                modN.getValue().isEmpty();
    }

    // Set keys in ElGamal
    private void setKeysVerify() {
        elGamal.g = new BigNoLongerNatural(gKey.getValue());
        elGamal.h = new BigNoLongerNatural(hKey.getValue());
        elGamal.p = new BigNoLongerNatural(modN.getValue());
        elGamal.generatepMinOne();
    }

    private void setKeysSign() {
        setKeysVerify();
        elGamal.a = new BigNoLongerNatural(aKey.getValue());
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
        openPopup(setString("authors1") + "\n" + setString("authors2") + "\n" + setString("authors3"));
    }

    private String setString(String key) {
        if(isPolish)
            return plBundle.getString(key);
        else
            return enBundle.getString(key);
    }
}