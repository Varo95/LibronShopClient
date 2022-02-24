package com.iesfranciscodelosrios.utils;

import java.io.Serial;
import java.io.Serializable;

public class Operations implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public enum UserOptions implements Serializable{
        Register(0),//ok
        Login(1), //Menu si ok
        //opciones de cliente normal
        ViewOnStockBooks(2),//Menu
        ViewAccount(3),//Menu
        ChargeAccount(4),//Menu
        ChargeAccountSend(5),//enviar nueva cantidad
        ViewPurchaseHistory(6),//Menu->historial compra
        BuyItem(7),//OK
        //Ociones de un Operador
        AddBook(8), //menu añadir
        AddBookAction(9),//enviar libro
        ChangeStock(10), //menu libros para cambiar stock
        ChangeStockAction(11);//enviar nuevos stocks
        @Serial
        private static final long serialVersionUID = 1L;
        private final int i;

        UserOptions(int i) {
            this.i = i;
        }

        public int getI() {
            return this.i;
        }

    }

    public enum ServerActions implements Serializable{
        SendMenu(0),
        SendPurchaseHistory(1),
        SendProfile(2),
        SendAddBook(3),
        SendBooksToPurchase(4),
        NewBalance(5),
        SendMenuBooks(6),
        OperationOk(200),
        OperationOkButNoContent(204),
        UserAlreadyExist(403),
        NotEnoughBalance(402),
        WrongPassword(405),
        IncorrectUserType(406),
        EmailDoesntExistOnDB(407);
        @Serial
        private static final long serialVersionUID = 1L;
        private final int i;

        ServerActions(int i) {
            this.i = i;
        }

        public int getI() {
            return this.i;
        }

    }
}
