package Controller;

import java.io.*;
import java.net.Socket;

public class Client {
    String username = "";
    int port = 0;
    private static Socket clientSocket;
    private static BufferedReader reader;
    private static BufferedReader in;
    private static BufferedWriter out;

    public Client(String username, int port) {
        this.username = username;
        this.port = port;
    }

    public Client() {
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
        String time_result = "";
        if (clientSocket.isConnected()){
            out.write(message+"\n");
            out.flush();
        }
        time_result = in.readLine();
        return Integer.parseInt(time_result);
    }

    public void setWorkForServer() throws IOException {
        System.out.println(this.username);
        if (clientSocket == null){
            clientSocket = new Socket("localhost", 3244);
        }

        System.out.println("Please input your username: ");
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

        String word = this.username;

        out.write(word+"\n");
        out.flush();
        System.out.println("ok");
        String serverWord = in.readLine();
        System.out.println(serverWord);
    }

    public boolean isConnect() {
        return clientSocket.isConnected();
    }

}
