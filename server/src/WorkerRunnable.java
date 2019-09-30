import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class WorkerRunnable implements Runnable{

    //protected
    protected Socket clientSocket = null;
    protected String serverText   = null;

    //private
    private ArrayList<Player> clients = new ArrayList<Player>();
    private boolean running;
    private boolean resyncNeeded;

    public WorkerRunnable(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText   = serverText;
    }

    public void run() {
        try {
            InputStream input  = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            while(running){

            }
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }
}