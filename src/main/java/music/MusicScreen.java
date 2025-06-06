package music;

import com.hmo_fx.hear_me_out_fx.Main;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

public class MusicScreen {
    Scene scene;
    Stage stage;

    public MusicScreen(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        this.createUI();
        this.stage.setScene(this.scene);
    }

    private void createUI() {
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

        Label lbl = new Label("Músicas");
        lbl.setStyle("-fx-text-fill: white; -fx-font-size: 30px; -fx-font-weight: bold;");

        Button addButton = new Button("Nova música");
        addButton.setStyle("-fx-background-color: #1db954; -fx-text-fill: white; -fx-font-weight: bold;");
        addButton.setOnAction(e -> {
            FormMusicScreen formScreen = new FormMusicScreen(stage, null);
            formScreen.show();
        });

        root.getChildren().addAll(
                backContainer,
                lbl,
                addButton,
                this.showMusicCards()
        );

        this.scene = new Scene(root, 600, 600);
    }

    private ScrollPane showMusicCards() {
        FlowPane cardsContainer = new FlowPane();
        cardsContainer.setHgap(15);
        cardsContainer.setVgap(15);
        cardsContainer.setPadding(new Insets(10));
        cardsContainer.setPrefWidth(480);
        cardsContainer.setAlignment(Pos.CENTER);
        cardsContainer.setStyle("-fx-background-color: #212121;");

        ArrayList<Music> musics = ManageMusic.readFile();

        for (Music music : musics) {
            cardsContainer.getChildren().add(createMusicCard(music));
        }

        return getScrollPane(cardsContainer);
    }

    private static ScrollPane getScrollPane(FlowPane cardsContainer) {
        ScrollPane scrollPane = new ScrollPane(cardsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background-insets: 0;" +
                        "-fx-padding: 0;" +
                        "-fx-border-color: transparent;" +
                        "-fx-border-width: 0;"
        );
        return scrollPane;
    }

    private VBox createMusicCard(Music music) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(10));
        card.setPrefSize(180, 300);
        card.setStyle(
                "-fx-background-color: #2e2e2e; " +
                        "-fx-background-radius: 10; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 8, 0.5, 0, 2);"
        );

        ImageView imageView = new ImageView();
        try {
            Image image;
            if (music.getCover() != null && !music.getCover().isEmpty()) {
                if (music.getCover().startsWith("http") || music.getCover().startsWith("file:/")) {
                    image = new Image(music.getCover(), false);
                } else {
                    image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(music.getCover())));
                }
                if (image.isError()) throw new Exception("Erro ao carregar imagem");
            } else {
                throw new Exception("Caminho de imagem vazio");
            }
            imageView.setImage(image);
        } catch (Exception e) {
            Image defaultImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/no_image.png")));
            imageView.setImage(defaultImage);
        }

        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        Label name = new Label(music.getName());
        name.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        name.setWrapText(true);

        Label artist = new Label(music.getArtist());
        artist.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");
        artist.setWrapText(true);

        Label album = new Label("Álbum: " + music.getAlbum());
        album.setStyle("-fx-text-fill: gray; -fx-font-size: 12px;");

        Label duration = new Label("Duração: " + music.getDuration());
        duration.setStyle("-fx-text-fill: gray; -fx-font-size: 12px;");

        String date = new SimpleDateFormat("dd/MM/yyyy").format(music.getLaunchDate());
        Label launch = new Label("Lançamento: " + date);
        launch.setStyle("-fx-text-fill: gray; -fx-font-size: 12px;");

        Button editButton = new Button("Editar");
        editButton.setStyle(
                "-fx-background-color: #b8a52a; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 5; " +
                        "-fx-font-weight: bold;"
        );
        editButton.setOnAction(e -> {
            FormMusicScreen editScreen = new FormMusicScreen(stage, music);
            editScreen.show();
        });

        Button deleteButton = new Button("Excluir");
        deleteButton.setStyle(
                "-fx-background-color: #b34747; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 5; " +
                        "-fx-font-weight: bold;"
        );
        deleteButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação");
            alert.setHeaderText("Deseja realmente excluir?");
            alert.setContentText("Essa ação não pode ser desfeita.");

            ButtonType yesButton = new ButtonType("Sim", ButtonBar.ButtonData.OK_DONE);
            ButtonType noButton = new ButtonType("Não", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(yesButton, noButton);

            alert.showAndWait().ifPresent(response -> {
                if (response == yesButton) {
                    ManageMusic.removeMusic(music.getName(), music.getArtist());
                    showAlert("Sucesso", "Música excluída com sucesso.", Alert.AlertType.INFORMATION);
                    refreshScreen();
                }
            });
        });

        HBox buttons = new HBox(10, editButton, deleteButton);
        buttons.setAlignment(Pos.CENTER);

        card.getChildren().addAll(imageView, name, artist, album, duration, launch, buttons);

        return card;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void refreshScreen() {
        MusicScreen musicScreen = new MusicScreen(stage);
        musicScreen.show();
    }


}
