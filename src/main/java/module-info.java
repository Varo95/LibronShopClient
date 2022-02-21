module LibronShopClient {
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires java.base;
    requires org.jfxtras.styles.jmetro;
    requires mail;

    opens com.iesfranciscodelosrios.controllers to javafx.fxml, javafx.controls, javafx.graphics, javafx.media;
    exports com.iesfranciscodelosrios;
}