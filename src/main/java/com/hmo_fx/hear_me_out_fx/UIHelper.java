package com.hmo_fx.hear_me_out_fx;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class UIHelper {
    private static UIHelper instance;

    private UIHelper() {}

    public static UIHelper getInstance() {
        if (instance == null) {
            instance = new UIHelper();
        }
        return instance;
    }

    public Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER_LEFT);
        return label;
    }

    public TextField createTextField(String prompt) {
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

    public void showAlert(String title, String message, Alert.AlertType type) {
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