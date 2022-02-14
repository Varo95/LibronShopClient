package com.iesfranciscodelosrios;

import com.iesfranciscodelosrios.service.SocketService;
import com.iesfranciscodelosrios.utils.Dialog;
import com.iesfranciscodelosrios.utils.Tools;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        SocketService.connectToServer();
        loadScene(stage, "login", " LibronShop", false, false);
    }

    private static Parent loadFXML(String fxml) {
        Parent result;
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        try {
            result = fxmlLoader.load();
        } catch (IOException e) {
            Dialog.showError("ERROR", "Hubo un error al cargar la vista", "La vista " + fxml + " no pudo cargarse debido a:\n " + e.getMessage());
            result = null;
        }
        return result;
    }

    public static void loadScene(Stage stage, String fxml, String title, boolean SaW, boolean isResizable) {
        stage.setScene(new Scene(loadFXML(fxml)));
        Tools.addCssAndIcon(stage);
        new JMetro(Style.DARK).setScene(stage.getScene());
        stage.setTitle(title);
        stage.setResizable(isResizable);
        if (SaW)
            stage.showAndWait();
        else
            stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
