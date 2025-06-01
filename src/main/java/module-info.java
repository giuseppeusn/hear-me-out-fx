module com.hmo_fx.hear_me_out_fx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.hmo_fx.hear_me_out_fx to javafx.fxml;
    exports com.hmo_fx.hear_me_out_fx;
    exports music;
    opens music to javafx.fxml;
}