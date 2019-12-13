package Blitz;

import Blitz.Types.Lobby;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class MenuServer implements Runnable {
    private Socket socket;
    private JTextArea console;
    private Lists lists;
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
                case 0:
                    socket.close();
                    console.append("Client Disconnected");
                    break;
                case 1:
                    //login
                    console.append(data[1] + " logged in." + "\n");
                    //log.logger.info(data[1] + " logged in.");
                    sendPacket("1,true");
                    sendLobbyList();
                    console.append("Lobby List sent.\n");
                    break;
                case 2:
                    //creating lobby
                    console.append("Creating Blitz.Types.Lobby\n");
                    Lobby newLobby = new Lobby(data[1], Integer.parseInt(data[2]), data[3]);
                    lists.lobbies.add(newLobby);
                    ArrayList<String> playerList = newLobby.getPlayers();
                    String playersInLobby = playerList.get(0);
                    for(int i = 1; i < playerList.size(); i++){
                        playersInLobby = playersInLobby + "," + playerList.get(i);
                    }
                    console.append("Lobby: " + newLobby.name+","+newLobby.gameMode+","+playersInLobby + "\n");
                    sendPacket("2,"+newLobby.name+","+newLobby.gameMode+","+playersInLobby);
                    sendLobbyList();
                    break;
            }
        }else{
            console.append(("Empty packet.\n"));
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
                lobbyList = lobbyList + lists.lobbies.get(i).name + "," ;
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