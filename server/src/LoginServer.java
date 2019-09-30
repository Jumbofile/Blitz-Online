import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Scanner;

public class LoginServer implements Runnable {
    private Socket socket;
    private JTextArea console;

    LoginServer(Socket socket, JTextArea console) {
        this.socket = socket;
        this.console = console;
    }

    @Override
    public void run() {
        Log log = new Log();
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
                            log.logger.info(data[1] + " logged in.");
                            out.write("1,true");
                            out.flush();
                            break;
                    }
                }
            }
        } catch (Exception e) {
            console.append("Disconnected:" + socket + "\n");
            log.logger.info("Disconnected:" + socket);
        } finally {
            try { socket.close(); } catch (IOException e) {}
            console.append("Closed: " + socket + "\n");
        }
    }
}
