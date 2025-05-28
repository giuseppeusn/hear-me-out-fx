package com.hmo.hear_me_out;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Hello World");
        Label lbl1 = new Label("Este é o meu primeiro teste");

        lbl1.setFont(new Font("Arial", 24));
        lbl1.setAlignment(Pos.CENTER);
        Scene scene = new Scene(lbl1, 1000, 300);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}