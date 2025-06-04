package Album;

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

public class AlbumScreen {
    Scene scene;
    Stage stage;

    public AlbumScreen(Stage stage) {
        this.stage = stage;
    }

    public void show() { // Este método deve ser público
        this.createUI();
        this.stage.setScene(this.scene);
    }

    private void createUI() {
        stage.setTitle("Hear Me Out - Álbuns");

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

        Label lbl = new Label("Álbuns");
        lbl.setStyle("-fx-text-fill: white; -fx-font-size: 30px; -fx-font-weight: bold;");

        Button addButton = new Button("Novo álbum");
        addButton.setStyle("-fx-background-color: #1db954; -fx-text-fill: white; -fx-font-weight: bold;");
        addButton.setOnAction(e -> {
            FormAlbumScreen formScreen = new FormAlbumScreen(stage, null);
            formScreen.show();
        });

        root.getChildren().addAll(
                backContainer,
                lbl,
                addButton,
                this.showAlbumCards()
        );

        this.scene = new Scene(root, 600, 600);
    }

    private ScrollPane showAlbumCards() {
        FlowPane cardsContainer = new FlowPane();
        cardsContainer.setHgap(15);
        cardsContainer.setVgap(15);
        cardsContainer.setPadding(new Insets(10));
        cardsContainer.setPrefWidth(480);
        cardsContainer.setAlignment(Pos.CENTER);
        cardsContainer.setStyle("-fx-background-color: #212121;");

        ArrayList<Album> albums = ManageAlbum.readFile();

        for (Album album : albums) {
            cardsContainer.getChildren().add(createAlbumCard(album));
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

    private VBox createAlbumCard(Album album) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(10));
        card.setPrefSize(180, 250);
        card.setStyle(
                "-fx-background-color: #2e2e2e; " +
                        "-fx-background-radius: 10; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 8, 0.5, 0, 2);"
        );

        ImageView imageView = new ImageView();
        try {
            Image image;
            if (album.getCover() != null && !album.getCover().isEmpty()) {
                if (album.getCover().startsWith("http") || album.getCover().startsWith("file:/")) {
                    image = new Image(album.getCover(), false);
                } else {
                    image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(album.getCover())));
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

        Label name = new Label(album.getName());
        name.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        name.setWrapText(true);

        Label artist = new Label(album.getArtist());
        artist.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");
        artist.setWrapText(true);

        String date = new SimpleDateFormat("dd/MM/yyyy").format(album.getLaunchDate());
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
            FormAlbumScreen editScreen = new FormAlbumScreen(stage, album);
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
                    ManageAlbum.removeAlbum(album.getName(), album.getArtist());
                    showAlert("Sucesso", "Álbum excluído com sucesso.", Alert.AlertType.INFORMATION);
                    refreshScreen();
                }
            });
        });

        HBox buttons = new HBox(10, editButton, deleteButton);
        buttons.setAlignment(Pos.CENTER);

        card.getChildren().addAll(imageView, name, artist, launch, buttons);

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
        AlbumScreen albumScreen = new AlbumScreen(stage);
        albumScreen.show();
    }
}