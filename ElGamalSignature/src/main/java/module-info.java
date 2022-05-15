module egs {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.io;


    opens egs to javafx.fxml;
    exports egs;
    exports egs.gui;
    exports egs.alg.util;
    exports egs.alg;
    opens egs.gui to javafx.fxml;
}