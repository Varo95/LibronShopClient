package com.iesfranciscodelosrios.utils;

import com.iesfranciscodelosrios.App;
import com.iesfranciscodelosrios.controllers.MenuController;
import com.iesfranciscodelosrios.model.User;
import com.iesfranciscodelosrios.service.SocketService;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import jfxtras.styles.jmetro.MDL2IconFont;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class Tools {

    private static List<Button> userOptions;

    public static void setUserOptions(List<Button> list) {
        userOptions = list;
    }

    public static List<Button> getUserOptions() {
        if (userOptions == null)
            userOptions = new ArrayList<>();
        return userOptions;
    }

    public static String selectImg() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.bmp", "*.gif", "*.jpg", "*.jpeg", "*.png"));
        fc.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {
            return selectedFile.getAbsolutePath();
        } else {
            Dialog.showWarning("Advertencia", "", "Fichero no seleccionado!");
            return null;
        }
    }

    public static boolean ValidateFile_img(String url) {
        boolean result = switch (url.toLowerCase().substring(url.length() - 4, url.length())) {
            case ".bmp", ".gif", ".jpg", ".png" -> true;
            default -> false;
        };
        if (!result) {
            if (url.endsWith(".jpeg")) {
                result = true;
            }
        }
        return result;
    }

    public static Image getImage(String resPath) {
        if (!Pattern.compile("(http)?s?:?(\\/\\/[^\"']*\\.(?:bmp|gif|jpg|jpeg|png))").matcher(resPath).matches()) {
            File f = new File(resPath);
            if (f.exists() && f.isFile())
                if (ValidateFile_img(f.getName()))
                    return new Image(Objects.requireNonNull(f.getAbsoluteFile().getAbsolutePath()));
        } else if (Pattern.compile("(http)?s?:?(\\/\\/[^\"']*\\.(?:bmp|gif|jpg|jpeg|png))").matcher(resPath).matches()) {
            return new Image(resPath);
        }
        return null;
    }

    public static Image decodeBase64Img(String img) {
        return new Image(Base64.getDecoder().wrap(new ByteArrayInputStream(img.getBytes())));
    }

    public static String encodeBase64Img(Image img) {
        try {
            return Base64.getEncoder().encodeToString(Files.readAllBytes(new File(img.getUrl()).toPath()));
        } catch (IOException e) {
            Platform.runLater(() -> Dialog.showWarning("Error", "No se pudo enviar correctamente la portada", "Pongase en contacto con los desarrolladores"));
        }
        return "";
    }

    public static boolean onlyValidEmail(String email){
        try {
            new InternetAddress(email).validate();return true;
        } catch (AddressException ex) {
            return false;
        }
    }

    public static void onlyDoubleValue(TextField tf) {
        tf.setTextFormatter(new TextFormatter<>(new StringConverter<>() {
            @Override
            public Double fromString(String s) {
                if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s))
                    return 0.0;
                else
                    return Double.valueOf(s);
            }

            @Override
            public String toString(Double d) {
                return d.toString();
            }
        }, 0.0, c -> Pattern.compile("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?").matcher(c.getControlNewText()).matches() ? c : null));
    }

    public static void setMenuOptions(String[] menu, User client) {
        List<Button> menuButtons = new ArrayList<>();
        MenuController.setUser(client);
        for (String option : menu) {
            Button btn_option = new Button(option);
            btn_option.setAlignment(Pos.CENTER);
            switch (option) {
                case "Añadir libros" -> btn_option.setOnAction(event -> {
                    LinkedHashMap<Operations.UserOptions, Object> sendMsgToServer = new LinkedHashMap<>();
                    sendMsgToServer.put(Operations.UserOptions.AddBook, client);
                    try {
                        SocketService.sendDataToServer(sendMsgToServer);
                    } catch (IOException e) {
                        Platform.runLater(() -> Dialog.showError("Error", "No hay conexión con el servidor", "Pulsa Archivo-> Conectar con servidor para intentar de nuevo una conexión"));
                    }
                });
                case "Eliminar libros" -> btn_option.setOnAction(event -> {
                    LinkedHashMap<Operations.UserOptions, Object> sendMsgToServer = new LinkedHashMap<>();
                    sendMsgToServer.put(Operations.UserOptions.DeleteBook, client);
                    try {
                        SocketService.sendDataToServer(sendMsgToServer);
                    } catch (IOException e) {
                        Platform.runLater(() -> Dialog.showError("Error", "No hay conexión con el servidor", "Pulsa Archivo-> Conectar con servidor para intentar de nuevo una conexión"));
                    }
                });
                case "Cambiar stock" -> btn_option.setOnAction(event -> {
                    LinkedHashMap<Operations.UserOptions, Object> sendMsgToServer = new LinkedHashMap<>();
                    sendMsgToServer.put(Operations.UserOptions.ChangeStock, client);
                    try {
                        SocketService.sendDataToServer(sendMsgToServer);
                    } catch (IOException e) {
                        Platform.runLater(() -> Dialog.showError("Error", "No hay conexión con el servidor", "Pulsa Archivo-> Conectar con servidor para intentar de nuevo una conexión"));
                    }
                });
                case "Gestionar Clientes" -> btn_option.setOnAction(event -> {
                    LinkedHashMap<Operations.UserOptions, Object> sendMsgToServer = new LinkedHashMap<>();
                    sendMsgToServer.put(Operations.UserOptions.SeeClients, client);
                    try {
                        SocketService.sendDataToServer(sendMsgToServer);
                    } catch (IOException e) {
                        Platform.runLater(() -> Dialog.showError("Error", "No hay conexión con el servidor", "Pulsa Archivo-> Conectar con servidor para intentar de nuevo una conexión"));
                    }
                });
                case "Ver libros disponibles" -> btn_option.setOnAction(event -> {
                    LinkedHashMap<Operations.UserOptions, Object> sendMsgToServer = new LinkedHashMap<>();
                    sendMsgToServer.put(Operations.UserOptions.ViewOnStockBooks, client);
                    try {
                        SocketService.sendDataToServer(sendMsgToServer);
                    } catch (IOException e) {
                        Platform.runLater(() -> Dialog.showError("Error", "No hay conexión con el servidor", "Pulsa Archivo-> Conectar con servidor para intentar de nuevo una conexión"));
                    }
                });
                case "Ver Historial pedidos" -> btn_option.setOnAction(event -> {
                    LinkedHashMap<Operations.UserOptions, Object> sendMsgToServer = new LinkedHashMap<>();
                    sendMsgToServer.put(Operations.UserOptions.ViewPurchaseHistory, client);
                    try {
                        SocketService.sendDataToServer(sendMsgToServer);
                    } catch (IOException e) {
                        Platform.runLater(() -> Dialog.showError("Error", "No hay conexión con el servidor", "Pulsa Archivo-> Conectar con servidor para intentar de nuevo una conexión"));
                    }
                });
                case "Ver Saldo" -> btn_option.setOnAction(event -> {
                    LinkedHashMap<Operations.UserOptions, Object> sendMsgToServer = new LinkedHashMap<>();
                    sendMsgToServer.put(Operations.UserOptions.ViewAccount, client);
                    try {
                        SocketService.sendDataToServer(sendMsgToServer);
                    } catch (IOException e) {
                        Platform.runLater(() -> Dialog.showError("Error", "No hay conexión con el servidor", "Pulsa Archivo-> Conectar con servidor para intentar de nuevo una conexión"));
                    }
                });
                case "Ingresar Saldo" -> btn_option.setOnAction(event -> {
                    LinkedHashMap<Operations.UserOptions, Object> sendMsgToServer = new LinkedHashMap<>();
                    sendMsgToServer.put(Operations.UserOptions.ChargeAccount, client);
                    try {
                        SocketService.sendDataToServer(sendMsgToServer);
                    } catch (IOException e) {
                        Platform.runLater(() -> Dialog.showError("Error", "No hay conexión con el servidor", "Pulsa Archivo-> Conectar con servidor para intentar de nuevo una conexión"));
                    }
                });
            }
            menuButtons.add(btn_option);
        }
        setUserOptions(menuButtons);
    }

    public static void addCssAndIcon(Stage window) {
        window.getScene().getStylesheets().add(String.valueOf(App.class.getResource("dark.css")));
        window.getIcons().add(new Image(Objects.requireNonNull(App.class.getResourceAsStream("icon.png"))));
    }

    public static String encryptSHA256(String s) {
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA256");
            md.update(s.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : md.digest()) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            result = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            Dialog.showError("Error", "Algo salió mal al encriptar la contraseña", "Vuelva a intentarlo y si se repite, contacte con los desarrolladores");
        }
        return result;
    }

    /**
     * This method is used to get icons and put them into a window, making less code and more readable to coders
     * Codes are extracted from Microsoft official page -> https://docs.microsoft.com/en-us/windows/apps/design/style/segoe-ui-symbol-font
     */
    //Para añadir nuevos iconos consultar la página de Microsoft-> https://docs.microsoft.com/en-us/windows/apps/design/style/segoe-ui-symbol-font
    //Añadir "\\u" al principio (al igual que el \n imprime salto de línea) para indicar que es un icono gráfico de Windows.
    public static MDL2IconFont getIcon(String value) {
        MDL2IconFont result = switch (value) {
            case "close" -> new MDL2IconFont("\uE711");
            case "cloud" -> {
                result = new MDL2IconFont("\uE753");
                result.setStyle("-fx-text-fill: #00a8a0;");
                yield result;
            }
            case "info" -> {
                result = new MDL2IconFont("\uE946");
                result.setStyle("-fx-text-fill: lightblue;");
                yield result;
            }
            case "arrow-next" -> {
                result = new MDL2IconFont("\uF0D3");
                result.setStyle("-fx-text-fill: lightblue;");
                yield result;
            }
            case "arrow-back" -> {
                result = new MDL2IconFont("\uF0D2");
                result.setStyle("-fx-text-fill: lightblue;");
                yield result;
            }
            case "add" -> {
                result = new MDL2IconFont("\uECC8");
                result.setStyle("-fx-text-fill: lightgreen;");
                yield result;
            }
            case "remove" -> {
                result = new MDL2IconFont("\uECC9");
                result.setStyle("-fx-text-fill: red;");
                yield result;
            }
            case "edit" -> {
                result = new MDL2IconFont("\uEC87");
                result.setStyle("-fx-text-fill: orange;");
                yield result;
            }
            case "close-session" -> {
                result = new MDL2IconFont("\uF3B1");
                yield result;
            }
            case "profile" -> {
                result = new MDL2IconFont("\uE77B");
                yield result;
            }
            case "upload" -> {
                result = new MDL2IconFont("\uE898");
                result.setStyle("-fx-text-fill: lightgreen;");
                yield result;
            }
            case "sync" -> {
                result = new MDL2IconFont("\uE895");
                result.setStyle("-fx-text-fill: lightgreen;");
                yield result;
            }
            default -> new MDL2IconFont("\uF16B");
        };
        return result;
    }
}
