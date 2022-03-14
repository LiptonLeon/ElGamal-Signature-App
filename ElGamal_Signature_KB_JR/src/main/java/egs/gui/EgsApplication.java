package egs.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class EgsApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        System.setProperty("prism.lcdtext", "false");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setOpacity(0.95);
        stage.setTitle("ElGamal Signature App");

        FXMLLoader fxmlLoader = new FXMLLoader(EgsApplication.class.getResource("app-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        ((EgsController) fxmlLoader.getController()).setStage(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}