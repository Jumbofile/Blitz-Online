package Types;

import java.util.ArrayList;

public class Lobby {
    public String name;
    public int gameMode;
    private String owner;
    private ArrayList<String> players;

    public Lobby(String name, int gameMode, String owner){
        this.name = name;
        this.gameMode = gameMode;
        this.owner = owner;

        //change this to a list of type Player
        players = new ArrayList<String>();
        addPlayer(owner);
    }

    public ArrayList<String> getPlayers(){
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
    public void addPlayer(String name){
        players.add(name);
    }
}
