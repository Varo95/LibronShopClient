package com.iesfranciscodelosrios.service;

import com.iesfranciscodelosrios.App;
import com.iesfranciscodelosrios.controllers.LoginController;
import com.iesfranciscodelosrios.controllers.MenuController;
import com.iesfranciscodelosrios.model.Client;
import com.iesfranciscodelosrios.model.Manager;
import com.iesfranciscodelosrios.model.User;
import com.iesfranciscodelosrios.utils.Dialog;
import com.iesfranciscodelosrios.utils.Operations;
import com.iesfranciscodelosrios.utils.Tools;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedHashMap;
import java.util.List;

public class SocketService {

    private static Socket server;

    public static void connectToServer() {
        try {
            server = new Socket("localhost", 1995);
            readServerInputs(server);
        } catch (IOException e) {
            Dialog.showError("Error", "Conexión rechazada", "Revise que el servidor está online");
        }
    }

    public static Socket getConnectionToServer() {
        return server;
    }

    public static void readServerInputs(Socket server) {
        new Thread(() -> {
            try {
                while (!server.isClosed())
                    listenToServerActions(server);
            } catch (IOException | ClassNotFoundException e) {
                if (!server.isClosed()) {
                    Platform.runLater(() -> {
                        if (e instanceof SocketException) {
                            Dialog.showWarning("¡Aviso!", "El servidor se ha desonectado", "Voy a parar cerrar la conexión");
                        } else if (e instanceof IOException)
                            Dialog.showError("Error", "Hubo un error durante la conexión", "Contacte con los desarrolladores");
                        if (e instanceof ClassNotFoundException)
                            Dialog.showWarning("¡Aviso!", "El servidor envió un mensaje que no es capaz de leer el cliente", "Revise que tiene el cliente actualizado");
                    });
                    closeServer(server, true);
                }
            }
        }).start();
    }

    public static void listenToServerActions(Socket server) throws IOException, ClassNotFoundException {
        if (server != null && !server.isClosed()) {
            ObjectInputStream objectInputStream = null;
            try {
                objectInputStream = new ObjectInputStream(server.getInputStream());
                LinkedHashMap<Operations.ServerActions, Object> o = (LinkedHashMap<Operations.ServerActions, Object>) objectInputStream.readObject();
                if (o.containsKey(Operations.ServerActions.SendMenu)) {
                    Tools.setMenuOptions((String[]) o.get(Operations.ServerActions.SendMenu), (User) o.get(Operations.ServerActions.OperationOk));
                    Platform.runLater(() -> {
                        if (LoginController.loginStage != null) {
                            LoginController.loginStage.close();
                        }
                        App.loadScene(new Stage(), "menu", " Libron Shop", true, true);
                    });
                } else if (o.containsKey(Operations.ServerActions.SendSubmenu)) {
                    Platform.runLater(()->{
                        MenuController.setContentOnView(Operations.UserOptions.AddBook, null, (String[]) o.get(Operations.ServerActions.SendSubmenu));
                    });
                } else if (o.containsKey(Operations.ServerActions.OperationOkButNoContent)) {

                } else if (o.containsKey(Operations.ServerActions.UserAlreadyExist)) {

                } else if (o.containsKey(Operations.ServerActions.NotEnoughBalance)) {
                    Platform.runLater(() -> Dialog.showInformation("¡Error!", "No pudiste comprar el libro", "No dispones de suficiente saldo en la cuenta"));
                } else if (o.containsKey(Operations.ServerActions.WrongPassword)) {
                    Platform.runLater(() -> Dialog.showInformation("¡Error!", "No pudiste loguearte", "Las contraseñas no coinciden"));
                } else if (o.containsKey(Operations.ServerActions.IncorrectUserType)) {
                    Platform.runLater(() -> Dialog.showInformation("¡Error!", "Intentaste iniciar sesion con otro tipo de usuario", "Prueba a loguearte como otro tipo de usuario"));
                } else if (o.containsKey(Operations.ServerActions.EmailDoesntExistOnDB)) {
                    Platform.runLater(() -> Dialog.showInformation("¡Error!", "No pudiste loguearte", "Este email no existe en la base de datos"));
                } else if (o.containsKey(Operations.ServerActions.OperationOk)) {
                    Object object = o.get(Operations.ServerActions.OperationOk);
                    if (object instanceof Client)
                        Platform.runLater(() -> Dialog.showInformation("¡Éxito!", "Te has registrado correctamente como cliente", "Ahora puedes iniciar sesión"));
                    if (object instanceof Manager)
                        Platform.runLater(() -> Dialog.showInformation("¡Éxito!", "Te has registrado correctamente como librero ", "Ahora puedes iniciar sesión"));
                    if (object instanceof List b) {
                        //todo cosas
                    }
                }
            } catch (EOFException e) {
                Platform.runLater(() -> Dialog.showWarning("¡Aviso!", "Hubo un error en la conexión al recibir la petición del servidor", ""));
                if (objectInputStream != null)
                    objectInputStream.close();
                throw new SocketException("El servidor me ha desconectado");
            }
            //cosas que hacer
        }
        //condiciones de las peticiones recibidas por el servidor
    }

    public static void sendDataToServer(Object o) throws IOException {
        if (server != null && !server.isClosed()) {
            ObjectOutputStream objectOutputStream = null;
            try {
                objectOutputStream = new ObjectOutputStream(server.getOutputStream());
                objectOutputStream.writeObject(o);
            } catch (EOFException e) {
                Platform.runLater(() -> {
                    Dialog.showWarning("¡Aviso!", "Hubo un error en la conexión al recibir la petición del servidor", "");
                });
                if (objectOutputStream != null)
                    objectOutputStream.close();
                throw new SocketException("El servidor me ha desconectado");
            }
        }
    }

    public static void closeConnection() {
        closeServer(server, false);
    }

    private static void closeServer(Socket server, boolean isFromException) {
        try {
            server.getOutputStream().close();
            server.close();
            Platform.runLater(() -> {
                if (isFromException)
                    Dialog.showError("Error", "Se cerró la conexión con el servidor", "Vuelva a conectarse lo antes posible, de lo contrario no podrá hacer nada");
                else
                    Dialog.showInformation("¡Éxito!", "Se cerró la conexión con el servidor", "Se pudo cerrar la conexión correctamente");
            });
        } catch (IOException e) {
            Platform.runLater(() -> Dialog.showError("Error al cerrar la conexión con el servidor", "Hubo una excepción al intentar cerrar la conexión", "Contacte con los desarrolladores"));
        }
    }
}
