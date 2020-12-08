package Controller;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class ClientSomething {
    private int port;
    private String addr;
    private static BufferedReader in;
    private static BufferedWriter out;
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

    public String getMessageFieldFromServer(String field) throws IOException {
        String res = "";
        String s = "";
        String[] field_mass = field.split("\n");
        for (String fieldMass : field_mass) {
            if (clientSocket.isConnected()) {
                out.write(fieldMass + "\n");
                out.flush();
                s = in.readLine();
                res += s + "\n";
            }
        }
        return res;
    }

    public String getMessageToServerSquare(String message) throws IOException {
        int[] res = new int[2];
        String r = "";
        if (message.equals("square") && clientSocket.isConnected()) {
            out.write(message + "\n");
            out.flush();
            String p = in.readLine();
            r += p;
        }
        return r;
    }

}
