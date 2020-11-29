package Controller;

import javafx.application.Application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

public class ClientTwo {
    String username = "";
    int port = 0;
    private static Socket clientSocket;
    private static BufferedReader reader;
    private static BufferedReader in;
    private static BufferedWriter out;
    static ClientSomething cs = new ClientSomething();

    public ClientTwo(String username, int port) {
        this.username = username;
        this.port = port;
        cs = new ClientSomething("localhost",3244,this.username);
    }

    public ClientTwo() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setStopMessage(){
    }

    public int getSendMessage(String message) throws IOException {
        return cs.getSendMessage(message);
    }

    public void setWorkForServer() throws IOException {
        cs.setWorkForServer();
    }

    public boolean isConnect() {
        return cs.isConnected();
    }

    public static void main(String[] args) {
        Application.launch(Main.class,args);
    }
}
