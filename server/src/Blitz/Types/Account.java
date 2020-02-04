package Blitz.Types;

import java.net.Socket;

public class Account {
    //this will also be populated with sql
    private Socket socket;
    private String username;
    public Account(Socket socket){
        this.socket = socket;
    }
    public void setAccountName(String username){this.username = username;}
    public String getAccountName(){
        return username;
    }
}
