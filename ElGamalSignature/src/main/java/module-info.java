module egs {
    requires javafx.controls;
    requires javafx.fxml;


    opens egs to javafx.fxml;
    exports egs;
    exports egs.gui;
    exports egs.alg.util;
    exports egs.alg;
    opens egs.gui to javafx.fxml;
}