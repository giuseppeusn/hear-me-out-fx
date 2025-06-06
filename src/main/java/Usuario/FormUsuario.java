package Usuario;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class FormUsuario {
    private Scene scene;
    private final Stage stage;
    private final usuarioPrincipal usuarioToEdit;

    private final List<TextField> textFields = new ArrayList<>();

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

        Label title = new Label(usuarioToEdit == null ? "Criar cadastro" : "Editar cadastro");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

        VBox formContainer = createForm();

        Button saveButton = new Button(usuarioToEdit == null ? "Salvar" : "Atualizar");
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

    private VBox createForm() {
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);

        String[] labels = {"Nome", "Email", "CPF", "Telefone"};
        String[] prompts = {
                "Digite o seu nome:",
                "Digite o seu email (ex: exemplo@gmail.com)",
                "Digite o CPF:",
                "Digite o número do telefone:"
        };

        for (int i = 0; i < labels.length; i++) {
            Label label = createLabel(labels[i]);
            TextField field = createTextField(prompts[i]);
            textFields.add(field);
            container.getChildren().addAll(label, field);
        }

        // Se estiver editando, preencher os dados
        if (usuarioToEdit != null) {
            fillFields();
        }

        return container;
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
                "-fx-padding: 8 12 8 12;" +
                        "-fx-background-radius: 5;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-color: #2e2e2e;" +
                        "-fx-text-fill: white;"
        );
        return field;
    }

    private void fillFields() {
        textFields.get(0).setText(usuarioToEdit.getNome());
        textFields.get(1).setText(usuarioToEdit.getEmail());
        textFields.get(2).setText(usuarioToEdit.getCpf());
        textFields.get(3).setText(usuarioToEdit.getTelefone());
    }

    private void handleSave() {
        if (!validateFields()) return;

        usuarioPrincipal usuario = new usuarioPrincipal(
                textFields.get(0).getText(),
                textFields.get(1).getText(),
                textFields.get(2).getText(),
                textFields.get(3).getText()
        );

        if (usuarioToEdit == null) {
            ManagerUsuario.addUsuario(usuario);
            showAlert("Sucesso", "Usuário cadastrado com sucesso.", Alert.AlertType.INFORMATION);
        } else {
            ManagerUsuario.updateUsuario(usuarioToEdit, usuario);
            showAlert("Sucesso", "Usuário atualizado com sucesso.", Alert.AlertType.INFORMATION);
        }

        new Usuario(stage).showUser();
    }

    private boolean validateFields() {
        String[] fieldNames = {"nome", "email", "CPF", "telefone"};

        for (int i = 0; i < textFields.size(); i++) {
            if (textFields.get(i).getText().trim().isEmpty()) {
                showAlert("Erro", "O campo '" + fieldNames[i] + "' é obrigatório.", Alert.AlertType.ERROR);
                return false;
            }
        }

        return true;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
