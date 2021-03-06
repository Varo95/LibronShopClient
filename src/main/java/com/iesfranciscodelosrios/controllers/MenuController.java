package com.iesfranciscodelosrios.controllers;

import com.iesfranciscodelosrios.model.Book;
import com.iesfranciscodelosrios.model.Client;
import com.iesfranciscodelosrios.model.User;
import com.iesfranciscodelosrios.service.SocketService;
import com.iesfranciscodelosrios.utils.Dialog;
import com.iesfranciscodelosrios.utils.Operations;
import com.iesfranciscodelosrios.utils.Tools;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MenuController {
    @FXML
    private MenuItem mi_connect;
    public static Stage menuStage;
    @FXML
    private MenuItem mi_close_session;
    @FXML
    private GridPane content;
    @FXML
    private GridPane menuOptions;
    @FXML
    private ScrollPane scrollPane;
    private Socket connection;

    private static User user;

    @FXML
    protected void initialize() {
        Platform.runLater(() -> menuStage = (Stage) menuOptions.getScene().getWindow());
        connection = SocketService.getConnectionToServer();
        setIcons();
        setButtonActions();
        for (int i = 0; i < Tools.getUserOptions().size(); i++) {
            menuOptions.addRow(i, Tools.getUserOptions().get(i));
            GridPane.setHalignment(menuOptions.getChildren().get(i), HPos.CENTER);
        }
        content.setOnScroll(event -> {
            double deltaY = event.getDeltaY() * 3;
            double width = scrollPane.getContent().getBoundsInLocal().getWidth();
            double vvalue = scrollPane.getVvalue();
            scrollPane.setVvalue(vvalue + -deltaY / width);
        });
    }

    public static synchronized void setUser(User u) {
        user = u;
    }

    public static void setBlankContent() {
        if (menuStage != null) {
            GridPane content = (GridPane) menuStage.getScene().lookup("#content");
            content.getChildren().removeIf(Objects::nonNull);
        }
    }

    private void setButtonActions() {
        mi_connect.setOnAction(event -> {
            if (connection == null || connection.isClosed()) {
                SocketService.connectToServer();
                connection = SocketService.getConnectionToServer();
                if (connection == null || connection.isClosed()) {
                    Dialog.showError("Error", "Error al conectar con el servidor", "Intentelo de nuevo m??s tarde");
                } else {
                    Dialog.showInformation("????xito!", "Pudimos conectar con el servidor", "Ahora puede registrarse/loguearse");
                }
            }
        });
        mi_close_session.setOnAction(event -> {
            if (user != null)
                user = null;
            setBlankContent();
            Platform.runLater(() -> {
                menuStage.close();
                if (!LoginController.loginStage.isShowing())
                    LoginController.loginStage.show();
            });
        });
        Platform.runLater(() -> menuOptions.getScene().getWindow().setOnCloseRequest(event -> mi_close_session.fire()));
    }

    private void setIcons() {
        mi_connect.setGraphic(Tools.getIcon("sync"));
        mi_close_session.setGraphic(Tools.getIcon("close-session"));
    }

    public static void setContentOnView(Operations.UserOptions menuToLoadOption, List objects, String[] menu) {
        if (menuStage != null) {
            GridPane content = (GridPane) menuStage.getScene().lookup("#content");
            if (menuToLoadOption.equals(Operations.UserOptions.AddBook)) {
                setContentAddBook(content, menu);
            } else if (menuToLoadOption.equals(Operations.UserOptions.ViewOnStockBooks)) {
                setContentViewBooks(content, (List<Book>) objects);
            } else if (menuToLoadOption.equals(Operations.UserOptions.ViewAccount)) {
                setContentViewAccount(content, (double) objects.get(0));
            } else if (menuToLoadOption.equals(Operations.UserOptions.ChargeAccount)) {
                setContentChargeAccount(content, (Map<String, Client>) objects.get(0));
            } else if (menuToLoadOption.equals(Operations.UserOptions.ViewPurchaseHistory)) {
                setContentViewHistoricalData(content, (Map<LocalDateTime, Book>) objects.get(0));
            } else if (menuToLoadOption.equals(Operations.UserOptions.ChangeStock)) {
                setContentChangeStockBooks(content, (List<Book>) objects);
            }
        }
    }

    private static void setContentAddBook(GridPane content, String[] menu) {
        //Portada
        setBlankContent();
        ImageView cover = new ImageView();
        cover.setFitHeight(267);
        cover.setFitWidth(200);
        cover.setImage(Tools.decodeBase64Img(menu[0]));
        content.addRow(0, cover);
        content.addRow(1, new Label(menu[1]));
        TextField tf_cover = new TextField();
        tf_cover.textProperty().addListener(onChange -> {
            Image i = Tools.getImage(tf_cover.getText());
            cover.setImage(Objects.requireNonNullElse(i, Tools.decodeBase64Img(menu[0])));
        });
        content.addRow(2, tf_cover);
        Button examineImg = new Button("Examinar");
        examineImg.setOnAction(event -> {
            String file = Tools.selectImg();
            ((TextField) content.getChildren().get(2)).setText((file == null) ? "" : file);
        });
        content.addRow(2, examineImg);
        //Titulo
        content.addRow(3, new Label(menu[2]));
        content.addRow(4, new TextField());
        //Autor
        content.addRow(5, new Label(menu[3]));
        content.addRow(6, new TextField());
        //fechaSalida
        content.addRow(7, new Label(menu[4]));
        content.addRow(8, new DatePicker(LocalDate.now()));
        //Precio
        content.addRow(9, new Label(menu[5]));
        TextField tf_price = new TextField();
        Tools.onlyDoubleValue(tf_price);
        content.addRow(10, tf_price);
        //Stock
        content.addRow(11, new CheckBox(menu[6]));
        Button send = new Button("Enviar");
        content.addRow(12, send);
        for (Node n : content.getChildren()) {
            GridPane.setHalignment(n, HPos.CENTER);
        }
        send.setOnAction(event -> {
            Book bookToSave = new Book();
            bookToSave.setFrontPage(Tools.encodeBase64Img(cover.getImage()));
            bookToSave.setTitle(((TextField) content.getChildren().get(5)).getText());
            bookToSave.setAuthor(((TextField) content.getChildren().get(7)).getText());
            bookToSave.setReleasedDate(((DatePicker) content.getChildren().get(9)).getValue().atStartOfDay());
            bookToSave.setPrice(Double.valueOf(((TextField) content.getChildren().get(11)).getText()));
            bookToSave.setStock(((CheckBox) content.getChildren().get(12)).isSelected());
            LinkedHashMap<Operations.UserOptions, Object> operation = new LinkedHashMap<>();
            operation.put(Operations.UserOptions.AddBookAction, bookToSave);
            try {
                SocketService.sendDataToServer(operation);
                cover.setImage(Tools.decodeBase64Img(menu[0]));
                tf_cover.clear();
                ((TextField) content.getChildren().get(5)).clear();
                ((TextField) content.getChildren().get(7)).clear();
                ((TextField) content.getChildren().get(11)).clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static void setContentViewBooks(GridPane content, List<Book> books) {
        int columnCount = 0;
        int rowCount = 0;
        setBlankContent();
        for (Book b : books) {
            GridPane gridForBook = new GridPane();
            gridForBook.setVgap(15);
            if (columnCount > 2) {
                columnCount = 0;
                rowCount++;
            }
            ImageView cover = new ImageView();
            cover.setFitHeight(275);
            cover.setFitWidth(180);
            cover.setImage(Tools.decodeBase64Img(b.getFrontPage()));
            gridForBook.addRow(0, cover);
            gridForBook.addRow(1, new Label("T??tulo: " + b.getTitle()));
            gridForBook.addRow(2, new Label("Autor: " + ((b.getAuthor() == null || b.getAuthor().equals("")) ? "An??nimo" : b.getAuthor())));
            gridForBook.addRow(3, new Label("Precio: " + b.getPrice() + "???"));
            Button btn_buy = new Button("Comprar");
            btn_buy.setOnAction(event -> {
                LinkedHashMap<Operations.UserOptions, Object> mapToSend = new LinkedHashMap<>();
                Map<User, Book> user_book = new HashMap<>();
                user_book.put(user, b);
                mapToSend.put(Operations.UserOptions.BuyItem, user_book);
                try {
                    SocketService.sendDataToServer(mapToSend);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            gridForBook.addRow(4, btn_buy);
            for (Node n : gridForBook.getChildren()) {
                GridPane.setHalignment(n, HPos.CENTER);
            }
            gridForBook.setAlignment(Pos.CENTER);
            content.add(gridForBook, columnCount++, rowCount);
        }
        for (Node n : content.getChildren()) {
            GridPane.setHalignment(n, HPos.CENTER);
        }
    }

    private static void setContentViewAccount(GridPane content, double amount) {
        setBlankContent();
        GridPane amountPane = new GridPane();
        Label labelAmount = new Label("Tu saldo actual es de " + (Math.floor(amount * 100) / 100) + " ???");
        labelAmount.setScaleX(1.5);
        labelAmount.setScaleY(1.5);
        amountPane.addRow(0, labelAmount);
        content.add(amountPane, 0, 0);
        for (Node n : content.getChildren()) {
            GridPane.setHalignment(n, HPos.CENTER);
        }
    }

    private static void setContentChargeAccount(GridPane content, Map<String, Client> values) {
        setBlankContent();
        GridPane increaseAmount = new GridPane();
        increaseAmount.setVgap(10);
        increaseAmount.setHgap(5);
        values.forEach((s, client) -> {
            increaseAmount.addRow(0, new Label(s));
            TextField tf_increased = new TextField();
            Tools.onlyDoubleValue(tf_increased);
            increaseAmount.addRow(1, tf_increased);
            Button btn_onSend = new Button("Enviar");
            btn_onSend.setOnAction(event -> {
                LinkedHashMap<Operations.UserOptions, Object> operation = new LinkedHashMap<>();
                Map<User, Double> newValues = new HashMap<>();
                newValues.put(user, Double.parseDouble(tf_increased.getText()));
                operation.put(Operations.UserOptions.ChargeAccountSend, newValues);
                try {
                    SocketService.sendDataToServer(operation);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            increaseAmount.addRow(2, btn_onSend);
            increaseAmount.addRow(3, new Label("Actualmente dispones de: " + (Math.floor(client.getBalance() * 100) / 100) + " ??? "));
        });
        content.add(increaseAmount, 0, 0);
        for (Node n : content.getChildren()) {
            GridPane.setHalignment(n, HPos.CENTER);
        }
    }

    private static void setContentViewHistoricalData(GridPane content, Map<LocalDateTime, Book> history) {
        setBlankContent();
        GridPane historical = new GridPane();
        historical.setHgap(20);
        Label title_book = new Label("TITULO");
        title_book.setScaleX(1.15);
        title_book.setScaleY(1.15);
        historical.addRow(0, title_book);
        Label title_autor = new Label("AUTOR");
        title_autor.setScaleX(1.15);
        title_autor.setScaleY(1.15);
        historical.addRow(0, title_autor);
        Label title_price = new Label("PRECIO");
        title_price.setScaleX(1.15);
        title_price.setScaleY(1.15);
        historical.addRow(0, title_price);
        Label title_date = new Label("FECHA");
        title_date.setScaleX(1.15);
        title_date.setScaleY(1.15);
        historical.addRow(0, title_date);
        AtomicInteger i = new AtomicInteger(1);
        history.forEach((localDateTime, book) -> {
            historical.addRow(i.get(), new Label(book.getTitle()));
            historical.addRow(i.get(), new Label(book.getAuthor()));
            historical.addRow(i.get(), new Label(book.getPrice() + " ???"));
            historical.addRow(i.get(), new Label(localDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm"))));
            i.getAndIncrement();
        });
        content.add(historical, 0, 0);
    }

    private static void setContentChangeStockBooks(GridPane content, List<Book> books) {
        setBlankContent();
        content.add(getBookTable(books, content), 0, 0);
    }

    private static TableView<Book> getBookTable(List<Book> books, GridPane content) {
        TableView<Book> result = new TableView<>();
        TableColumn<Book, String> tcTitle = new TableColumn<>("Titulo");
        TableColumn<Book, String> tcAuthor = new TableColumn<>("Autor");
        TableColumn<Book, String> tcPDate = new TableColumn<>("Fecha lanzamiento");
        TableColumn<Book, String> tcPrice = new TableColumn<>("Precio");
        TableColumn<Book, CheckBox> tcStock = new TableColumn<>("??En Stock?");
        tcTitle.setCellValueFactory(eachBook -> new SimpleStringProperty(eachBook.getValue().getTitle()));
        tcAuthor.setCellValueFactory(eachBook -> new SimpleStringProperty(eachBook.getValue().getAuthor()));
        tcPDate.setCellValueFactory(eachBook -> new SimpleStringProperty(eachBook.getValue().getReleasedDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
        tcPrice.setCellValueFactory(eachBook -> new SimpleStringProperty(eachBook.getValue().getPrice() + " ???"));
        tcStock.setCellValueFactory(eachBook -> {
            CheckBox cb = new CheckBox();
            cb.selectedProperty().setValue(eachBook.getValue().isStock());
            cb.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
                eachBook.getValue().setStock(t1);
                try {
                    LinkedHashMap<Operations.UserOptions, Book> operation = new LinkedHashMap<>();
                    operation.put(Operations.UserOptions.ChangeStockAction, eachBook.getValue());
                    SocketService.sendDataToServer(operation);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return new SimpleObjectProperty<>(cb);
        });
        result.getColumns().addAll(List.of(tcTitle,tcAuthor, tcPDate,tcPrice,tcStock));
        result.getItems().addAll(books);
        result.prefHeightProperty().bind(content.heightProperty());
        result.prefWidthProperty().bind(content.widthProperty());
        result.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return result;
    }

}
