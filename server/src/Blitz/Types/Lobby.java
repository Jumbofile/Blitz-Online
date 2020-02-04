package Blitz.Types;

import Blitz.GameServer;
import Blitz.Lists;

import java.util.ArrayList;

public class Lobby {
    public String name;
    public int gameMode;
    private Account owner;
    private ArrayList<Account> players;

    public Lobby(String name, int gameMode, Account owner){
        this.name = name;
        this.gameMode = gameMode;
        this.owner = owner;

        //change this to a list of type Blitz.Types.Player
        players = new ArrayList<Account>();
        addPlayer(owner);

    }

    public ArrayList<Account> getPlayers(){
        return players;
    }

    //removes a player if they exist
    public boolean removePlayer(String name){
        if(players.contains(name)){
            players.remove(name);
            return true;
        }else{
            return false;
        }
    }

    //adds player to player list
    public void addPlayer(Account player){
        players.add(player);
    }
}
