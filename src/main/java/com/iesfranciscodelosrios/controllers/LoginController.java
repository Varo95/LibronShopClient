package com.iesfranciscodelosrios.controllers;

import com.iesfranciscodelosrios.App;
import com.iesfranciscodelosrios.model.Client;
import com.iesfranciscodelosrios.model.Manager;
import com.iesfranciscodelosrios.model.User;
import com.iesfranciscodelosrios.service.SocketService;
import com.iesfranciscodelosrios.utils.Dialog;
import com.iesfranciscodelosrios.utils.Operations;
import com.iesfranciscodelosrios.utils.Tools;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedHashMap;

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
    private TextField tf_email;
    @FXML
    private PasswordField tf_passwd;
    @FXML
    private ToggleGroup userType;
    @FXML
    private RadioButton rbtn_user;
    private Socket connection;
    private User user;
    public static Stage loginStage;

    @FXML
    protected void initialize() {
        Platform.runLater(()-> loginStage = (Stage) tf_email.getScene().getWindow());
        connection = SocketService.getConnectionToServer();
        setButtonActions();
        setIcons();
        userType.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue instanceof RadioButton button) {
                user = null;
                if (button.getText().equals("Usuario")) {
                    user = new Client();
                    user.setManager(false);
                } else if (button.getText().equals("Operario")) {
                    user = new Manager();
                    user.setManager(true);
                }
            }
        });
        userType.selectToggle(rbtn_user);
        tf_email.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) btn_login.fire();
        });
        tf_passwd.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) btn_login.fire();
        });
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
            if (connection != null && user != null) {
                user.setEmail(tf_email.getText().replace("\n", ""));
                user.setPassword(Tools.encryptSHA256(tf_passwd.getText().replace("\n", "")));
                try {
                    LinkedHashMap<Operations.UserOptions, User> operation = new LinkedHashMap<>();
                    operation.put(Operations.UserOptions.Login, user);
                    SocketService.sendDataToServer(operation);
                }catch (IOException e){
                    Dialog.showError("Error","Se perdió la conexión con el servidor a la hora de enviarse el objeto","");
                }
            } else {
                Dialog.showError("Error", "No hay conexión con el servidor", "Pulsa Archivo-> Conectar con servidor para intentar de nuevo una conexión");
            }
        });
        btn_register.setOnAction(event -> {
            if (connection != null && user != null) {
                user.setEmail(tf_email.getText());
                user.setPassword(Tools.encryptSHA256(tf_passwd.getText()));
                if(user instanceof Client c)
                    c.setBalance(0.0);
                try {
                    LinkedHashMap<Operations.UserOptions, User> operation = new LinkedHashMap<>();
                    operation.put(Operations.UserOptions.Register, user);
                    SocketService.sendDataToServer(operation);
                }catch (IOException e){
                    e.printStackTrace();
                }
            } else {
                Dialog.showError("Error", "No hay conexión con el servidor", "Pulsa Archivo-> Conectar con servidor para intentar de nuevo una conexión");
            }
        });
        mi_about.setOnAction(event -> App.loadScene(new Stage(), "about", "Sobre la aplicación", true, false));
        mi_close.setOnAction(event -> {
            if(connection!=null)
                SocketService.closeConnection();
            ((Stage) btn_login.getScene().getWindow()).close();
        });
        Platform.runLater(()-> btn_login.getScene().getWindow().setOnCloseRequest(event -> mi_close.fire()));
    }

    private void setIcons() {
        mi_connect.setGraphic(Tools.getIcon("sync"));
        mi_close.setGraphic(Tools.getIcon("close"));
        mi_about.setGraphic(Tools.getIcon("info"));
    }

}
