package com.iesfranciscodelosrios.controllers;

import com.iesfranciscodelosrios.utils.Dialog;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AboutController {
    @FXML
    private Button github;

    @FXML
    protected void initialize() {
        github.setOnAction(event -> {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    URI uri;
                    try {
                        uri = new URI("https://github.com/Varo95/LibronShopClient");
                        desktop.browse(uri);
                    } catch (URISyntaxException | IOException e) {
                        Dialog.showWarning("Lo sentimos","No se pudo abrir el enlace","Contacte con los desarrolladores");
                    }
                }
            }
        });
    }

}
