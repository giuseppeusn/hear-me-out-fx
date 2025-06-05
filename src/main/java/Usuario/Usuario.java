package Usuario;

import com.hmo_fx.hear_me_out_fx.Main;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import music.FormMusicScreen;

public class Usuario {
    Scene scene;
    Stage stage;

    public Usuario(Stage stage) {
        this.stage = stage;

    }

    public void showUser() {
        this.criarsuario();
        this.stage.setScene(this.scene);
    }

    private void criarsuario() {
        stage.setTitle("Hear Me Out");

        VBox root = new VBox(20);
        root.setStyle("-fx-background-color: #212121;");
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));

        Button backButton = new Button("Voltar");
        backButton.setStyle("-fx-background-color: #2e2e2e; -fx-text-fill: white; -fx-font-weight: bold;");
        backButton.setOnAction(e -> {
            Main main = new Main();
            main.showMainScreen(stage);
        });

        HBox backContainer = new HBox(backButton);
        backContainer.setAlignment(Pos.CENTER_LEFT);
        backContainer.setMaxWidth(Double.MAX_VALUE);

        Label lbl = new Label("Usuario");
        lbl.setStyle("-fx-text-fill: white; -fx-font-size: 30px; -fx-font-weight: bold;");

        Button addButton = new Button("Novo usuario ");
        addButton.setStyle("-fx-background-color: #1db954; -fx-text-fill: white; -fx-font-weight: bold;");
        addButton.setOnAction(e -> {
            FormUsuario formUser = new FormUsuario(stage, null);
            formUser.show();
        });

        root.getChildren().addAll(
                backContainer,
                lbl,
                addButton
        );

        this.scene = new Scene(root, 600, 600);
    }
};
