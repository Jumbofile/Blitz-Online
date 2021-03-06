package Blitz;

import Blitz.Types.Account;
import Blitz.Types.Lobby;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class MenuServer implements Runnable {
    private Socket socket;
    private JTextArea console;
    private Lists lists;
    private Account account;
    public MenuServer(Socket socket, Lists lists, JTextArea console) {
        this.socket = socket;
        this.console = console;
        this.lists = lists;
        //lists.lobbies.add(new Lobby("Game Name", 1, "Greg"));
    }

    @Override
    public void run() {
        //Blitz.Database.Log log = new Blitz.Database.Log();
        console.append("Connected: " + socket + "\n");
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String[] data = null;

            while (socket.isConnected()) {
                String incoming = in.readLine();
                console.append(incoming + "\n");
                try {
                    data = incoming.split(",");
                }catch(NullPointerException e){
                    //eh
                }
                //recieve packets
                receivePackets(data);
            }
        } catch (Exception e) {
            console.append("Disconnected:" + socket + "\n");
            //e.printStackTrace();
        } finally {
            try { socket.close(); } catch (IOException e) {}
            console.append("Closed: " + socket + "\n");
        }
    }

    /***
     * Receives and parses packets
     * @param data
     * @throws IOException
     */
    public void receivePackets(String[] data) throws IOException{
        if (data != null) {
            switch(Integer.parseInt(data[0])){
                case 0: //disconnect packet
                    System.out.println("CLIENT DISCONNECT REQUESTED");
                    socket.close();
                    console.append("Client Disconnected");
                    lists.onlinePlayers.remove(account);
                    break;
                case 1: //Login packet
                    console.append(data[1] + " logged in." + "\n");
                    //log.logger.info(data[1] + " logged in.");
                    account = new Account(this.socket);
                    account.setAccountName(data[1]);
                    lists.onlinePlayers.add(account);
                    sendPacket("1,true");
                    sendLobbyList();
                    console.append("Lobby List sent.\n");
                    break;
                case 2: //creating lobby packet
                    console.append("Creating Blitz.Types.Lobby\n");

                    //make a new lobby and add it to the list of lobbies
                    Lobby newLobby = new Lobby(data[1], Integer.parseInt(data[2]), account);
                    lists.lobbies.add(newLobby);

                    //add the players to the lobby type
                    ArrayList<Account> playerList = newLobby.getPlayers();
                    String playersInLobby = playerList.get(0).getAccountName();
                    for(int i = 1; i < playerList.size(); i++){
                        playersInLobby = playersInLobby + "~" + playerList.get(i).getAccountName();
                    }

                    //set the owner
                    newLobby.setOwner(account);

                    //get the lobby id in the list
                    int lobbyId = lists.lobbies.indexOf(newLobby);
                    console.append("Playerse: " + playersInLobby);
                    console.append("Lobby: " + newLobby.name+","+newLobby.gameMode+","+playersInLobby + "\n");
                    sendPacket("2,"+lobbyId+","+newLobby.name+","+newLobby.gameMode+","+playersInLobby);
                    sendLobbyList();
                    break;
                case 3: //send player to the lobby
                    //int lobby = Integer.parseInt(data[1].split("/")[1]);
                    String[] lobbyString = (data[1].split("/"));
                    int lobbyNum = Integer.parseInt(lobbyString[1]);
                    System.out.println("Lobby ID: " + lobbyNum);

                    //get the lobby
                    Lobby lobby = lists.lobbies.get(lobbyNum);

                    //add player to the lobby
                    lobby.addPlayer(account);
                    //get the player list and make a string
                    playersInLobby = lobby.getPlayers().get(0).getAccountName();
                    for(int i = 1; i < lobby.getPlayers().size(); i++){
                        playersInLobby = playersInLobby + "~" + lobby.getPlayers().get(i).getAccountName();
                    }
                    console.append("Players: " + playersInLobby);
                    sendPacket("2,"+lobbyNum+","+lobby.name+","+lobby.gameMode+","+playersInLobby);
                    //get lobby by id
                    //add player to lobby and send command to move to lobby
                    break;
                case 4: //update the lobby

                    lobbyNum = Integer.parseInt(data[1]);

                    //get the lobby
                    lobby = lists.lobbies.get(lobbyNum);

                    //get the player list and make a string
                    playersInLobby = lobby.getPlayers().get(0).getAccountName();
                    for(int i = 1; i < lobby.getPlayers().size(); i++){
                        playersInLobby = playersInLobby + "~" + lobby.getPlayers().get(i).getAccountName();
                    }
                    console.append("Players: " + playersInLobby);
                    sendPacket("4,"+lobbyNum+","+playersInLobby);
                    break;
                case 5: //remove player from the lobby
                    //define the lobby
                    lobbyNum = Integer.parseInt(data[1]);
                    lobby = lists.lobbies.get(lobbyNum);

                    //remove the player
                    lobby.removePlayer(account);

                    if(lobby.isOwner(account)){
                        lists.lobbies.remove(lobby);
                    }

                    break;
                case 6://update game select
                    sendLobbyList();
                    break;
            }
        }else{
            //console.append(("Empty packet.\n"));
        }
    }

    /***
     * Send the packets in the correct format
     * @param data
     * @throws IOException
     */
    public void sendPacket(String data) throws IOException {

        BufferedWriter out  = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        out.write(data + "\0");
        out.flush();
    }

    /***
     * HELPER FUNCTION BELOW
     */
    public void sendLobbyList() throws IOException{

        try {
            Thread.sleep(50);
            String lobbyList = new String();
            console.append("LOBBIES: " + lists.lobbies.size() + "\n");
            for (int i = 0; i < lists.lobbies.size(); i++) {
                lobbyList = lobbyList + lists.lobbies.get(i).name +"/"+ i + "," ;
                //console.append(lists.lobbies.get(i).name + "\n");
            }
            //console.append("3," + lobbyList + "\n");
            if ((lobbyList != null) && (lobbyList.length() > 0)) {
                lobbyList = lobbyList.substring(0, lobbyList.length() - 1);
            }
            sendPacket("3," + lobbyList);
        }catch(IndexOutOfBoundsException | InterruptedException e){
            //do nothing because the lobby list is empty
            console.append("Empty Lobby." + "\n");
            e.printStackTrace();
        }
    }
}
