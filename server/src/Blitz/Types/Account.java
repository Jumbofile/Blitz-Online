package Blitz.Types;

import java.net.Socket;

public class Account {
    //this will also be populated with sql
    private Socket socket;
    public Account(Socket socket){
        this.socket = socket;
    }
}
