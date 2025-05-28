module com.hmo.hear_me_out {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.hmo.hear_me_out to javafx.fxml;
    exports com.hmo.hear_me_out;
}