package com.iesfranciscodelosrios.utils;

import com.iesfranciscodelosrios.App;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.MDL2IconFont;

import java.util.Objects;

public class Tools {

    public static void addCssAndIcon(Stage window) {
        window.getScene().getStylesheets().add(String.valueOf(App.class.getResource("dark.css")));
        window.getIcons().add(new Image(Objects.requireNonNull(App.class.getResourceAsStream("icon.png"))));
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
