package Blitz;

import Blitz.Types.Account;
import Blitz.Types.Lobby;
import Blitz.Types.Player;

import java.util.ArrayList;

public class Lists {
    public ArrayList<Account> onlinePlayers;
    public ArrayList<Lobby> lobbies;
    public ArrayList<GameServer> gameServers;

    public Lists(){
        onlinePlayers = new ArrayList<Account>();
        lobbies = new ArrayList<Lobby>();
        gameServers = new ArrayList<GameServer>();

    }
}
