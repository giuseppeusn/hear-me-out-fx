package Usuario; // Certifique-se de que o pacote está correto

// Imports necessários para a tela de listagem de USUÁRIOS
import com.hmo_fx.hear_me_out_fx.Main; // Para voltar à tela principal

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image; // Para ImageView
import javafx.scene.image.ImageView; // Para cards de usuário
import javafx.scene.layout.FlowPane; // Para cards de usuário
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList; // Para a lista de usuários
import java.util.Objects; // Para Objects.requireNonNull

// REMOVIDOS: music.FormMusicScreen, music.ManageMusic, music.Music, music.MusicScreen, SimpleDateFormat
//            Esses imports não pertencem à tela de listagem de USUÁRIOS.

public class Usuario { // Esta classe agora é a tela de listagem de USUÁRIOS
    Scene scene;
    Stage stage;

    public Usuario(Stage stage) {
        this.stage = stage;
    }

    public void showUser() { // Renomeado de criarsuario para ser mais específico
        this.createMainUserScreen(); // Cria a tela principal de listagem de usuários
        this.stage.setScene(this.scene);
        this.stage.show();
    }

    private void createMainUserScreen() { // Renomeado de criarsuario
        stage.setTitle("Hear Me Out - Usuários"); // Título ajustado para Usuários

        VBox root = new VBox(20);
        root.setStyle("-fx-background-color: #212121;");
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));

        Button backButton = new Button("Voltar");
        backButton.setStyle("-fx-background-color: #2e2e2e; -fx-text-fill: white; -fx-font-weight: bold;");
        backButton.setOnAction(e -> {
            Main main = new Main();
            main.showMainScreen(stage); // Voltar para a tela principal (Main)
        });

        HBox backContainer = new HBox(backButton);
        backContainer.setAlignment(Pos.CENTER_LEFT);
        backContainer.setMaxWidth(Double.MAX_VALUE);

        Label lbl = new Label("Usuários Cadastrados"); // Label ajustado para Usuários
        lbl.setStyle("-fx-text-fill: white; -fx-font-size: 30px; -fx-font-weight: bold;");

        Button addButton = new Button("Novo Usuário"); // Texto do botão ajustado
        addButton.setStyle("-fx-background-color: #1db954; -fx-text-fill: white; -fx-font-weight: bold;");
        addButton.setOnAction(e -> {
            // Chama o FormUsuario para criar um novo usuário (passando null para edição)
            FormUsuario formUser = new FormUsuario(stage, null); // CORRIGIDO: O construtor correto
            formUser.show();
        });

        // --- Conteúdo da listagem de usuários ---
        ScrollPane userCardsScrollPane = showUserCards(); // Método para mostrar cards de usuário

        root.getChildren().addAll(
                backContainer,
                lbl,
                addButton,
                userCardsScrollPane // Adiciona o ScrollPane com os cards de usuário
        );

        this.scene = new Scene(root, 600, 600); // Tamanho da cena
    }

    // --- NOVO MÉTODO: showUserCards (equivalente ao showMusicCards, mas para usuários) ---
    private ScrollPane showUserCards() {
        FlowPane cardsContainer = new FlowPane();
        cardsContainer.setHgap(15);
        cardsContainer.setVgap(15);
        cardsContainer.setPadding(new Insets(10));
        cardsContainer.setPrefWidth(480);
        cardsContainer.setAlignment(Pos.CENTER);
        cardsContainer.setStyle("-fx-background-color: #212121;");

        // LÊ OS USUÁRIOS DO ARQUIVO (usando ManagerUsuario)
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

    // --- Método auxiliar para o ScrollPane (mantido, pois é genérico) ---
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

    // --- Método createUserCard (CORRIGIDO para exibir dados de USUÁRIO) ---
    private VBox createUserCard(usuarioPrincipal usuario) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(10));
        card.setPrefSize(180, 280); // Ajustei o tamanho para caber os labels
        card.setStyle(
                "-fx-background-color: #2e2e2e; " +
                        "-fx-background-radius: 10; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 8, 0.5, 0, 2);"
        );

        // --- Imagem (adaptada para usuários. Usa padrão se não houver capa) ---
        ImageView imageView = new ImageView();
        try {
            // Assumindo que usuarioPrincipal NÃO TEM getCover() e usa imagem padrão para usuário
            // Se houver uma imagem específica para usuário, você pode implementar usuario.getCover()
            // e adaptar a lógica abaixo. Por agora, usa uma imagem padrão de usuário.
            Image userDefaultImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/default_user.png"))); // Crie essa imagem!
            imageView.setImage(userDefaultImage);

        } catch (Exception e) {
            // Em caso de qualquer erro (ex: /img/default_user.png não encontrada), usa um fallback
            Image fallbackImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/no_image.png"))); // Imagem de erro genérica
            imageView.setImage(fallbackImage);
            System.err.println("Erro ao carregar imagem de usuário: " + e.getMessage());
        }
        imageView.setFitWidth(120);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);

        // --- Labels de Informação do Usuário (Corrigidos) ---
        // As labels usam os getters CORRETOS de usuarioPrincipal
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

        // --- Botões de Ação (Editar, Excluir) ---
        Button editButton = new Button("Editar");
        editButton.setStyle(
                "-fx-background-color: #b8a52a; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 5; " +
                        "-fx-font-weight: bold;"
        );
        editButton.setOnAction(e -> {
            // Chama o FormUsuario para edição, passando o usuário a ser editado
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
                    // Lógica de Exclusão de USUÁRIO (usando ManagerUsuario.removeUsuario)
                    ManagerUsuario.removeUsuario(usuario.getEmail()); // Removendo por email
                    showAlert("Sucesso", "Usuário excluído com sucesso.", Alert.AlertType.INFORMATION);
                    refreshUserScreen(); // Atualiza a tela de listagem de usuários
                }
            });
        });

        HBox buttons = new HBox(10, editButton, deleteButton);
        buttons.setAlignment(Pos.CENTER);

        // Adiciona os elementos ao card (CORRIGIDO: usando as Labels corretas)
        card.getChildren().addAll(imageView, nameLabel, emailLabel, cpfLabel, phoneLabel, buttons);

        return card;
    }

    // --- Métodos de alerta e refresh (ajustados para usuários) ---
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void refreshUserScreen() { // Renomeado de refreshScreen
        this.showUser(); // Recria e exibe a tela de usuários para refletir as mudanças
    }
}