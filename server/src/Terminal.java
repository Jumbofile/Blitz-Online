import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class Terminal {
    public static JTextArea consoleWin = new JTextArea();
    public Terminal(){
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
        DefaultCaret caret = (DefaultCaret)consoleWin.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        frame.getContentPane().add(scroll);
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    public JTextArea getConsoleWin(){
        return consoleWin;
    }
}
