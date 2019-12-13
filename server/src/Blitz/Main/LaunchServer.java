package Blitz.Main;

import Blitz.Lists;
import Blitz.MenuServer;

import javax.swing.*;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LaunchServer{


    public static void main(String[] args) throws Exception{
        Terminal terminal = new Terminal();
        JTextArea consoleWin = terminal.getConsoleWin();
        Lists lists = new Lists();
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
                pool.execute(new MenuServer(listener.accept(), lists, consoleWin));
            }
        }
    }

    public static class Logger {
    }
}
