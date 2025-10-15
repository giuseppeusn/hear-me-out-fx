package critico;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.ZoneId;

import com.hmo_fx.hear_me_out_fx.UIHelper; // Padrão Singleton
import com.hmo_fx.hear_me_out_fx.EntityFactory; // Padrão Simple Factory
import com.hmo_fx.hear_me_out_fx.PersistenceFacade; // Padrão Facade


public class FormCritico {
    private Scene scene;
    private final Stage stage;
    private final Critico criticoToEdit;

    private TextField txtNome;
    private TextField txtCpf;
    private TextField txtEmail;
    private DatePicker dpDataNascimento;
    private TextField txtBio;
    private TextField txtSite;
    private TextField txtGenero;

    // Acessa a instância única do Singleton UIHelper
    private final UIHelper ui = UIHelper.getInstance();

    public FormCritico(Stage stage, Critico criticoToEdit) {
        this.stage = stage;
        this.criticoToEdit = criticoToEdit;
    }

    public void show() {
        VBox root = new VBox(15);
        root.setStyle("-fx-background-color: #212121;");
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));

        HBox backContainer = createBackButton();

        Label title = new Label(criticoToEdit == null ? "Adicionar Crítico" : "Editar Crítico");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

        VBox formContainer = createForm();

        Button saveButton = new Button(criticoToEdit == null ? "Salvar" : "Atualizar");
        saveButton.setStyle("-fx-background-color: #1db954; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;");
        saveButton.setOnAction(e -> saveCritic());

        ScrollPane scrollPane = new ScrollPane(formContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: #212121;");
        VBox.setVgrow(scrollPane, javafx.scene.layout.Priority.ALWAYS);

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
        backButton.setOnAction(e -> new CriticoScreen(stage).show());

        HBox container = new HBox(backButton);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setMaxWidth(Double.MAX_VALUE);
        return container;
    }

    private VBox createForm() {
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(0, 20, 0, 20));

        // Usa UIHelper (Singleton)
        Label lblNome = ui.createLabel("Nome Completo:");
        txtNome = ui.createTextField("Digite o nome do crítico");
        container.getChildren().addAll(lblNome, txtNome);

        Label lblCpf = ui.createLabel("CPF:");
        txtCpf = ui.createTextField("Digite o CPF (apenas números)");
        if (criticoToEdit != null) {
            txtCpf.setEditable(false);
            txtCpf.setStyle(txtCpf.getStyle() + "-fx-control-inner-background: #404040;");
        }
        container.getChildren().addAll(lblCpf, txtCpf);

        Label lblEmail = ui.createLabel("Email:");
        txtEmail = ui.createTextField("Digite o email");
        container.getChildren().addAll(lblEmail, txtEmail);

        Label lblDataNascimento = ui.createLabel("Data de Nascimento:");
        dpDataNascimento = new DatePicker();
        dpDataNascimento.setPromptText("DD/MM/AAAA");
        dpDataNascimento.setMaxWidth(Double.MAX_VALUE);
        dpDataNascimento.setStyle(
                "-fx-padding: 4 8;" +
                        "-fx-background-radius: 5;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-color: #2e2e2e;" +
                        "-fx-text-fill: white;"
        );
        dpDataNascimento.getEditor().setStyle("-fx-text-fill: white; -fx-background-color: #2e2e2e;");
        container.getChildren().addAll(lblDataNascimento, dpDataNascimento);

        Label lblBiografia = ui.createLabel("Biografia:");
        txtBio = ui.createTextField("Descreva o crítico...");
        container.getChildren().addAll(lblBiografia, txtBio);

        Label lblSite = ui.createLabel("Site/Blog (opcional):");
        txtSite = ui.createTextField("http://exemplo.com");
        container.getChildren().addAll(lblSite, txtSite);

        Label lblGenero = ui.createLabel("Gênero:");
        txtGenero = ui.createTextField("Ex: Masculino, feminino ou indefinido.");
        container.getChildren().addAll(lblGenero, txtGenero);

        if (criticoToEdit != null) {
            fillFields();
        }
        return container;
    }

    private void fillFields() {
        txtNome.setText(criticoToEdit.getNome());
        txtCpf.setText(criticoToEdit.getCpf());
        txtEmail.setText(criticoToEdit.getEmail());

        if (criticoToEdit.getData_nascimento() != null) {
            dpDataNascimento.setValue(criticoToEdit.getData_nascimento().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }
        txtBio.setText(criticoToEdit.getBiografia());
        txtSite.setText(criticoToEdit.getSite());
        txtGenero.setText(criticoToEdit.getGenero());
    }

    private void saveCritic() {
        if (!validateFields()) return;

        try {
            // Usa EntityFactory (Simple Factory), que delega ao CriticoBuilder (Builder)
            Critico critico = EntityFactory.createCritico(
                    txtNome.getText(),
                    txtCpf.getText(),
                    dpDataNascimento.getValue(), // LocalDate
                    txtEmail.getText(),
                    txtBio.getText(),
                    txtSite.getText(),
                    txtGenero.getText()
            );

            if (criticoToEdit == null) {
                // Usa PersistenceFacade (Facade)
                if (!PersistenceFacade.addEntity(critico)) {
                    ui.showAlert("Erro", "CPF " + critico.getCpf() + " já cadastrado!", Alert.AlertType.ERROR);
                    return;
                }
                ui.showAlert("Sucesso", "Crítico adicionado com sucesso.", Alert.AlertType.INFORMATION);
            } else {
                // Usa PersistenceFacade (Facade)
                PersistenceFacade.updateEntity(criticoToEdit, critico);
                ui.showAlert("Sucesso", "Crítico atualizado com sucesso.", Alert.AlertType.INFORMATION);
            }

            new CriticoScreen(stage).show();

        } catch (Exception ex) {
            ex.printStackTrace();
            ui.showAlert("Erro", "Ocorreu um erro ao salvar. Verifique os campos.\nDetalhe: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean validateFields() {
        if (txtNome.getText().trim().isEmpty()) {
            ui.showAlert("Erro de Validação", "O campo 'Nome Completo' é obrigatório.", Alert.AlertType.ERROR);
            txtNome.requestFocus();
            return false;
        }
        if (txtCpf.getText().trim().isEmpty()) {
            ui.showAlert("Erro de Validação", "O campo 'CPF' é obrigatório.", Alert.AlertType.ERROR);
            txtCpf.requestFocus();
            return false;
        }
        if (!txtCpf.getText().trim().matches("\\d{11}")) {
            ui.showAlert("Erro de Validação", "O CPF deve conter 11 dígitos numéricos.", Alert.AlertType.ERROR);
            txtCpf.requestFocus();
            return false;
        }
        if (txtEmail.getText().trim().isEmpty()) {
            ui.showAlert("Erro de Validação", "O campo 'Email' é obrigatório.", Alert.AlertType.ERROR);
            txtEmail.requestFocus();
            return false;
        }
        if (!txtEmail.getText().trim().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            ui.showAlert("Erro de Validação", "Formato de 'Email' inválido.", Alert.AlertType.ERROR);
            txtEmail.requestFocus();
            return false;
        }
        if (dpDataNascimento.getValue() == null) {
            ui.showAlert("Erro de Validação", "O campo 'Data de Nascimento' é obrigatório.", Alert.AlertType.ERROR);
            dpDataNascimento.requestFocus();
            return false;
        }
        if (dpDataNascimento.getValue().isAfter(LocalDate.now())) {
            ui.showAlert("Erro de Validação", "A 'Data de Nascimento' não pode ser no futuro.", Alert.AlertType.ERROR);
            dpDataNascimento.requestFocus();
            return false;
        }
        if (txtBio.getText().trim().isEmpty()) {
            ui.showAlert("Erro de Validação", "O campo 'Biografia' é obrigatório.", Alert.AlertType.ERROR);
            txtBio.requestFocus();
            return false;
        }
        if (txtGenero.getText().trim().isEmpty()) {
            ui.showAlert("Erro de Validação", "O campo 'Gênero' é obrigatório.", Alert.AlertType.ERROR);
            txtGenero.requestFocus();
            return false;
        }
        return true;
    }
}