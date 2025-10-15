package Album;

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
import java.util.Date;

import com.hmo_fx.hear_me_out_fx.UIHelper; // Padrão Singleton
import com.hmo_fx.hear_me_out_fx.EntityFactory; // Padrão Simple Factory
import com.hmo_fx.hear_me_out_fx.PersistenceFacade; // Padrão Facade

public class FormAlbum {
    private Scene scene;
    private final Stage stage;
    private final Album albumToEdit;

    private TextField txtNome;
    private TextField txtArtista;
    private DatePicker dpDataLancamento;
    private Label coverLabel;
    private String coverPath = "";

    // Acessa a instância única do Singleton UIHelper
    private final UIHelper ui = UIHelper.getInstance();

    public FormAlbum(Stage stage, Album albumToEdit) {
        this.stage = stage;
        this.albumToEdit = albumToEdit;
        if (this.albumToEdit != null) {
            this.coverPath = albumToEdit.getCover();
        }
    }

    public void show() {
        VBox root = new VBox(15);
        root.setStyle("-fx-background-color: #212121;");
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));

        HBox backContainer = createBackButton();

        Label title = new Label(albumToEdit == null ? "Adicionar Álbum" : "Editar Álbum");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

        VBox formContainer = createForm();

        ScrollPane scrollPane = new ScrollPane(formContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: #212121;");
        VBox.setVgrow(scrollPane, javafx.scene.layout.Priority.ALWAYS);

        Button saveButton = new Button(albumToEdit == null ? "Salvar" : "Atualizar");
        saveButton.setStyle("-fx-background-color: #1db954; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;");
        saveButton.setOnAction(e -> saveAlbum());

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
        backButton.setOnAction(e -> new AlbumScreen(stage).show());
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
        Label lblNome = ui.createLabel("Nome do Álbum:");
        txtNome = ui.createTextField("Digite o nome do álbum");
        container.getChildren().addAll(lblNome, txtNome);

        Label lblArtista = ui.createLabel("Artista:");
        txtArtista = ui.createTextField("Digite o nome do artista");
        container.getChildren().addAll(lblArtista, txtArtista);

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

        Button selectCoverButton = new Button("Escolher Capa");
        selectCoverButton.setStyle("-fx-background-color: #4f72b3; -fx-text-fill: white;");
        selectCoverButton.setOnAction(e -> selectCover());

        coverLabel = ui.createLabel("Nenhuma imagem selecionada");

        HBox coverBox = new HBox(10, selectCoverButton, coverLabel);
        coverBox.setAlignment(Pos.CENTER_LEFT);
        container.getChildren().add(coverBox);

        if (albumToEdit != null) {
            fillFields();
        }

        return container;
    }

    private void fillFields() {
        txtNome.setText(albumToEdit.getName());
        txtArtista.setText(albumToEdit.getArtist());

        if (albumToEdit.getLaunchDate() != null) {
            Date launchDate = albumToEdit.getLaunchDate();
            dpDataLancamento.setValue(launchDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }

        if (coverPath != null && !coverPath.isEmpty()) {
            try {
                File file = new File(new java.net.URI(coverPath));
                coverLabel.setText(file.getName());
            } catch (Exception e) {
                coverLabel.setText("Caminho inválido");
            }
        }
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

    private void saveAlbum() {
        if (!validateFields()) return;

        try {
            // Usa EntityFactory (Simple Factory), que delega ao AlbumBuilder (Builder)
            Album album = EntityFactory.createAlbum(
                    txtNome.getText(),
                    txtArtista.getText(),
                    dpDataLancamento.getValue(), // LocalDate
                    coverPath
            );

            if (albumToEdit == null) {
                if (ManageAlbum.albumExists(album.getName(), album.getArtist())) {
                    ui.showAlert("Erro", "Este álbum já está cadastrado para este artista.", Alert.AlertType.ERROR);
                    return;
                }
                // Usa PersistenceFacade (Facade)
                PersistenceFacade.addEntity(album);
                ui.showAlert("Sucesso", "Álbum adicionado com sucesso.", Alert.AlertType.INFORMATION);
            } else {
                // Usa PersistenceFacade (Facade)
                PersistenceFacade.updateEntity(albumToEdit, album);
                ui.showAlert("Sucesso", "Álbum atualizado com sucesso.", Alert.AlertType.INFORMATION);
            }

            new AlbumScreen(stage).show();

        } catch (Exception ex) {
            ex.printStackTrace();
            ui.showAlert("Erro", "Ocorreu um erro ao salvar: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean validateFields() {
        if (txtNome.getText().trim().isEmpty()) {
            ui.showAlert("Erro de Validação", "O campo 'Nome do Álbum' é obrigatório.", Alert.AlertType.ERROR);
            txtNome.requestFocus();
            return false;
        }
        if (txtArtista.getText().trim().isEmpty()) {
            ui.showAlert("Erro de Validação", "O campo 'Artista' é obrigatório.", Alert.AlertType.ERROR);
            txtArtista.requestFocus();
            return false;
        }
        if (dpDataLancamento.getValue() == null) {
            ui.showAlert("Erro de Validação", "O campo 'Data de Lançamento' é obrigatório.", Alert.AlertType.ERROR);
            dpDataLancamento.requestFocus();
            return false;
        }
        if (dpDataLancamento.getValue().isAfter(LocalDate.now())) {
            ui.showAlert("Erro de Validação", "A 'Data de Lançamento' não pode ser uma data futura.", Alert.AlertType.ERROR);
            dpDataLancamento.requestFocus();
            return false;
        }
        if (coverPath == null || coverPath.trim().isEmpty()) {
            ui.showAlert("Erro de Validação", "É obrigatório selecionar uma imagem de capa.", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }
}