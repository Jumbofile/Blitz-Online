import Types.Lobby;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class MenuServer implements Runnable {
    private Socket socket;
    private JTextArea console;
    private Lists lists;
    MenuServer(Socket socket, Lists lists, JTextArea console) {
        this.socket = socket;
        this.console = console;
        this.lists = lists;
        lists.lobbies.add(new Lobby("Game Name", 1, "Greg"));
    }

    @Override
    public void run() {
        //Log log = new Log();
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

                if (data != null) {
                    switch(Integer.parseInt(data[0])){
                        case 1:
                            //login
                            console.append(data[1] + " logged in." + "\n");
                            //log.logger.info(data[1] + " logged in.");
                            sendPacket("1,true");
                            sendLobbyList();
                            break;
                        case 2:
                            //creating lobby
                            console.append("Creating Types.Lobby\n");
                            Lobby newLobby = new Lobby(data[1], Integer.parseInt(data[2]), data[3]);
                            lists.lobbies.add(newLobby);
                            ArrayList<String> playerList = newLobby.getPlayers();
                            String playersInLobby = playerList.get(0);
                            for(int i = 1; i < playerList.size(); i++){
                                playersInLobby = playersInLobby + "," + playerList.get(i);
                            }
                            console.append("Lobby: " + newLobby.name+","+newLobby.gameMode+","+playersInLobby);
                            sendPacket("2,"+newLobby.name+","+newLobby.gameMode+","+playersInLobby);
                            sendLobbyList();
                            break;
                    }
                }else{
                    console.append(("Empty packet.\n"));
                }
            }
        } catch (Exception e) {
            console.append("Disconnected:" + socket + "\n");
            e.printStackTrace();
        } finally {
            try { socket.close(); } catch (IOException e) {}
            console.append("Closed: " + socket + "\n");
        }
    }

    public void sendLobbyList() throws IOException{
        BufferedWriter out  = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        try {
            String lobbyList = new String();
            console.append("LOBBIES: " + lists.lobbies.size() + "\n");
            for (int i = 0; i < lists.lobbies.size(); i++) {
                lobbyList = lobbyList  + "," + lists.lobbies.get(i).name;
                console.append(lists.lobbies.get(i).name + "\n");
            }
            sendPacket("3," + lobbyList);
        }catch(IndexOutOfBoundsException e){
            //do nothing because the lobby list is empty
            console.append("Empty Lobby." + "\n");
            e.printStackTrace();
        }
    }

    public void sendPacket(String data) throws IOException {
        BufferedWriter out  = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        out.write(data + "\0");
        out.flush();
    }
}
