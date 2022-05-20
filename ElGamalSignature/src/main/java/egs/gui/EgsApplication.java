package egs.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

public class EgsApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        System.setProperty("prism.lcdtext", "false");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setOpacity(0.96);
        stage.setMinHeight(500);
        stage.setMinWidth(520);

        ResourceBundle bundle = ResourceBundle.getBundle("egs/gui/lang", new Locale("en", "EN"));
        FXMLLoader fxmlLoader = new FXMLLoader(EgsApplication.class.getResource("app-view2.fxml"), bundle);
        Scene scene = new Scene(fxmlLoader.load());
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        ((EgsController) fxmlLoader.getController()).setStage(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}