package critico; // Pertence ao pacote 'critico'.

// Imports para componentes JavaFX e outras utilidades.
import javafx.geometry.Insets; // Para definir preenchimento (padding).
import javafx.geometry.Pos;   // Para definir alinhamento de componentes.
import javafx.scene.Node;     // Classe base para todos os elementos visuais em JavaFX (não está sendo usada diretamente no seu código, mas DatePicker é um Node).
import javafx.scene.Scene;    // Representa o conteúdo de uma janela (Stage).
import javafx.scene.control.*; // Contém classes para controles de UI como Button, Label, TextField, DatePicker, Alert.
import javafx.scene.layout.HBox;  // Painel que organiza os filhos horizontalmente.
import javafx.scene.layout.VBox;  // Painel que organiza os filhos verticalmente.
import javafx.stage.Stage;    // A janela principal da aplicação JavaFX.

// Imports para manipulação de datas e listas.
// import java.text.ParseException; // Não está sendo usado diretamente, pois DatePicker lida com o parse.
import java.text.SimpleDateFormat; // Para formatar Datas para String (ex: para exibir).
import java.time.LocalDate;       // Para trabalhar com datas (sem hora/fuso) de forma moderna, usado pelo DatePicker.
import java.time.ZoneId;        // Para lidar com fusos horários na conversão entre Date e LocalDate.
import java.util.ArrayList;     // Para a lista 'textFields' (que não está sendo usada corretamente na sua validação).
import java.util.Date;          // Classe legada para datas, usada no seu modelo 'Critico'.
import java.util.List;          // Interface para listas, usada por 'textFields'.

public class FormularioCritico { // Declaração da classe do formulário.
    private Scene scene; // Cena para este formulário.
    private final Stage stage; // A janela (Stage) principal onde esta cena será mostrada. 'final' porque é atribuída no construtor e não muda.
    private final Critico criticoToEdit; // Armazena o objeto 'Critico' que está sendo editado. Se for null, significa que estamos adicionando um novo crítico.

    // Campos relacionados à seleção de imagem (parecem ser resquícios do FormMusicScreen e não são usados para Critico).
    private String coverPath = ""; // Não usado para Critico.
    private final List<TextField> textFields = new ArrayList<>(); // Esta lista é declarada mas não é populada/usada corretamente no seu método validateFields.
    private Label coverLabel; // Não usado para Critico.

    // Campos de entrada do formulário.
    private TextField txtNome;
    private TextField txtCpf;
    private TextField txtEmail;
    private DatePicker dpDataNascimento; // Controle específico para seleção de datas.
    private TextField txtBio; // Você está usando TextField para biografia. Um TextArea seria mais apropriado para textos longos.
    private TextField txtSite;
    private TextField txtGenero;

    // Construtor.
    public FormularioCritico(Stage stage, Critico criticoToEdit) {
        this.stage = stage; // Armazena o Stage recebido.
        this.criticoToEdit = criticoToEdit; // Armazena o Critico para edição (ou null se for novo).
    }

    // Método para configurar e exibir a UI do formulário.
    public void show() {
        VBox root = new VBox(15); // Painel raiz vertical, com espaçamento de 15 pixels entre os filhos.
        root.setStyle("-fx-background-color: #212121;"); // Define a cor de fundo.
        root.setAlignment(Pos.TOP_CENTER); // Alinha os filhos no topo e centro do VBox.
        root.setPadding(new Insets(20)); // Define um preenchimento de 20 pixels ao redor do conteúdo do VBox.

        HBox backContainer = createBackButton(); // Cria o HBox com o botão "Voltar".

        // Define o título da tela ("Adicionar Crítico" ou "Editar Crítico").
        Label title = new Label(criticoToEdit == null ? "Adicionar Crítico" : "Editar Crítico");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;"); // Estiliza o título.

        VBox formContainer = createForm(); // Cria o VBox que contém todos os campos do formulário.

        // Cria o botão Salvar/Atualizar.
        Button saveButton = new Button(criticoToEdit == null ? "Salvar" : "Atualizar");
        saveButton.setStyle("-fx-background-color: #1db954; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;"); // Estiliza o botão.
        saveButton.setOnAction(e -> saveCritic()); // Define a ação a ser executada quando o botão é clicado (chama o método saveCritic).

        // Cria um ScrollPane para envolver o formContainer, permitindo rolagem se o formulário for muito grande.
        ScrollPane scrollPane = new ScrollPane(formContainer);
        scrollPane.setFitToWidth(true); // Faz o conteúdo do ScrollPane (formContainer) se ajustar à largura do ScrollPane.
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: #212121;"); // Estilo para o ScrollPane.
        VBox.setVgrow(scrollPane, javafx.scene.layout.Priority.ALWAYS); // Permite que o ScrollPane cresça verticalmente para ocupar o espaço disponível no VBox 'root'.

        // Adiciona todos os componentes ao painel raiz 'root'.
        root.getChildren().addAll(
                backContainer,
                title,
                scrollPane, // Adiciona o ScrollPane (que contém o formContainer).
                saveButton
        );

        this.scene = new Scene(root, 600, 700); // Cria a cena com o painel 'root' e dimensões especificadas.
        this.stage.setScene(this.scene); // Define esta cena no Stage principal.
    }

    // Método privado para criar o HBox com o botão "Voltar".
    private HBox createBackButton() {
        Button backButton = new Button("Voltar");
        backButton.setStyle("-fx-background-color: #2e2e2e; -fx-text-fill: white; -fx-font-weight: bold;");
        backButton.setOnAction(e -> new CriticoTela(stage).show()); // Ao clicar, cria e mostra a tela CriticoTela.

        HBox container = new HBox(backButton); // Coloca o botão em um HBox.
        container.setAlignment(Pos.CENTER_LEFT); // Alinha o conteúdo do HBox à esquerda.
        container.setMaxWidth(Double.MAX_VALUE); // Permite que o HBox ocupe toda a largura disponível (útil para alinhamento).
        return container;
    }

    // Método privado para criar o VBox com os campos do formulário.
    private VBox createForm() {
        VBox container = new VBox(10); // VBox para os campos, com espaçamento 10.
        container.setAlignment(Pos.CENTER_LEFT); // Alinha os campos à esquerda.
        container.setPadding(new Insets(0, 20, 0, 20)); // Padding lateral.

        // Campo Nome
        Label lblNome = createLabel("Nome Completo:"); // Cria o rótulo.
        txtNome = createTextField("Digite o nome do crítico"); // Cria o campo de texto.
        container.getChildren().addAll(lblNome, txtNome); // Adiciona rótulo e campo ao container.

        // Campo CPF
        Label lblCpf = createLabel("CPF:");
        txtCpf = createTextField("Digite o CPF (apenas números)");
        if (criticoToEdit != null) { // Se estiver editando um crítico existente...
            txtCpf.setEditable(false); // Torna o campo CPF não editável.
            txtCpf.setStyle(txtCpf.getStyle() + "-fx-control-inner-background: #404040;"); // Muda o estilo para indicar que não é editável.
        }
        container.getChildren().addAll(lblCpf, txtCpf);

        // Campo Email
        Label lblEmail = createLabel("Email:");
        txtEmail = createTextField("Digite o email");
        container.getChildren().addAll(lblEmail, txtEmail);

        // Campo Data de Nascimento
        Label lblDataNascimento = createLabel("Data de Nascimento:");
        dpDataNascimento = new DatePicker(); // Cria o DatePicker.
        dpDataNascimento.setPromptText("DD/MM/AAAA"); // Texto de placeholder.
        dpDataNascimento.setMaxWidth(Double.MAX_VALUE); // Faz o DatePicker ocupar a largura disponível.
        // Estilo para o DatePicker.
        dpDataNascimento.setStyle(
                "-fx-padding: 4 8;" +
                        "-fx-background-radius: 5;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-color: #2e2e2e;" +
                        "-fx-text-fill: white;"
        );
        // Estilo para o editor de texto dentro do DatePicker.
        dpDataNascimento.getEditor().setStyle("-fx-text-fill: white; -fx-background-color: #2e2e2e;");
        container.getChildren().addAll(lblDataNascimento, dpDataNascimento);

        // Campo Biografia (usando TextField txtBio)
        Label lblBiografia = createLabel("Biografia:");
        txtBio = createTextField("Descreva o crítico..."); // Se precisar de múltiplas linhas, use TextArea.
        container.getChildren().addAll(lblBiografia, txtBio);

        // Campo Site
        Label lblSite = createLabel("Site/Blog (opcional):");
        txtSite = createTextField("http://exemplo.com");
        container.getChildren().addAll(lblSite, txtSite);

        // Campo Gênero (Anteriormente "Gênero Principal de Atuação", agora só "Gênero")
        Label lblGenero = createLabel("Gênero:"); // Texto do label corrigido.
        txtGenero = createTextField("Ex: Masculino, feminino ou indefinido.");
        container.getChildren().addAll(lblGenero, txtGenero);

        if (criticoToEdit != null) { // Se estiver editando...
            fillFields(); // Preenche os campos com os dados do crítico existente.
        }
        return container; // Retorna o VBox com todos os campos.
    }

    // Método utilitário para criar Labels estilizados.
    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER_LEFT);
        return label;
    }

    // Método utilitário para criar TextFields estilizados.
    private TextField createTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle(
                "-fx-padding: 8 12;" +
                        "-fx-background-radius: 5;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-color: #2e2e2e;" +
                        "-fx-text-fill: white;"
        );
        return field;
    }

    // Método para preencher os campos do formulário quando estiver editando.
    private void fillFields() {
        txtNome.setText(criticoToEdit.getNome());
        txtCpf.setText(criticoToEdit.getCpf());
        txtEmail.setText(criticoToEdit.getEmail());

        if (criticoToEdit.getData_nascimento() != null) { // Verifica se a data de nascimento não é nula.
            // Converte java.util.Date para java.time.LocalDate para o DatePicker.
            dpDataNascimento.setValue(criticoToEdit.getData_nascimento().toInstant()
                    .atZone(ZoneId.systemDefault()) // Usa o fuso horário padrão do sistema.
                    .toLocalDate());
        }
        txtBio.setText(criticoToEdit.getBiografia());
        txtSite.setText(criticoToEdit.getSite());
        txtGenero.setText(criticoToEdit.getGenero());
    }

    // Método chamado quando o botão Salvar/Atualizar é clicado.
    private void saveCritic() {
        if (!validateFields()) return; // Se a validação falhar, não continua.

        try { // Bloco try-catch para tratar exceções durante a criação/atualização do objeto.
            // Coleta os dados dos campos do formulário.
            String nome = txtNome.getText();
            String cpf = txtCpf.getText(); // CPF não é editável, então este será o CPF original se estiver editando.
            String email = txtEmail.getText();
            LocalDate localDateNascimento = dpDataNascimento.getValue(); // Pega LocalDate do DatePicker.
            Date dataNascimento = null; // Inicializa como null.
            if (localDateNascimento != null) { // Se uma data foi selecionada...
                // Converte LocalDate para java.util.Date.
                dataNascimento = Date.from(localDateNascimento.atStartOfDay(ZoneId.systemDefault()).toInstant());
            }

            String biografia = txtBio.getText();
            String site = txtSite.getText();
            String genero = txtGenero.getText();

            // Cria um novo objeto Critico com os dados coletados.
            Critico critico = new Critico(nome, cpf, dataNascimento, email, biografia, site, genero);

            ManageCritico mc = new ManageCritico(); // Cria uma instância de ManageCritico para interagir com os dados.

            if (criticoToEdit == null) { // Se for um novo crítico (não está editando).
                // Antes de adicionar, verifica se o CPF já existe (para evitar duplicatas).
                if (ManageCritico.cpfExists(cpf)) {
                    showAlert("Erro", "CPF " + cpf + " já cadastrado!", Alert.AlertType.ERROR);
                    return; // Não adiciona se o CPF já existir.
                }
                mc.addCritic(critico); // Adiciona o novo crítico.
                showAlert("Sucesso", "Crítico adicionado com sucesso.", Alert.AlertType.INFORMATION);
            } else { // Se estiver atualizando um crítico existente.
                mc.updateCritic(criticoToEdit, critico); // Chama o método de atualização, passando o objeto original (criticoToEdit) e o novo (critico).
                showAlert("Sucesso", "Crítico atualizado com sucesso.", Alert.AlertType.INFORMATION);
            }

            new CriticoTela(stage).show(); // Retorna para a tela de listagem de críticos.

        } catch (Exception ex) { // Captura qualquer outra exceção que possa ocorrer.
            ex.printStackTrace(); // Imprime o rastreamento da pilha da exceção.
            showAlert("Erro", "Ocorreu um erro ao salvar. Verifique os campos.\nDetalhe: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Método para validar os campos do formulário.
    // A versão anterior deste método usava uma lista 'textFields' que não era populada.
    // Esta versão valida cada campo individualmente.
    private boolean validateFields() {
        if (txtNome.getText().trim().isEmpty()) {
            showAlert("Erro de Validação", "O campo 'Nome Completo' é obrigatório.", Alert.AlertType.ERROR);
            txtNome.requestFocus(); // Coloca o foco no campo com erro.
            return false; // Retorna false indicando que a validação falhou.
        }
        if (txtCpf.getText().trim().isEmpty()) {
            showAlert("Erro de Validação", "O campo 'CPF' é obrigatório.", Alert.AlertType.ERROR);
            txtCpf.requestFocus();
            return false;
        }
        // Validação simples de formato de CPF (11 dígitos numéricos).
        if (!txtCpf.getText().trim().matches("\\d{11}")) {
            showAlert("Erro de Validação", "O CPF deve conter 11 dígitos numéricos.", Alert.AlertType.ERROR);
            txtCpf.requestFocus();
            return false;
        }
        if (txtEmail.getText().trim().isEmpty()) {
            showAlert("Erro de Validação", "O campo 'Email' é obrigatório.", Alert.AlertType.ERROR);
            txtEmail.requestFocus();
            return false;
        }
        // Validação simples de formato de email usando expressão regular.
        if (!txtEmail.getText().trim().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showAlert("Erro de Validação", "Formato de 'Email' inválido.", Alert.AlertType.ERROR);
            txtEmail.requestFocus();
            return false;
        }
        if (dpDataNascimento.getValue() == null) {
            showAlert("Erro de Validação", "O campo 'Data de Nascimento' é obrigatório.", Alert.AlertType.ERROR);
            dpDataNascimento.requestFocus();
            return false;
        }
        // Validação para garantir que a data de nascimento não seja no futuro.
        if (dpDataNascimento.getValue().isAfter(LocalDate.now())) {
            showAlert("Erro de Validação", "A 'Data de Nascimento' não pode ser no futuro.", Alert.AlertType.ERROR);
            dpDataNascimento.requestFocus();
            return false;
        }
        if (txtBio.getText().trim().isEmpty()) { // Validando o campo de biografia.
            showAlert("Erro de Validação", "O campo 'Biografia' é obrigatório.", Alert.AlertType.ERROR);
            txtBio.requestFocus();
            return false;
        }
        // txtSite é opcional, então não há validação de obrigatoriedade para ele.
        if (txtGenero.getText().trim().isEmpty()) {
            showAlert("Erro de Validação", "O campo 'Gênero' é obrigatório.", Alert.AlertType.ERROR);
            txtGenero.requestFocus();
            return false;
        }
        return true; // Se todas as validações passarem, retorna true.
    }

    // Método utilitário para mostrar alertas.
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null); // Não mostra texto de cabeçalho no alerta.
        alert.setContentText(message); // Define a mensagem principal do alerta.

        // Tentativa de estilizar o Alert para tema escuro (pode ter limitações).
        DialogPane dialogPane = alert.getDialogPane();
        if (dialogPane != null) {
            dialogPane.setStyle("-fx-background-color: #2e2e2e;");
            Node contentLabel = dialogPane.lookup(".content.label");
            if (contentLabel != null) {
                contentLabel.setStyle("-fx-text-fill: white;");
            }
            Node headerPanel = dialogPane.lookup(".header-panel");
            if (headerPanel != null) {
                headerPanel.setStyle("-fx-background-color: #212121;");
                Node headerPanelLabel = headerPanel.lookup(".label");
                if (headerPanelLabel != null) {
                    headerPanelLabel.setStyle("-fx-text-fill: white;");
                }
            }
            Node buttonBarNode = dialogPane.lookup(".button-bar");
            if (buttonBarNode instanceof ButtonBar) {
                ButtonBar buttonBar = (ButtonBar) buttonBarNode;
                buttonBar.getButtons().forEach(b -> {
                    b.setStyle("-fx-background-color: #1db954; -fx-text-fill: white; -fx-font-weight: bold;");
                    b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: #1aa34a; -fx-text-fill: white; -fx-font-weight: bold;")); // Efeito hover.
                    b.setOnMouseExited(e -> b.setStyle("-fx-background-color: #1db954; -fx-text-fill: white; -fx-font-weight: bold;"));
                });
            }
        }
        alert.showAndWait(); // Mostra o alerta e espera o usuário interagir.
    }
} // Fim da classe FormularioCritico.