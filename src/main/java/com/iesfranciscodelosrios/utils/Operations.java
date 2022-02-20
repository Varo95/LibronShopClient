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
        ViewPurchaseHistory(5),//Menu->historial compra
        BuyItem(6),//OK
        //Ociones de un Operador
        AddBook(7), //menu añadir
        AddBookAction(8),//enviar libro
        ChangeStock(9), //menu libros para cambiar stock
        ChangeStockAction(10),//enviar nuevos stocks
        DeleteBook(10),//menu para eliminar libros
        DeleteBookAction(11),//enviar borrar libro
        SeeClients(12),//menu para ver clientes
        SeeClientRemove(13),//opcion eliminar clientes
        SeeClientsAdd(14),//opcion añadir cliente
        SeeClientsModify(14);//opcion cambiar cliente
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
        SendSubmenu(3),
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
