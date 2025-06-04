package artista;

import artista.ManageArtista;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FormArtistaScreen {
    private Scene scene;
    private final Stage stage;
    private final Artista artistaToEdit;

    private String coverPath = "";
    private final List<TextField> textFields = new ArrayList<>();
    private Label coverLabel;

    public FormArtistaScreen(Stage stage, Artista artistaToEdit) {
        this.stage = stage;
        this.artistaToEdit = artistaToEdit;
    }

    public void show() {
        VBox root = new VBox(15);
        root.setStyle("-fx-background-color: #212121;");
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));

        HBox backContainer = createBackButton();

        Label title = new Label(artistaToEdit == null ? "Adicionar artista" : "Editar artista");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

        VBox formContainer = createForm();

        Button saveButton = new Button(artistaToEdit == null ? "Salvar" : "Atualizar");
        saveButton.setStyle("-fx-background-color: #1db954; -fx-text-fill: white; -fx-font-weight: bold;");
        saveButton.setOnAction(e -> saveArtista());

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
        backButton.setOnAction(e -> new ArtistaScreen(stage).show());

        HBox container = new HBox(backButton);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setMaxWidth(Double.MAX_VALUE);
        return container;
    }

    private VBox createForm() {
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);

        String[] labels = {"Nome", "email", "Data de lançamento", "Bio", "nacionalidade", "site" , "genero musical"};
        String[] prompts = {
                "Digite o nome do artista/banda",
                "Digite o email",
                "Digite a data de lançamento (DD/MM/YYYY)",
                "Digite a bio",
                "Digite a nacionalidade",
                "Digite o link do site",
                "Digite o genero musical"
        };

        for (int i = 0; i < labels.length; i++) {
            Label label = createLabel(labels[i]);
            TextField field = createTextField(prompts[i]);
            textFields.add(field);
            container.getChildren().addAll(label, field);
        }

        // Botão para selecionar a capa
        Button selectCoverButton = new Button("Escolher capa");
        selectCoverButton.setStyle("-fx-background-color: #4f72b3; -fx-text-fill: white;");
        selectCoverButton.setOnAction(e -> selectCover());

        coverLabel = new Label("Nenhuma imagem selecionada");
        coverLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        coverLabel.setAlignment(Pos.CENTER);
        coverLabel.setMaxWidth(Double.MAX_VALUE);

        container.getChildren().addAll(selectCoverButton, coverLabel);

        // Se estiver editando, preencher os dados
        if (artistaToEdit != null) {
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = sdf.format(artistaToEdit.getLaunchDate());

        textFields.get(0).setText(artistaToEdit.getName());
        textFields.get(1).setText(artistaToEdit.getEmail());
        textFields.get(2).setText(formattedDate);
        textFields.get(3).setText(artistaToEdit.getBio());
        coverPath = artistaToEdit.getCover();
        textFields.get(4).setText(artistaToEdit.getNacionalidade());
        textFields.get(5).setText(artistaToEdit.getSite());
        textFields.get(6).setText(artistaToEdit.getGeneroMusical());
        File file = new File(coverPath);
        coverLabel.setText(file.getName());
    }

    private void saveArtista() {
        if (!validateFields()) return;

        try {
            Artista artista = new Artista(
                    textFields.get(0).getText(),
                    textFields.get(1).getText(),
                    textFields.get(2).getText(),
                    textFields.get(3).getText(),
                    coverPath,
                    textFields.get(4).getText(),
                    textFields.get(5).getText(),
                    textFields.get(6).getText()
            );

            ManageArtista ma = new ManageArtista();

            if (artista == null) {
                ma.addArtista(artista);
                showAlert("Sucesso", "Artista adicionado com sucesso.", Alert.AlertType.INFORMATION);
            } else {
                ma.updateArtista(artistaToEdit, artista);
                showAlert("Sucesso", "Artista atualizado com sucesso.", Alert.AlertType.INFORMATION);
            }

            new ArtistaScreen(stage).show();

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Erro", "Erro ao salvar. Verifique os campos.", Alert.AlertType.ERROR);
        }
    }

    private boolean validateFields() {
        String[] fieldNames = {"nome", "email", "data de lançamento", "bio", "nacionalidade", "site", "genero musical"};

        for (int i = 0; i < textFields.size(); i++) {
            if (textFields.get(i).getText().trim().isEmpty()) {
                showAlert("Erro", "O campo '" + fieldNames[i] + "' é obrigatório.", Alert.AlertType.ERROR);
                return false;
            }
        }

        if (coverPath.isEmpty()) {
            showAlert("Erro", "Selecione uma imagem de capa.", Alert.AlertType.ERROR);
            return false;
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
