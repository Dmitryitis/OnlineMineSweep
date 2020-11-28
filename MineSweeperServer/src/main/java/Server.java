import javafx.animation.AnimationTimer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Timer;

public class Server {

    private static Socket clientSocket;

    private static ServerSocket server;

    private static BufferedReader in;

    private static BufferedWriter out;


    public static void main(String[] args) {
        try {

            server = new ServerSocket(3244);

            System.out.println("Сервер запущен!");

            clientSocket = server.accept();

            System.out.println("Клиент подключен");

            while (clientSocket.isConnected()) {

                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                String word = in.readLine();
                System.out.println(word);

                if (word.equalsIgnoreCase("stop")) {
                    System.out.println(word);
                    clientSocket.close();
                    in.close();
                    out.close();
                    server.close();
                    break;
                }



                if (word.equalsIgnoreCase("blank")) {
                    int time = 10 + (int) (Math.random() * 10);
                    String time_string = String.valueOf(time);
                    out.write(time_string+"\n");
                }
                else {
                    out.write("Привет, это Сервер! Подтверждаю, вам передано время : " + word + "\n");
                }
                out.flush();

            }

        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void setTimer(int current_time) {
        current_time -= 1;
    }
}
