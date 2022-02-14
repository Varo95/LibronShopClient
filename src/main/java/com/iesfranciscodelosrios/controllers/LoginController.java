package com.iesfranciscodelosrios.controllers;

import com.iesfranciscodelosrios.App;
import com.iesfranciscodelosrios.model.Client;
import com.iesfranciscodelosrios.model.Manager;
import com.iesfranciscodelosrios.model.User;
import com.iesfranciscodelosrios.service.SocketService;
import com.iesfranciscodelosrios.utils.Dialog;
import com.iesfranciscodelosrios.utils.Tools;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.Socket;

public class LoginController {
    @FXML
    private Button btn_login;
    @FXML
    private Button btn_register;
    @FXML
    private MenuItem mi_about;
    @FXML
    private MenuItem mi_close;
    @FXML
    private MenuItem mi_connect;
    @FXML
    private TextField tf_name;
    @FXML
    private PasswordField tf_passwd;
    @FXML
    private ToggleGroup userType;
    @FXML
    private RadioButton rbtn_user;
    private Socket connection;
    private User user;

    @FXML
    protected void initialize() {
        connection = SocketService.getConnectionToServer();
        setButtonActions();
        setIcons();
        userType.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue instanceof RadioButton button) {
                user = null;
                if (button.getText().equals("Usuario")) {
                    user = new Client();
                } else if (button.getText().equals("Operario")) {
                    user = new Manager();
                }
            }
        });
        userType.selectToggle(rbtn_user);
    }

    private void setButtonActions() {
        mi_connect.setOnAction(event -> {
            if (connection == null || connection.isClosed()) {
                SocketService.connectToServer();
                connection = SocketService.getConnectionToServer();
                if (connection == null || connection.isClosed())
                    Dialog.showError("Error", "Error al conectar con el servidor", "Intentelo de nuevo más tarde");
                else
                    Dialog.showInformation("¡Éxito!", "Pudimos conectar con el servidor", "Ahora puede registrarse/loguearse");
            }
        });
        btn_login.setOnAction(event -> {
            if (connection != null) {
                if (user != null) {
                    if(user instanceof Client client){

                    }else if(user instanceof Manager manager){

                    }
                } else {
                    Dialog.showWarning("Error", "No se pudo loguear", "Seleccione si quiere entrar como operario o cliente");
                }
            } else {
                Dialog.showError("Error", "No hay conexión con el servidor", "Pulsa Archivo-> Conectar con servidor para intentar de nuevo una conexión");
            }
        });
        btn_register.setOnAction(event -> {
            if (connection != null) {
                if (user != null) {
                    if(user instanceof Client client){

                    }else if(user instanceof Manager manager){

                    }
                } else {
                    Dialog.showWarning("Error", "No se pudo registrar", "Seleccione si quiere entrar como operario o cliente");
                }
            } else {
                Dialog.showError("Error", "No hay conexión con el servidor", "Pulsa Archivo-> Conectar con servidor para intentar de nuevo una conexión");
            }
        });
        mi_about.setOnAction(event -> {
            App.loadScene(new Stage(), "about", "Sobre la aplicación", true, false);
        });
        mi_close.setOnAction(event -> {
            if(connection!=null)
                SocketService.closeConnection();
            ((Stage) btn_login.getScene().getWindow()).close();
        });
        Platform.runLater(()->{
            btn_login.getScene().getWindow().setOnCloseRequest(event -> {
                mi_close.fire();
            });
        });
    }

    private void setIcons() {
        mi_connect.setGraphic(Tools.getIcon("sync"));
        mi_close.setGraphic(Tools.getIcon("close"));
        mi_about.setGraphic(Tools.getIcon("info"));
    }

}
