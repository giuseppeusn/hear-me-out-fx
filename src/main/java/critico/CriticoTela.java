package critico; // Pertence ao pacote 'critico'.

import com.hmo_fx.hear_me_out_fx.Main; // Importa a classe Main para navegação de volta.
// Imports JavaFX para UI.
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.text.SimpleDateFormat; // Para formatar datas.
import java.util.ArrayList;       // Para a lista de críticos.

public class CriticoTela { // Declaração da classe da tela de listagem de críticos.
    Scene scene; // A cena desta tela.
    Stage stage; // O Stage principal.

    // Construtor.
    public CriticoTela(Stage stage) {
        this.stage = stage; // Armazena o Stage.
    }

    // Método para configurar a UI e definir a cena no Stage.
    public void show() {
        this.createUI(); // Chama o método que constrói a interface.
        this.stage.setScene(this.scene); // Define a cena construída no Stage.
    }

    // Método privado que constrói a interface do usuário.
    private void createUI() {
        stage.setTitle("Hear Me Out - Críticos"); // Define o título da janela.

        VBox root = new VBox(20); // Painel raiz vertical com espaçamento 20.
        root.setStyle("-fx-background-color: #212121;"); // Fundo escuro.
        root.setAlignment(Pos.TOP_CENTER); // Alinhamento dos filhos.
        root.setPadding(new Insets(20)); // Padding.

        // Botão Voltar.
        Button backButton = new Button("Voltar");
        backButton.setStyle("-fx-background-color: #2e2e2e; -fx-text-fill: white; -fx-font-weight: bold;");
        backButton.setOnAction(e -> { // Ação ao clicar.
            Main main = new Main(); // Cria uma instância de Main.
            main.showMainScreen(stage); // Chama o método para mostrar a tela principal.
        });

        HBox backContainer = new HBox(backButton); // Container para o botão Voltar.
        backContainer.setAlignment(Pos.CENTER_LEFT); // Alinhamento.
        backContainer.setMaxWidth(Double.MAX_VALUE); // Ocupa largura máxima.

        // Título da Tela ("Críticos").
        Label lbl = new Label("Críticos");
        lbl.setStyle("-fx-text-fill: white; -fx-font-size: 30px; -fx-font-weight: bold;");

        // Botão "Novo Crítico".
        Button addButton = new Button("Novo Crítico");
        addButton.setStyle("-fx-background-color: #1db954; -fx-text-fill: white; -fx-font-weight: bold;");
        addButton.setOnAction(e -> { // Ação ao clicar.
            FormularioCritico formScreen = new FormularioCritico(stage, null); // Cria a tela de formulário para um novo crítico (null).
            formScreen.show(); // Mostra a tela de formulário.
        });

        // ScrollPane que conterá os cards dos críticos.
        ScrollPane scrollPane = this.showCriticCards(); // Chama o método que cria e retorna o ScrollPane com os cards.
        VBox.setVgrow(scrollPane, javafx.scene.layout.Priority.ALWAYS); // Faz o ScrollPane crescer verticalmente para ocupar espaço.
        scrollPane.setFitToHeight(true); // Tenta fazer o conteúdo do ScrollPane (FlowPane) preencher a altura do viewport.
        // Isso, combinado com o Vgrow e o fundo do ScrollPane/FlowPane, deve ajudar com a barra branca.
        // E também foi ajustado o fundo do ScrollPane em getScrollPane().

        // Adiciona todos os componentes ao painel raiz 'root'.
        root.getChildren().addAll(
                backContainer,
                lbl,
                addButton,
                scrollPane // Adiciona a instância de ScrollPane que foi configurada.
        );

        this.scene = new Scene(root, 600, 600); // Cria a cena.
    }

    // Método que cria o ScrollPane com o FlowPane de cards.
    private ScrollPane showCriticCards() {
        FlowPane cardsContainer = new FlowPane(); // Painel que organiza os cards em um fluxo.
            cardsContainer.setHgap(15); // Espaçamento horizontal entre cards.
            cardsContainer.setVgap(15); // Espaçamento vertical entre cards.
            cardsContainer.setPadding(new Insets(10)); // Padding interno do FlowPane.
            cardsContainer.setPrefWidth(560); // Largura preferida para o FlowPane (ajuda no cálculo de quebra de linha dos cards).
            cardsContainer.setAlignment(Pos.CENTER); // Alinha os cards ao centro do FlowPane.
            cardsContainer.setStyle("-fx-background-color: #212121;"); // Fundo escuro para o FlowPane.

        ArrayList<Critico> criticos = ManageCritico.readFile(); // Lê a lista de críticos.

        if (criticos.isEmpty()) { // Se não houver críticos...
            Label noCriticsLabel = new Label("Nenhum crítico cadastrado."); // Mostra uma mensagem.
            noCriticsLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
            cardsContainer.getChildren().add(noCriticsLabel);
            cardsContainer.setMinHeight(200); // Opcional: define altura mínima se vazio.
        } else { // Se houver críticos...
            for (Critico critico : criticos) { // Itera pela lista.
                cardsContainer.getChildren().add(createCriticCard(critico)); // Cria um card para cada crítico e adiciona ao FlowPane.
            }
        }
        return getScrollPane(cardsContainer); // Retorna um ScrollPane contendo o FlowPane.
    }

    // Método estático utilitário para criar e configurar o ScrollPane.
    private static ScrollPane getScrollPane(FlowPane cardsContainer) {
        ScrollPane scrollPane = new ScrollPane(cardsContainer); // Cria o ScrollPane com o FlowPane como conteúdo.
        scrollPane.setFitToWidth(true); // Faz o conteúdo (FlowPane) se ajustar à largura do ScrollPane.

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Nunca mostra a barra de rolagem horizontal.
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Mostra a barra de rolagem vertical apenas quando necessário.

        // Estilo para o ScrollPane, garantindo que seja transparente ou tenha o fundo desejado.
        scrollPane.setStyle(
                "-fx-background-color: #212121;" +   // Fundo escuro explícito para o ScrollPane.
                        "-fx-background: #212121;" +         // Redundante com o anterior, mas garante.
                        "-fx-padding: 0;" +
                        "-fx-border-color: transparent;" +
                        "-fx-border-width: 0;"
        );
        return scrollPane;
    }

    // Método para criar o VBox (card) para um único crítico.
    private VBox createCriticCard(Critico critico) {
        VBox card = new VBox(10); // VBox para o card, com espaçamento 10.
        card.setAlignment(Pos.TOP_LEFT); // Alinhamento do conteúdo do card.
        card.setPadding(new Insets(15)); // Padding interno do card.
        card.setPrefSize(250, 320); // Tamanho preferido do card (altura aumentada para CPF e Bio).

        // Estilo do card (fundo, bordas arredondadas, sombra).
        card.setStyle(
                "-fx-background-color: #2e2e2e; " +
                        "-fx-background-radius: 10; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 8, 0.5, 0, 2);"
        );

        // Criação dos Labels para cada informação do crítico.
        Label name = new Label(critico.getNome());
        name.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
        name.setWrapText(true); // Permite quebra de linha se o nome for muito longo.

        Label cpfLabel = new Label("CPF: " + (critico.getCpf() != null ? critico.getCpf() : "N/A"));
        cpfLabel.setStyle("-fx-text-fill: #b0b0b0; -fx-font-size: 12px;");
        cpfLabel.setWrapText(true);

        Label email = new Label("Email: " + critico.getEmail());
        email.setStyle("-fx-text-fill: #b0b0b0; -fx-font-size: 12px;");
        email.setWrapText(true);

        Label site = new Label("Site: " + (critico.getSite() != null && !critico.getSite().isEmpty() ? critico.getSite() : "N/A"));
        site.setStyle("-fx-text-fill: #b0b0b0; -fx-font-size: 12px;");
        site.setWrapText(true);

        Label genero = new Label("Gênero: " + critico.getGenero());
        genero.setStyle("-fx-text-fill: #b0b0b0; -fx-font-size: 12px;");
        genero.setWrapText(true);

        String birthDateStr = "Nasc.: ";
        if (critico.getData_nascimento() != null) {
            birthDateStr += new SimpleDateFormat("dd/MM/yyyy").format(critico.getData_nascimento());
        } else {
            birthDateStr += "N/A";
        }
        Label birthDate = new Label(birthDateStr);
        birthDate.setStyle("-fx-text-fill: #b0b0b0; -fx-font-size: 12px;");

        // Label para Biografia, com truncamento para não ocupar muito espaço no card.
        Label biografiaLabel = new Label("Bio: " + (critico.getBiografia() != null && !critico.getBiografia().isEmpty() ?
                (critico.getBiografia().length() > 50 ? critico.getBiografia().substring(0, 47) + "..." : critico.getBiografia()) // Trunca se > 50 chars.
                : "N/A"));
        biografiaLabel.setStyle("-fx-text-fill: #b0b0b0; -fx-font-size: 12px;");
        biografiaLabel.setWrapText(true);

        // Botões de Ação (Editar, Excluir).
        Button editButton = new Button("Editar");
        editButton.setStyle(
                "-fx-background-color: #b8a52a; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 5; " +
                        "-fx-font-weight: bold;"
        );
        editButton.setOnAction(e -> { // Ação do botão Editar.
            FormularioCritico editScreen = new FormularioCritico(stage, critico); // Abre o formulário em modo de edição.
            editScreen.show();
        });

        Button deleteButton = new Button("Excluir");
        deleteButton.setStyle(
                "-fx-background-color: #b34747; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 5; " +
                        "-fx-font-weight: bold;"
        );
        deleteButton.setOnAction(e -> { // Ação do botão Excluir.
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION); // Cria um alerta de confirmação.
            alert.setTitle("Confirmação de Exclusão");
            alert.setHeaderText("Deseja realmente excluir o crítico \"" + critico.getNome() + "\"?");
            alert.setContentText("Esta ação não pode ser desfeita.");

            ButtonType yesButton = new ButtonType("Sim", ButtonBar.ButtonData.OK_DONE); // Botão Sim.
            ButtonType noButton = new ButtonType("Não", ButtonBar.ButtonData.CANCEL_CLOSE); // Botão Não.
            alert.getButtonTypes().setAll(yesButton, noButton); // Adiciona os botões ao alerta.

            alert.showAndWait().ifPresent(response -> { // Mostra o alerta e processa a resposta.
                if (response == yesButton) { // Se o usuário clicou Sim...
                    ManageCritico.removeCritic(critico.getCpf()); // Remove o crítico.
                    showAlert("Sucesso", "Crítico excluído com sucesso.", Alert.AlertType.INFORMATION); // Mostra alerta de sucesso.
                    refreshScreen(); // Atualiza a tela para remover o card do crítico excluído.
                }
            });
        });

        HBox buttons = new HBox(10, editButton, deleteButton); // Container para os botões.
        buttons.setAlignment(Pos.CENTER); // Alinhamento dos botões.

        Region spacer = new Region(); // Um espaçador para empurrar os botões para baixo no card.
        VBox.setVgrow(spacer, Priority.ALWAYS); // Faz o spacer ocupar todo o espaço vertical disponível.

        // Adiciona todos os componentes ao card na ordem desejada.
        card.getChildren().addAll(name, cpfLabel, email, site, genero, birthDate, biografiaLabel, spacer, buttons);

        return card; // Retorna o VBox do card pronto.
    }

    // Método utilitário para mostrar alertas.
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Método para recarregar/atualizar a tela.
    private void refreshScreen() {
        CriticoTela criticoTela = new CriticoTela(stage); // Cria uma nova instância da tela.
        criticoTela.show(); // Mostra a nova instância (recarregando os dados).
    }
}