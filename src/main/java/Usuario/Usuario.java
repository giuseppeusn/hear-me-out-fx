package Usuario;

import com.hmo_fx.hear_me_out_fx.Main;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;

public class Usuario {
    Scene scene;
    Stage stage;

    public Usuario(Stage stage) {
        this.stage = stage;
    }

    public void showUser() {
        this.createMainUserScreen();
        this.stage.setScene(this.scene);
        this.stage.show();
    }

    private void createMainUserScreen() {
        stage.setTitle("Hear Me Out - Usuários");

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

        Label lbl = new Label("Usuários Cadastrados");
        lbl.setStyle("-fx-text-fill: white; -fx-font-size: 30px; -fx-font-weight: bold;");

        Button addButton = new Button("Novo Usuário");
        addButton.setStyle("-fx-background-color: #1db954; -fx-text-fill: white; -fx-font-weight: bold;");
        addButton.setOnAction(e -> {
            FormUsuario formUser = new FormUsuario(stage, null);
            formUser.show();
        });

        ScrollPane userCardsScrollPane = showUserCards();

        root.getChildren().addAll(
                backContainer,
                lbl,
                addButton,
                userCardsScrollPane
        );

        this.scene = new Scene(root, 600, 600);
    }

    private ScrollPane showUserCards() {
        FlowPane cardsContainer = new FlowPane();
        cardsContainer.setHgap(15);
        cardsContainer.setVgap(15);
        cardsContainer.setPadding(new Insets(10));
        cardsContainer.setPrefWidth(480);
        cardsContainer.setAlignment(Pos.CENTER);
        cardsContainer.setStyle("-fx-background-color: #212121;");

        ArrayList<usuarioPrincipal> users = ManagerUsuario.readFile();

        if (users.isEmpty()) {
            Label noUsersLabel = new Label("Nenhum usuário cadastrado.");
            noUsersLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 16px;");
            cardsContainer.getChildren().add(noUsersLabel);
        } else {
            for (usuarioPrincipal user : users) {
                cardsContainer.getChildren().add(createUserCard(user));
            }
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

    private VBox createUserCard(usuarioPrincipal usuario) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(10));
        card.setPrefSize(180, 160);
        card.setStyle(
                "-fx-background-color: #2e2e2e; " +
                        "-fx-background-radius: 10; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 8, 0.5, 0, 2);"
        );

        Label nameLabel = new Label(usuario.getNome());
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        nameLabel.setWrapText(true);

        Label emailLabel = new Label(usuario.getEmail());
        emailLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 12px;");
        emailLabel.setWrapText(true);

        Label cpfLabel = new Label("CPF: " + usuario.getCpf());
        cpfLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 12px;");

        Label phoneLabel = new Label("Tel: " + usuario.getTelefone());
        phoneLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 12px;");

        Button editButton = new Button("Editar");
        editButton.setStyle(
                "-fx-background-color: #b8a52a; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 5; " +
                        "-fx-font-weight: bold;"
        );
        editButton.setOnAction(e -> {
            FormUsuario editScreen = new FormUsuario(stage, usuario);
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
            alert.setTitle("Confirmação de Exclusão");
            alert.setHeaderText("Deseja realmente excluir o usuário " + usuario.getNome() + "?");
            alert.setContentText("Essa ação não pode ser desfeita.");

            ButtonType yesButton = new ButtonType("Sim", ButtonBar.ButtonData.OK_DONE);
            ButtonType noButton = new ButtonType("Não", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(yesButton, noButton);

            alert.showAndWait().ifPresent(response -> {
                if (response == yesButton) {
                    ManagerUsuario.removeUsuario(usuario.getEmail());
                    showAlert("Sucesso", "Usuário excluído com sucesso.", Alert.AlertType.INFORMATION);
                    refreshUserScreen();
                }
            });
        });

        HBox buttons = new HBox(10, editButton, deleteButton);
        buttons.setAlignment(Pos.CENTER);

        card.getChildren().addAll(nameLabel, emailLabel, cpfLabel, phoneLabel, buttons);

        return card;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void refreshUserScreen() {
        this.showUser();
    }
}