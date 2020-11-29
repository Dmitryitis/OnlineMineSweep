import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSomething extends Thread {
    private Socket clientSocket;

    private BufferedReader in;
    private BufferedWriter out;

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
                String word = in.readLine();
                System.out.println(word);

                if (word.equalsIgnoreCase("stop")) {
                    System.out.println(word);
                    clientSocket.close();
                    in.close();
                    out.close();
                    break;
                }

                for (ServerSomething ss: Server.serverList){
                    int time = 10 + (int) (Math.random() * 10);
                    if (word.equalsIgnoreCase("blank")) {

                        String time_string = String.valueOf(time);
                        out.write(time_string + "\n");
                    } else {
                        out.write("Привет, это Сервер! Подтверждаю, вам передано время : " + time + "\n");
                    }
                    out.flush();
                }


            }

        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
