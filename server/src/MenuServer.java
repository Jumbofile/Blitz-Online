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
    }

    @Override
    public void run() {
        //Log log = new Log();
        console.append("Connected: " + socket + "\n");
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out  = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String[] data;
            while (socket.isConnected()) {
                data = in.readLine().split(",");

                if (data != null) {
                    switch(Integer.parseInt(data[0])){
                        case 1:
                            //login
                            console.append(data[1] + " logged in." + "\n");
                            //log.logger.info(data[1] + " logged in.");
                            out.write("1,true");
                            out.flush();
                            sendLobbyList(out);
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
                            out.write("2,"+newLobby.name+","+newLobby.gameMode+","+playersInLobby);
                            out.flush();
                            sendLobbyList(out);
                            break;
                    }
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

    public void sendLobbyList(BufferedWriter out) throws IOException{
        try {
            String lobbyList = lists.lobbies.get(0).name;
            for (int i = 1; i < lists.lobbies.size(); i++) {
                lobbyList = lobbyList  + "," + lists.lobbies.get(i).name;
                console.append(lists.lobbies.get(i).name + "\n");
            }
            out.write("3," + lobbyList);
            out.flush();
        }catch(IndexOutOfBoundsException e){
            //do nothing because the lobby list is empty
            e.printStackTrace();
        }
    }
}
