package music;

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


public class FormMusic {
    private Scene scene;
    private final Stage stage;
    private final Music musicToEdit;

    private TextField txtNome;
    private TextField txtDuracao;
    private DatePicker dpDataLancamento;
    private TextField txtArtista;
    private TextField txtAlbum;
    private Label coverLabel;
    private String coverPath = "";

    // Acessa a instância única do Singleton UIHelper
    private final UIHelper ui = UIHelper.getInstance();

    public FormMusic(Stage stage, Music musicToEdit) {
        this.stage = stage;
        this.musicToEdit = musicToEdit;
        if (musicToEdit != null) {
            this.coverPath = musicToEdit.getCover();
        }
    }

    public void show() {
        VBox root = new VBox(15);
        root.setStyle("-fx-background-color: #212121;");
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));

        HBox backContainer = createBackButton();

        Label title = new Label(musicToEdit == null ? "Adicionar Música" : "Editar Música");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

        VBox formContainer = createForm();

        ScrollPane scrollPane = new ScrollPane(formContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: #212121;");
        VBox.setVgrow(scrollPane, javafx.scene.layout.Priority.ALWAYS);

        Button saveButton = new Button(musicToEdit == null ? "Salvar" : "Atualizar");
        saveButton.setStyle("-fx-background-color: #1db954; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;");
        saveButton.setOnAction(e -> saveMusic());

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
        backButton.setOnAction(e -> new MusicScreen(stage).show());

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
        txtNome = ui.createTextField("Digite o nome da música");
        container.getChildren().addAll(lblNome, txtNome);

        Label lblDuracao = ui.createLabel("Duração:");
        txtDuracao = ui.createTextField("Digite a duração (ex: 3:45)");
        container.getChildren().addAll(lblDuracao, txtDuracao);

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

        Label lblArtista = ui.createLabel("Artista:");
        txtArtista = ui.createTextField("Digite o nome do artista");
        container.getChildren().addAll(lblArtista, txtArtista);

        Label lblAlbum = ui.createLabel("Álbum:");
        txtAlbum = ui.createTextField("Digite o nome do álbum");
        container.getChildren().addAll(lblAlbum, txtAlbum);

        Button selectCoverButton = new Button("Escolher Capa");
        selectCoverButton.setStyle("-fx-background-color: #4f72b3; -fx-text-fill: white;");
        selectCoverButton.setOnAction(e -> selectCover());

        coverLabel = ui.createLabel("Nenhuma imagem selecionada");

        HBox coverBox = new HBox(10, selectCoverButton, coverLabel);
        coverBox.setAlignment(Pos.CENTER_LEFT);
        container.getChildren().add(coverBox);

        if (musicToEdit != null) {
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
        txtNome.setText(musicToEdit.getName());
        txtDuracao.setText(musicToEdit.getDuration());
        txtArtista.setText(musicToEdit.getArtist());
        txtAlbum.setText(musicToEdit.getAlbum());

        if (musicToEdit.getLaunchDate() != null) {
            dpDataLancamento.setValue(musicToEdit.getLaunchDate().toInstant()
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

    private void saveMusic() {
        if (!validateFields()) return;

        try {
            // Usa EntityFactory (Simple Factory), que delega ao MusicBuilder (Builder)
            Music music = EntityFactory.createMusic(
                    txtNome.getText(),
                    txtDuracao.getText(),
                    dpDataLancamento.getValue(), // LocalDate
                    coverPath,
                    txtArtista.getText(),
                    txtAlbum.getText()
            );

            if (musicToEdit == null) {
                // Usa PersistenceFacade (Facade)
                PersistenceFacade.addEntity(music);
                ui.showAlert("Sucesso", "Música adicionada com sucesso.", Alert.AlertType.INFORMATION);
            } else {
                // Usa PersistenceFacade (Facade)
                PersistenceFacade.updateEntity(musicToEdit, music);
                ui.showAlert("Sucesso", "Música atualizada com sucesso.", Alert.AlertType.INFORMATION);
            }

            new MusicScreen(stage).show();

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
        if (txtDuracao.getText().trim().isEmpty()) {
            ui.showAlert("Erro de Validação", "O campo 'Duração' é obrigatório.", Alert.AlertType.ERROR);
            txtDuracao.requestFocus();
            return false;
        }
        if (!txtDuracao.getText().trim().matches("^\\d{1,2}:\\d{2}$")) {
            ui.showAlert("Erro de Validação", "O formato da 'Duração' deve ser 'minutos:segundos' (ex: 3:45).", Alert.AlertType.ERROR);
            txtDuracao.requestFocus();
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
        if (txtArtista.getText().trim().isEmpty()) {
            ui.showAlert("Erro de Validação", "O campo 'Artista' é obrigatório.", Alert.AlertType.ERROR);
            txtArtista.requestFocus();
            return false;
        }
        if (txtAlbum.getText().trim().isEmpty()) {
            ui.showAlert("Erro de Validação", "O campo 'Álbum' é obrigatório.", Alert.AlertType.ERROR);
            txtAlbum.requestFocus();
            return false;
        }
        if (coverPath == null || coverPath.trim().isEmpty()) {
            ui.showAlert("Erro de Validação", "É obrigatório selecionar uma imagem de capa.", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }
}