package Controller;

import java.io.*;
import java.net.Socket;

public class ClientSomething {
    private int port;
    private String addr;
    private BufferedReader in;
    private BufferedWriter out;
    private static Socket clientSocket;
    private String username;

    public ClientSomething() {
    }

    public ClientSomething(String addr, int port, String username) {
        this.addr = addr;
        this.port = port;
        this.username = username;


    }

    public void setWorkForServer() throws IOException {
        System.out.println(this.username);
        if (clientSocket == null) {
            clientSocket = new Socket("localhost", 3244);
        }

        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

        String word = this.username;

        out.write(word + "\n");
        out.flush();

        String serverWord = in.readLine();
        System.out.println(serverWord);
    }

    public boolean isConnected() {
        return clientSocket.isConnected();
    }

    public int getSendMessage(String message) throws IOException {
        String time_result = "";
        if (clientSocket.isConnected()) {
            out.write(message + "\n");
            out.flush();
        }
        time_result = in.readLine();
        return Integer.parseInt(time_result);
    }

}
