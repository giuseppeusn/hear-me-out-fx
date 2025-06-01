package com.hmo_fx.hear_me_out_fx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import music.MusicScreen;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        this.showMainScreen(stage);
    }

    public void showMainScreen(Stage stage) {
        stage.setTitle("Hear Me Out");

        VBox root = new VBox(20);
        root.setStyle("-fx-background-color: #212121;");
        root.setAlignment(Pos.CENTER);

        Label lbl1 = new Label("Hear Me Out");
        lbl1.setFont(new Font("Arial", 24));
        lbl1.setStyle("-fx-text-fill: white;");
        lbl1.setAlignment(Pos.CENTER);

        VBox.setMargin(lbl1, new Insets(0, 0, 20, 0));

        ArrayList<Button> buttons = this.renderButtons(stage);

        root.getChildren().addAll(lbl1);
        root.getChildren().addAll(buttons);

        Scene scene = new Scene(root, 600, 600);
        stage.setScene(scene);
        stage.show();
    }


    public void showUsers(Stage stage) {
        // Implementar lógica para mostrar tela usuários
    }

    public void showArtists(Stage stage) {
        // Implementar lógica para mostrar tela artistas
    }

    public void showCritics(Stage stage) {
        // Implementar lógica para mostrar tela críticos
    }

    public void showMusic(Stage stage) {
        MusicScreen musicScreen = new MusicScreen(stage);
        musicScreen.show();
    }

    public void showAlbums(Stage stage) {
        // Implementar lógica para mostrar tela álbuns
    }

    public ArrayList<Button> renderButtons(Stage stage) {
        String[] labels = {"Usuários", "Artistas", "Críticos", "Músicas", "Álbuns"};

        ArrayList<Button> buttons = new ArrayList<>();

        for (String text : labels) {
            Button btn = getButton(text);

            buttons.add(btn);
        }

        buttons.get(0).setOnAction(e -> this.showUsers(stage));
        buttons.get(1).setOnAction(e -> this.showArtists(stage));
        buttons.get(2).setOnAction(e -> this.showCritics(stage));
        buttons.get(3).setOnAction(e -> this.showMusic(stage));
        buttons.get(4).setOnAction(e -> this.showAlbums(stage));

        return buttons;
    }

    private static Button getButton(String text) {
        Button btn = new Button(text);
        btn.setFont(new Font("Arial", 24));
        btn.setPrefWidth(300);
        btn.setStyle(
                "-fx-background-color: #2e2e2e;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 10 20 10 20;"
        );

        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: #282828;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 10 20 10 20;"
        ));

        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: #2e2e2e;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 10 20 10 20;"
        ));
        return btn;
    }

    public static void main(String[] args) {
        launch();
    }
}