package Usuario;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FormUsuario {
    private Stage stage;
    private Scene scene;
    private TextField nameField;
    private TextField emailField;
    private TextField cpfField;
    private TextField phoneField;

    public FormUsuario(Stage stage, Scene scene) {
        this.stage = stage;
        this.scene = scene;
    }

    public void show() {
        VBox root = new VBox(15);
        root.setStyle("-fx-background-color: #212121;");
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));

        HBox backContainer = createBackButton();

        Label title = new Label("Criar cadastro");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

        VBox formContainer = createFormFields();

        Button saveButton = new Button("Salvar");
        saveButton.setStyle("-fx-background-color: #1db954; -fx-text-fill: white; -fx-font-weight: bold;");
        saveButton.setOnAction(e -> handleSave());

        root.getChildren().addAll(
                backContainer,
                title,
                formContainer,
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

    private VBox createFormFields() {
        VBox container = new VBox(15);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(10));

        nameField = new TextField();
        emailField = new TextField();
        cpfField = new TextField();
        phoneField = new TextField();

        container.getChildren().addAll(
                createFormFieldRow("Nome Completo:", nameField, "Digite seu nome"),
                createFormFieldRow("Email:", emailField, "Digite seu email"),
                createFormFieldRow("CPF:", cpfField, "Apenas 11 dígitos"),
                createFormFieldRow("Telefone:", phoneField, "Ex: (XX) XXXXX-XXXX")
        );

        return container;
    }

    private HBox createFormFieldRow(String labelText, TextField textField, String promptText) {
        Label label = new Label(labelText);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        label.setMinWidth(120);

        textField.setPromptText(promptText);
        textField.setPrefWidth(300);
        textField.setStyle("-fx-background-color: #2e2e2e; -fx-text-fill: white; -fx-border-color: #444;");

        HBox row = new HBox(10, label, textField);
        row.setAlignment(Pos.CENTER);
        return row;
    }

    private void handleSave() {
        String nome = nameField.getText().trim();
        String email = emailField.getText().trim();
        String cpf = cpfField.getText().trim();
        String telefone = phoneField.getText().trim();

        if (nome.isEmpty() || email.isEmpty() || cpf.isEmpty() || telefone.isEmpty()) {
            showAlert("Erro", "Todos os campos devem ser preenchidos.", Alert.AlertType.ERROR);
            return;
        }

        // Aqui você pode salvar ou enviar o objeto para uma classe de controle
        usuarioPrincipal usuario = new usuarioPrincipal(nome, email, cpf);
        System.out.println("Usuário cadastrado: " + usuario.getNome());

        showAlert("Sucesso", "Usuário cadastrado com sucesso!", Alert.AlertType.INFORMATION);
        new Usuario(stage).showUser();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
