module egs {
    requires javafx.controls;
    requires javafx.fxml;


    opens egs to javafx.fxml;
    exports egs;
    exports egs.gui;
    opens egs.gui to javafx.fxml;
}