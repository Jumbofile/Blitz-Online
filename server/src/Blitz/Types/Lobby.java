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

    public void setOwner(Account player){
        owner = player;
    }

    public boolean isOwner(Account player){
        boolean isThePlayerOwner = false;
        if(owner == player){
            isThePlayerOwner = true;
        }else{
            isThePlayerOwner = false;
        }

        return isThePlayerOwner;
    }
    public ArrayList<Account> getPlayers(){
        return players;
    }

    //removes a player if they exist
    public boolean removePlayer(Account player){
        if(players.contains(player)){
            players.remove(player);
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
