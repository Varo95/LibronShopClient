package com.iesfranciscodelosrios.utils;

import com.iesfranciscodelosrios.App;
import javafx.stage.Stage;

public class Tools {

    public static void addCssAndIcon(Stage window) {
        window.getScene().getStylesheets().add(String.valueOf(App.class.getResource("dark.css")));
        /*window.getIcons().add(getImage("icon.png", true));*/
    }
}
