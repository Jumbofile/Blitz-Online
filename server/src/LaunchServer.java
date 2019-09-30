import javax.swing.*;
import java.awt.*;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LaunchServer{
    public static JTextArea consoleWin = new JTextArea();

    public static void main(String[] args) throws Exception{
        //setup swing
        JFrame frame = new JFrame("Tank Server");
        JPanel panel = new JPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 420);
        frame.setResizable(false);
        consoleWin.setEditable(false);
        JScrollPane scroll = new JScrollPane (consoleWin);
        consoleWin.setFont(new Font("Consolas", Font.PLAIN, 14));  // make a new font object);
        consoleWin.setForeground(Color.lightGray);
        consoleWin.setBackground(Color.BLACK);
        scroll.setBounds(-1,0,800,420);
        consoleWin.setLineWrap(true);
        frame.getContentPane().add(scroll);
        frame.getContentPane().add(panel);
        frame.setVisible(true);

        try (ServerSocket listener = new ServerSocket(6666)) {
            consoleWin.append("  _______          _       _____                          " + "\n");
            consoleWin.append(" |__   __|        | |     / ____|                         " + "\n");
            consoleWin.append("    | | __ _ _ __ | | __ | (___   ___ _ ____   _____ _ __ " + "\n");
            consoleWin.append("    | |/ _` | '_ \\| |/ /  \\___ \\ / _ \\ '__\\ \\ / / _ \\ '__|" + "\n");
            consoleWin.append("    | | (_| | | | |   <   ____) |  __/ |   \\ V /  __/ |   " + "\n");
            consoleWin.append("    |_|\\__,_|_| |_|_|\\_\\ |_____/ \\___|_|    \\_/ \\___|_|   " + "\n");
            consoleWin.append("Started..." + "\n");
            ExecutorService pool = Executors.newFixedThreadPool(20);
            while (true) {
                pool.execute(new LoginServer(listener.accept(), consoleWin));
            }
        }
    }

    public static class Logger {
    }
}
