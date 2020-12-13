package server;

import javafx.animation.AnimationTimer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;

public class Server {

    private static Socket clientSocket;

    private static ServerSocket server;

    private static BufferedReader in;

    private static BufferedWriter out;

    public static final int PORT = 3244;
    public static LinkedList<ServerSomething> serverList = new LinkedList<>();


    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("I work in server");
            while (true) {
                Socket socket = server.accept();
                try {
                    serverList.add(new ServerSomething(socket)); // добавить новое соединенние в список
                } catch (IOException e) {
                    server.close();
                }
            }
        }
    }
}
