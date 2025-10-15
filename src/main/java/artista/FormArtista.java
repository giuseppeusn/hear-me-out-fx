package artista;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;

import com.hmo_fx.hear_me_out_fx.UIHelper; // Padrão Singleton
import com.hmo_fx.hear_me_out_fx.EntityFactory; // Padrão Simple Factory
import com.hmo_fx.hear_me_out_fx.PersistenceFacade; // Padrão Facade

public class FormArtista {
    private Scene scene;
    private final Stage stage;
    private final Artista artistaToEdit;

    private TextField txtNome;
    private TextField txtEmail;
    private DatePicker dpDataLancamento;
    private TextField txtBio;
    private TextField txtNacionalidade;
    private TextField txtSite;
    private TextField txtGeneroMusical;
    private Label coverLabel;
    private String coverPath = "";

    // Acessa a instância única do Singleton UIHelper
    private final UIHelper ui = UIHelper.getInstance();

    public FormArtista(Stage stage, Artista artistaToEdit) {
        this.stage = stage;
        this.artistaToEdit = artistaToEdit;
        if (artistaToEdit != null) {
            this.coverPath = artistaToEdit.getCover();
        }
    }

    public void show() {
        VBox root = new VBox(15);
        root.setStyle("-fx-background-color: #212121;");
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));

        HBox backContainer = createBackButton();

        Label title = new Label(artistaToEdit == null ? "Adicionar Artista" : "Editar Artista");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

        VBox formContainer = createForm();

        ScrollPane scrollPane = new ScrollPane(formContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: #212121;");
        VBox.setVgrow(scrollPane, javafx.scene.layout.Priority.ALWAYS);

        Button saveButton = new Button(artistaToEdit == null ? "Salvar" : "Atualizar");
        saveButton.setStyle("-fx-background-color: #1db954; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;");
        saveButton.setOnAction(e -> saveArtista());

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
        backButton.setOnAction(e -> new ArtistaScreen(stage).show());

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
        Label lblNome = ui.createLabel("Nome:");
        txtNome = ui.createTextField("Digite o nome do artista/banda");
        container.getChildren().addAll(lblNome, txtNome);

        Label lblEmail = ui.createLabel("Email:");
        txtEmail = ui.createTextField("Digite o email de contato");
        container.getChildren().addAll(lblEmail, txtEmail);

        Label lblDataLancamento = ui.createLabel("Data de Lançamento:");
        dpDataLancamento = new DatePicker();
        dpDataLancamento.setPromptText("DD/MM/AAAA");
        dpDataLancamento.setMaxWidth(Double.MAX_VALUE);
        dpDataLancamento.setStyle(
                "-fx-padding: 4 8;" +
                        "-fx-background-radius: 5;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-color: #2e2e2e;" +
                        "-fx-text-fill: white;"
        );
        dpDataLancamento.getEditor().setStyle("-fx-text-fill: white; -fx-background-color: #2e2e2e;");
        container.getChildren().addAll(lblDataLancamento, dpDataLancamento);

        Label lblBio = ui.createLabel("Bio:");
        txtBio = ui.createTextField("Digite a bio do artista");
        container.getChildren().addAll(lblBio, txtBio);

        Label lblNacionalidade = ui.createLabel("Nacionalidade:");
        txtNacionalidade = ui.createTextField("Digite a nacionalidade");
        container.getChildren().addAll(lblNacionalidade, txtNacionalidade);

        Label lblSite = ui.createLabel("Site (opcional):");
        txtSite = ui.createTextField("http://exemplo.com");
        container.getChildren().addAll(lblSite, txtSite);

        Label lblGeneroMusical = ui.createLabel("Gênero Musical:");
        txtGeneroMusical = ui.createTextField("Ex: Rock, Pop, etc.");
        container.getChildren().addAll(lblGeneroMusical, txtGeneroMusical);

        Button selectCoverButton = new Button("Escolher Capa");
        selectCoverButton.setStyle("-fx-background-color: #4f72b3; -fx-text-fill: white;");
        selectCoverButton.setOnAction(e -> selectCover());

        coverLabel = ui.createLabel("Nenhuma imagem selecionada");

        HBox coverBox = new HBox(10, selectCoverButton, coverLabel);
        coverBox.setAlignment(Pos.CENTER_LEFT);
        container.getChildren().add(coverBox);

        if (artistaToEdit != null) {
            fillFields();
        }

        return container;
    }

    private void selectCover() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escolher capa");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            coverPath = selectedFile.toURI().toString();
            coverLabel.setText(selectedFile.getName());
        }
    }

    private void fillFields() {
        txtNome.setText(artistaToEdit.getName());
        txtEmail.setText(artistaToEdit.getEmail());
        txtBio.setText(artistaToEdit.getBio());
        txtNacionalidade.setText(artistaToEdit.getNacionalidade());
        txtSite.setText(artistaToEdit.getSite());
        txtGeneroMusical.setText(artistaToEdit.getGeneroMusical());

        if (artistaToEdit.getLaunchDate() != null) {
            dpDataLancamento.setValue(artistaToEdit.getLaunchDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }

        if (coverPath != null && !coverPath.isEmpty()) {
            try {
                File file = new File(new java.net.URI(coverPath));
                coverLabel.setText(file.getName());
            } catch (Exception e) {
                coverLabel.setText("Caminho de imagem inválido");
            }
        }
    }

    private void saveArtista() {
        if (!validateFields()) return;

        try {
            // Usa EntityFactory (Simple Factory), que delega ao ArtistaBuilder (Builder)
            Artista artista = EntityFactory.createArtista(
                    txtNome.getText(),
                    txtEmail.getText(),
                    dpDataLancamento.getValue(), // LocalDate
                    txtBio.getText(),
                    coverPath,
                    txtNacionalidade.getText(),
                    txtSite.getText(),
                    txtGeneroMusical.getText()
            );

            if (artistaToEdit == null) {
                // Usa PersistenceFacade (Facade)
                PersistenceFacade.addEntity(artista);
                ui.showAlert("Sucesso", "Artista adicionado com sucesso.", Alert.AlertType.INFORMATION);
            } else {
                // Usa PersistenceFacade (Facade)
                PersistenceFacade.updateEntity(artistaToEdit, artista);
                ui.showAlert("Sucesso", "Artista atualizado com sucesso.", Alert.AlertType.INFORMATION);
            }

            new ArtistaScreen(stage).show();

        } catch (Exception ex) {
            ex.printStackTrace();
            ui.showAlert("Erro", "Ocorreu um erro ao salvar. Verifique os campos.\nDetalhe: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean validateFields() {
        if (txtNome.getText().trim().isEmpty()) {
            ui.showAlert("Erro de Validação", "O campo 'Nome' é obrigatório.", Alert.AlertType.ERROR);
            txtNome.requestFocus();
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
        if (dpDataLancamento.getValue() == null) {
            ui.showAlert("Erro de Validação", "O campo 'Data de Lançamento' é obrigatório.", Alert.AlertType.ERROR);
            dpDataLancamento.requestFocus();
            return false;
        }
        if (dpDataLancamento.getValue().isAfter(LocalDate.now())) {
            ui.showAlert("Erro de Validação", "A 'Data de Lançamento' não pode ser no futuro.", Alert.AlertType.ERROR);
            dpDataLancamento.requestFocus();
            return false;
        }
        if (txtBio.getText().trim().isEmpty()) {
            ui.showAlert("Erro de Validação", "O campo 'Bio' é obrigatório.", Alert.AlertType.ERROR);
            txtBio.requestFocus();
            return false;
        }
        if (txtNacionalidade.getText().trim().isEmpty()) {
            ui.showAlert("Erro de Validação", "O campo 'Nacionalidade' é obrigatório.", Alert.AlertType.ERROR);
            txtNacionalidade.requestFocus();
            return false;
        }
        if (txtGeneroMusical.getText().trim().isEmpty()) {
            ui.showAlert("Erro de Validação", "O campo 'Gênero Musical' é obrigatório.", Alert.AlertType.ERROR);
            txtGeneroMusical.requestFocus();
            return false;
        }
        if (coverPath == null || coverPath.trim().isEmpty()) {
            ui.showAlert("Erro de Validação", "É obrigatório selecionar uma imagem de capa.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }
}