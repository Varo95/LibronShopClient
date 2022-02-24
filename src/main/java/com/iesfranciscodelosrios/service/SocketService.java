package com.iesfranciscodelosrios.service;

import com.iesfranciscodelosrios.App;
import com.iesfranciscodelosrios.controllers.LoginController;
import com.iesfranciscodelosrios.controllers.MenuController;
import com.iesfranciscodelosrios.model.Book;
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
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SocketService {

    private static Socket server;

    public static void connectToServer() {
        try {
            server = new Socket(Tools.readIPFromTxt(), 1995);
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
                        LoginController.loginStage.close();
                        if(MenuController.menuStage == null)
                            MenuController.menuStage = new Stage();
                        App.loadScene(MenuController.menuStage, "menu", " Libron Shop", true, true);
                    });
                } else if (o.containsKey(Operations.ServerActions.SendAddBook)) {
                    Platform.runLater(() -> MenuController.setContentOnView(Operations.UserOptions.AddBook, null, (String[]) o.get(Operations.ServerActions.SendAddBook)));
                } else if (o.containsKey(Operations.ServerActions.SendProfile)) {
                    Map<String, Client> object = (Map<String, Client>) o.get(Operations.ServerActions.SendProfile);
                    Platform.runLater(() -> MenuController.setContentOnView(Operations.UserOptions.ChargeAccount, List.of(object), null));
                } else if (o.containsKey(Operations.ServerActions.OperationOkButNoContent)) {
                    Platform.runLater(()-> Dialog.showError("Error","Hubo un problema en el servidor","Contacte con los desarrolladores"));
                } else if (o.containsKey(Operations.ServerActions.UserAlreadyExist)) {
                    Platform.runLater(() -> Dialog.showWarning("¡Error", "No pudiste registrarte con ese email", "Ya existe, prueba a iniciar sesion o registrarte con otro correo"));
                } else if (o.containsKey(Operations.ServerActions.SendBooksToPurchase)) {
                    Platform.runLater(() -> MenuController.setContentOnView(Operations.UserOptions.ViewOnStockBooks, (List) o.get(Operations.ServerActions.SendBooksToPurchase), null));
                }else if (o.containsKey(Operations.ServerActions.SendPurchaseHistory)) {
                    Map<LocalDateTime, Book> responseFromServer = (Map<LocalDateTime, Book>) o.get(Operations.ServerActions.SendPurchaseHistory);
                    Platform.runLater(() -> MenuController.setContentOnView(Operations.UserOptions.ViewPurchaseHistory, List.of(responseFromServer) , null));
                }else if (o.containsKey(Operations.ServerActions.NotEnoughBalance)) {
                    Platform.runLater(() -> Dialog.showInformation("¡Error!", "No pudiste comprar el libro", "No dispones de suficiente saldo en la cuenta"));
                } else if (o.containsKey(Operations.ServerActions.WrongPassword)) {
                    Platform.runLater(() -> Dialog.showInformation("¡Error!", "No pudiste loguearte", "Las contraseñas no coinciden"));
                } else if (o.containsKey(Operations.ServerActions.IncorrectUserType)) {
                    Platform.runLater(() -> Dialog.showInformation("¡Error!", "Intentaste iniciar sesion con otro tipo de usuario", "Prueba a loguearte como otro tipo de usuario"));
                } else if(o.containsKey(Operations.ServerActions.SendMenuBooks)){
                    Platform.runLater(()-> MenuController.setContentOnView(Operations.UserOptions.ChangeStock, (List)o.get(Operations.ServerActions.SendMenuBooks), null));
                } else if (o.containsKey(Operations.ServerActions.EmailDoesntExistOnDB)) {
                    Platform.runLater(() -> Dialog.showInformation("¡Error!", "No pudiste loguearte", "Este email no existe en la base de datos"));
                } else if (o.containsKey(Operations.ServerActions.OperationOk)) {
                    Object object = o.get(Operations.ServerActions.OperationOk);
                    if (object instanceof Client)
                        Platform.runLater(() -> Dialog.showInformation("¡Éxito!", "Te has registrado correctamente como cliente", "Ahora puedes iniciar sesión"));
                    if (object instanceof Manager)
                        Platform.runLater(() -> Dialog.showInformation("¡Éxito!", "Te has registrado correctamente como librero ", "Ahora puedes iniciar sesión"));
                    if (object instanceof Book b)
                        Platform.runLater(() -> Dialog.showInformation("¡Éxito!", "Añadiste el libro: " + b.getTitle(), "Esta respuesta llegó desde el servidor"));
                    if (object instanceof LinkedHashMap<?, ?> c)
                        c.forEach((o1, o2) -> {
                            MenuController.setUser((User) o1);
                            Book b = (Book) o2;
                            Platform.runLater(() -> Dialog.showInformation("¡Éxito!", "Compraste el libro: " + b.getTitle() + "\nAutor: " + ((b.getAuthor() == null) ? "Anónimo" : b.getAuthor()) + "\nPor: " + b.getPrice() + " €", "¡Disfrútalo!"));
                        });
                    if(object instanceof Double account && !o.containsKey(Operations.ServerActions.NewBalance)){
                        Platform.runLater(()-> MenuController.setContentOnView(Operations.UserOptions.ViewAccount, List.of(account), null));
                    }else if(object instanceof Double newBalance && o.containsKey(Operations.ServerActions.NewBalance)){
                        MenuController.setUser((User) o.get(Operations.ServerActions.NewBalance));
                        Platform.runLater(() -> Dialog.showInformation("¡Éxito!", "Tu nuevo saldo es de: " + (Math.floor(newBalance * 100) / 100)+" €", "Esta respuesta llegó desde el servidor"));
                    }
                }
            } catch (EOFException e) {
                Platform.runLater(() -> Dialog.showWarning("¡Aviso!", "Hubo un error en la conexión al recibir la petición del servidor", ""));
                if (objectInputStream != null)
                    objectInputStream.close();
                throw new SocketException("El servidor me ha desconectado");
            }
        }
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
