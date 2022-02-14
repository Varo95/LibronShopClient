package com.iesfranciscodelosrios.service;

import com.iesfranciscodelosrios.utils.Dialog;
import javafx.application.Platform;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

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
                if(!server.isClosed()) {
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
        if(server!=null && !server.isClosed()) {
            ObjectInputStream objectInputStream = null;
            try {
                objectInputStream = new ObjectInputStream(server.getInputStream());
                Object o = objectInputStream.readObject();
            }catch (EOFException e){
                Platform.runLater(()->{Dialog.showWarning("¡Aviso!","Hubo un error en la conexión al recibir la petición del servidor","");});
                if(objectInputStream!=null)
                    objectInputStream.close();
                throw new SocketException("El servidor me ha desconectado");
            }
            //cosas que hacer
        }
        //condiciones de las peticiones recibidas por el servidor
    }

    public static void closeConnection() {
        closeServer(server, false);
    }

    private static void closeServer(Socket server, boolean isFromException) {
        try {
            server.getOutputStream().close();
            server.close();
            Platform.runLater(() -> {
                if(isFromException)
                    Dialog.showError("Error","Se cerró la conexión con el servidor","Vuelva a conectarse lo antes posible, de lo contrario no podrá hacer nada");
                else
                    Dialog.showInformation("¡Éxito!","Se cerró la conexión con el servidor","Se pudo cerrar la conexión correctamente");
            });
        } catch (IOException e) {
            Platform.runLater(() -> Dialog.showError("Error al cerrar la conexión con el servidor", "Hubo una excepción al intentar cerrar la conexión", "Contacte con los desarrolladores"));
        }
    }
}
