package Usuario;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FormUsuario {
    private Scene scene;
    private final Stage stage;
    private final usuarioPrincipal usuarioToEdit;

    private TextField txtNome;
    private TextField txtEmail;
    private TextField txtCpf;
    private TextField txtTelefone;

    public FormUsuario(Stage stage, usuarioPrincipal usuarioToEdit) {
        this.stage = stage;
        this.usuarioToEdit = usuarioToEdit;
    }

    public void show() {
        VBox root = new VBox(15);
        root.setStyle("-fx-background-color: #212121;");
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));

        HBox backContainer = createBackButton();

        Label title = new Label(usuarioToEdit == null ? "Criar Cadastro" : "Editar Cadastro");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

        VBox formContainer = createForm();

        ScrollPane scrollPane = new ScrollPane(formContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: #212121;");
        VBox.setVgrow(scrollPane, javafx.scene.layout.Priority.ALWAYS);

        Button saveButton = new Button(usuarioToEdit == null ? "Salvar" : "Atualizar");
        saveButton.setStyle("-fx-background-color: #1db954; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;");
        saveButton.setOnAction(e -> handleSave());

        root.getChildren().addAll(
                backContainer,
                title,
                scrollPane,
                saveButton
        );

        this.scene = new Scene(root, 600, 600);
        this.stage.setScene(this.scene);
    }

    private HBox createBackButton() {
        Button backButton = new Button("Voltar");
        backButton.setStyle("-fx-background-color: #2e2e2e; -fx-text-fill: white; -fx-font-weight: bold;");
        backButton.setOnAction(e -> new Usuario(stage).showUser());

        HBox container = new HBox(backButton);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setMaxWidth(Double.MAX_VALUE);
        return container;
    }

    private VBox createForm() {
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(0, 20, 0, 20));

        Label lblNome = createLabel("Nome:");
        txtNome = createTextField("Digite o seu nome");
        container.getChildren().addAll(lblNome, txtNome);

        Label lblEmail = createLabel("Email:");
        txtEmail = createTextField("Digite o seu email (ex: exemplo@gmail.com)");
        container.getChildren().addAll(lblEmail, txtEmail);

        Label lblCpf = createLabel("CPF:");
        txtCpf = createTextField("Digite o CPF (apenas números)");
        if (usuarioToEdit != null) {
            txtCpf.setEditable(false);
            txtCpf.setStyle(txtCpf.getStyle() + "-fx-control-inner-background: #404040;");
        }
        container.getChildren().addAll(lblCpf, txtCpf);

        Label lblTelefone = createLabel("Telefone:");
        txtTelefone = createTextField("Digite o telefone (com DDD, apenas números)");
        container.getChildren().addAll(lblTelefone, txtTelefone);

        if (usuarioToEdit != null) {
            fillFields();
        }

        return container;
    }

    private void fillFields() {
        txtNome.setText(usuarioToEdit.getNome());
        txtEmail.setText(usuarioToEdit.getEmail());
        txtCpf.setText(usuarioToEdit.getCpf());
        txtTelefone.setText(usuarioToEdit.getTelefone());
    }

    private void handleSave() {
        if (!validateFields()) return;

        try {
            usuarioPrincipal usuario = new usuarioPrincipal(
                    txtNome.getText(),
                    txtEmail.getText(),
                    txtCpf.getText(),
                    txtTelefone.getText()
            );

            if (usuarioToEdit == null) {
                if (ManagerUsuario.cpfExists(usuario.getCpf())) {
                    showAlert("Erro", "CPF " + usuario.getCpf() + " já cadastrado!", Alert.AlertType.ERROR);
                    return;
                }
                ManagerUsuario.addUsuario(usuario);
                showAlert("Sucesso", "Usuário cadastrado com sucesso.", Alert.AlertType.INFORMATION);
            } else {
                ManagerUsuario.updateUsuario(usuarioToEdit, usuario);
                showAlert("Sucesso", "Usuário atualizado com sucesso.", Alert.AlertType.INFORMATION);
            }

            new Usuario(stage).showUser();

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Erro", "Ocorreu um erro ao salvar.\nDetalhe: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean validateFields() {
        if (txtNome.getText().trim().isEmpty()) {
            showAlert("Erro de Validação", "O campo 'Nome' é obrigatório.", Alert.AlertType.ERROR);
            txtNome.requestFocus();
            return false;
        }
        if (txtEmail.getText().trim().isEmpty()) {
            showAlert("Erro de Validação", "O campo 'Email' é obrigatório.", Alert.AlertType.ERROR);
            txtEmail.requestFocus();
            return false;
        }
        if (!txtEmail.getText().trim().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showAlert("Erro de Validação", "Formato de 'Email' inválido.", Alert.AlertType.ERROR);
            txtEmail.requestFocus();
            return false;
        }
        if (txtCpf.getText().trim().isEmpty()) {
            showAlert("Erro de Validação", "O campo 'CPF' é obrigatório.", Alert.AlertType.ERROR);
            txtCpf.requestFocus();
            return false;
        }
        if (!txtCpf.getText().trim().matches("\\d{11}")) {
            showAlert("Erro de Validação", "O CPF deve conter 11 dígitos numéricos.", Alert.AlertType.ERROR);
            txtCpf.requestFocus();
            return false;
        }
        if (txtTelefone.getText().trim().isEmpty()) {
            showAlert("Erro de Validação", "O campo 'Telefone' é obrigatório.", Alert.AlertType.ERROR);
            txtTelefone.requestFocus();
            return false;
        }
        if (!txtTelefone.getText().trim().matches("\\d{10,11}")) {
            showAlert("Erro de Validação", "O Telefone deve conter 10 ou 11 dígitos numéricos (com DDD).", Alert.AlertType.ERROR);
            txtTelefone.requestFocus();
            return false;
        }

        return true;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER_LEFT);
        return label;
    }

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

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

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
                    b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: #1aa34a; -fx-text-fill: white; -fx-font-weight: bold;"));
                    b.setOnMouseExited(e -> b.setStyle("-fx-background-color: #1db954; -fx-text-fill: white; -fx-font-weight: bold;"));
                });
            }
        }
        alert.showAndWait();
    }
}