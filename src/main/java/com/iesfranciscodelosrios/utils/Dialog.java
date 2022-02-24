package com.iesfranciscodelosrios.utils;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

/**
 * This class is used to show different Dialog popups to the user (GUI)
 */
public class Dialog {
    public static void showError(String title, String header, String description) {
        showDialog(Alert.AlertType.ERROR, title, header, description);
    }

    public static void showWarning(String title, String header, String description) {
        showDialog(Alert.AlertType.WARNING, title, header, description);
    }

    public static void showInformation(String title, String header, String description) {
        showDialog(Alert.AlertType.INFORMATION, title, header, description);
    }

    private static void showDialog(Alert.AlertType type, String title, String header, String description) {
        Alert alert = new Alert(type);
        Tools.addCssAndIcon((Stage) alert.getDialogPane().getScene().getWindow());
        new JMetro(Style.DARK).setScene(alert.getDialogPane().getScene());
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(description);
        alert.showAndWait();
    }

    private static boolean showDialogBoolean(String title, String header, String description) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Tools.addCssAndIcon((Stage) alert.getDialogPane().getScene().getWindow());
        new JMetro(Style.DARK).setScene(alert.getDialogPane().getScene());
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(description);
        alert.showAndWait();
        return alert.getResult().getButtonData().isDefaultButton();
    }

}