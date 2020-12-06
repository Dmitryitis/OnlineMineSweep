import com.sun.corba.se.pept.encoding.InputObject;

import javax.crypto.spec.PSource;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ServerSomething extends Thread {
    private Socket clientSocket;

    private BufferedReader in;
    private BufferedWriter out;
    private ObjectInputStream objectIn;
    private ObjectOutputStream objectOut;
    private static String field;

    public ServerSomething(Socket socket) throws IOException {
        this.clientSocket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
    }

    @Override
    public void run() {
        try {

            System.out.println("Сервер запущен!");

            System.out.println("Клиент подключен");

            while (true) {

                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                System.out.println("I work in huuu");
                String s = "";
                String word = in.readLine();

                if (word.equalsIgnoreCase("stop")) {
                    System.out.println(word);
                    clientSocket.close();
                    in.close();
                    out.close();
                    break;
                }

                for (ServerSomething ss : Server.serverList) {
                    int time = 10 + (int) (Math.random() * 10);
                    if (word.equalsIgnoreCase("blank")) {

                        String time_string = String.valueOf(time);
                        out.write(time_string + "\n");
                        out.flush();
                    } else if (word.startsWith("0") || word.startsWith("1") || word.startsWith("-1")) {
                        String res = word;

                        field += res + "\n";
                        out.write("Записалась " + "\n");
                        out.flush();
                    } else if (word.startsWith("square")) {
                        field = field.replace("null", "");
                        ArrayList<ArrayList<Integer>> random_square = new ArrayList<>();
                        ArrayList<Integer> result = new ArrayList<>();
                        System.out.println(field);
                        String[] field_row = field.split("\n");
                        for (int i = 0; i < field_row.length; i++) {
                            String[] field_col = field_row[i].split(" ");
                            for (int j = 0; j < field_col.length; j++) {
                                if (field_col[j].equals("0")) {
                                    ArrayList<Integer> square = new ArrayList<>();
                                    square.add(i);
                                    square.add(j);
                                    random_square.add(square);
                                }
                            }
                        }
                        Random random = new Random();
                        result = random_square.get(random.nextInt(random_square.size()));
                        System.out.println(random_square);
                        out.write(result.get(0) +" "+ result.get(1) + "\n");
                        out.flush();
                        field = "";
                    } else {
                        out.write("Привет, это Сервер! Подтверждаю, вам передано время : " + time + "\n");
                        out.flush();
                    }
                }


            }

        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
