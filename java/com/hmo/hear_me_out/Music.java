package com.hmo.hear_me_out;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Music {
    Scene scene;
    Stage stage;

    public Music(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        this.createUI();

        this.stage.setScene(this.scene);
    }

    private void createUI() {
        stage.setTitle("Hear Me Out");

        VBox root = new VBox(10);
        root.setStyle("-fx-background-color: #000;");
        root.setAlignment(Pos.CENTER);

        Label lbl = new Label("Música");
        root.getChildren().add(lbl);

        this.scene = new Scene(root, 500, 500);
    }
}
