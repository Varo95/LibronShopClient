module LibronShopClient {
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.base;
    requires java.sql;
    requires org.jfxtras.styles.jmetro;

    opens com.iesfranciscodelosrios.controllers to javafx.fxml, javafx.controls, javafx.graphics, javafx.media, javafx.base;
    exports com.iesfranciscodelosrios;
}