package server;

import server.Point;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class ServerSomething extends Thread {
    private Socket clientSocket;

    private BufferedReader in;
    private BufferedWriter out;
    private ObjectInputStream objectIn;
    private ObjectOutputStream objectOut;
    private static String field;
    public static ArrayList<Point> random_square;
    public static ArrayList<Point> known_square;
    ArrayList<ArrayList<Integer>> fieldForBot;

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
                String s = "";
                String word = in.readLine();

                if (word.equalsIgnoreCase("stop")) {
                    System.out.println(word);
                    clientSocket.close();
                    in.close();
                    out.close();
                    break;
                }

                int time = 10 + (int) (Math.random() * 10);
                if (word.equalsIgnoreCase("blank")) {

                    String time_string = String.valueOf(time);
                    out.write(time_string + "\n");
                    out.flush();
                } else if (word.startsWith("9") || word.startsWith("1")
                        || word.startsWith("2")
                        || word.startsWith("-1")
                        || word.startsWith("3")
                        || word.startsWith("4")
                        || word.startsWith("5")
                        || word.startsWith("0")) {
                    String res = word;

                    field += res + "\n";
                    out.write("Записалась " + "\n");
                    out.flush();
                } else if (word.startsWith("square")) {
                    field = field.replace("null", "");
                    random_square = new ArrayList<>();
                    known_square = new ArrayList<>();
                    fieldForBot = new ArrayList<>();
                    Point result = new Point();

                    String[] field_row = field.split("\n");
                    for (int i = 0; i < field_row.length; i++) {
                        String[] field_col = field_row[i].split(" ");
                        ArrayList<Integer> sq = new ArrayList<>();
                        for (int j = 0; j < field_col.length; j++) {
                            if (field_col[j].equals("9")) {
                                sq.add(Integer.parseInt(field_col[j]));
                                Point square = new Point();
                                square.setX(i);
                                square.setY(j);
                                square.setValue(Integer.parseInt(field_col[j]));
                                random_square.add(square);
                            } else {
                                sq.add(Integer.parseInt(field_col[j]));
                                Point square = new Point();
                                square.setX(i);
                                square.setY(j);
                                square.setValue(Integer.parseInt(field_col[j]));
                                known_square.add(square);
                            }
                        }
                        fieldForBot.add(sq);
                    }
                    botAlgorithm();
                    Random random = new Random();
                    result = random_square.get(random.nextInt(random_square.size()));

                    for (int i = 0; i < fieldForBot.size(); i++) {
                        for (int j = 0; j < fieldForBot.get(i).size(); j++) {
                            System.out.print(fieldForBot.get(i).get(j) + " ");
                        }
                        System.out.println("");
                    }

                    System.out.println(random_square);
                    out.write(result.getX() + " " + result.getY() + "\n");
                    out.flush();
                    field = "";
                } else {
                    out.write("Привет, это Сервер! Подтверждаю, вам передано время : " + time + "\n");
                    out.flush();
                }
            }

        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void botAlgorithm() {
        for (int i = 0; i < fieldForBot.size(); i++) {
            for (int j = 0; j < fieldForBot.get(i).size(); j++) {
                if (i > 0) {
                    String up = "up";
                    if (fieldForBot.get(i).get(j) == 1 & (j < 8 | j < 7)) {
                        if (j < 7) {
                            if (IfFourHorizontalOnes(i, j, 1, 1, 1, 1)) {
                                Horizontal1111ClosedSquares(i, j, up);
                            }
                        }
                        if (IfThreeHorizontalOnes(i, j, 1, 1, 1)) {
                            Horizontal111ClosedSquares(i, j, up);
                        }
                    }
                    if (j < 8) {
                        if (fieldForBot.get(i).get(j) == 1 & fieldForBot.get(i).get(j + 1) == 2) {
                            if (j < 7) {
                                if (IfFourHorizontalOnes(i, j, 1, 2, 2, 1)) {
                                    Horizontal1221ClosedSquares(i, j, up);
                                }
                            }
                            if (IfThreeHorizontalOnes(i, j, 1, 2, 1)) {
                                Horizontal121ClosedSquares(i, j, up);
                            }
                        }
                    }
                }
                if (i < 9) {
                    String down = "down";
                    if (fieldForBot.get(i).get(j) == 1 & (j < 8 | j < 7)) {
                        if (j < 7) {
                            if (IfFourHorizontalOnes(i, j, 1, 1, 1, 1)) {
                                Horizontal1111ClosedSquares(i, j, down);
                            }
                        }
                        if (IfThreeHorizontalOnes(i, j, 1, 1, 1)) {
                            Horizontal111ClosedSquares(i, j, down);
                        }
                    }
                    if (j < 8) {
                        if (fieldForBot.get(i).get(j) == 1 & fieldForBot.get(i).get(j + 1) == 2) {
                            if (j < 7) {
                                if (IfFourHorizontalOnes(i, j, 1, 2, 2, 1)) {
                                    Horizontal1221ClosedSquares(i, j, down);
                                }
                            }
                            if (IfThreeHorizontalOnes(i, j, 1, 2, 1)) {
                                Horizontal121ClosedSquares(i, j, down);
                            }
                        }
                    }
                }
                if (j > 0) {
                    String left = "left";
                    if (fieldForBot.get(i).get(j) == 1 & (i < 8 | i < 7)) {
                        if (i < 7) {
                            if (IfFourVerticalOnes(i, j, 1, 1, 1, 1)) {
                                Vertical1111ClosedSquares(i, j, left);
                            }
                        }
                        if (IfThreeVerticalOnes(i, j, 1, 1, 1)) {
                            Vertical111ClosedSquares(i, j, left);
                        }
                    }
                    if (i < 8) {
                        if (fieldForBot.get(i).get(j) == 1 & fieldForBot.get(i + 1).get(j) == 2) {
                            if (i < 7) {
                                if (IfFourVerticalOnes(i, j, 1, 2, 2, 1)) {
                                    Vertical1221ClosedSquares(i, j, left);
                                }
                            }
                            if (IfThreeVerticalOnes(i, j, 1, 2, 1)) {
                                Vertical121ClosedSquares(i, j, left);
                            }
                        }
                    }

                }
                if (j < 9) {
                    String right = "right";
                    if (fieldForBot.get(i).get(j) == 1 & (i < 8 | i < 7)) {

                        if (i < 7) {
                            if (IfFourVerticalOnes(i, j, 1, 1, 1, 1)) {
                                Vertical1111ClosedSquares(i, j, right);
                            }
                        }
                        if (IfThreeVerticalOnes(i, j, 1, 1, 1)) {
                            Vertical111ClosedSquares(i, j, right);
                        }
                    }

                    if (i < 8) {
                        if (fieldForBot.get(i).get(j) == 1 & fieldForBot.get(i + 1).get(j) == 2) {
                            if (i < 7) {
                                if (IfFourVerticalOnes(i, j, 1, 2, 2, 1)) {
                                    Vertical1221ClosedSquares(i, j, right);
                                }
                            }
                            if (IfThreeVerticalOnes(i, j, 1, 2, 1)) {
                                Vertical121ClosedSquares(i, j, right);
                            }
                        }
                    }
                }
            }
        }
    }

    private void Horizontal111ClosedSquares(int i, int j, String side) {

        int index = -1;
        if (side.equals("up")) {
            index = findIndex(i - 1, j + 1);
            System.out.println("I mine in the up 111");
        } else if (side.equals("down")) {
            index = findIndex(i + 1, j + 1);
            System.out.println("I mine in the down 111");
        }
        if (index != -1) {
            random_square.remove(index);
        }

    }

    private void Horizontal1111ClosedSquares(int i, int j, String side) {

        int index = -1;
        int indextwo = -1;
        if (side.equals("up")) {
            index = findIndex(i - 1, j);
            indextwo = findIndex(i - 1, j + 3);
        } else if (side.equals("down")) {
            index = findIndex(i + 1, j);
            indextwo = findIndex(i + 1, j + 3);
        }

        if (index != -1 & indextwo != -1) {
            random_square.remove(index);
            random_square.remove(indextwo);
        }

    }

    private void Horizontal121ClosedSquares(int i, int j, String side) {
        int index = -1;
        int indexTwo = -1;
        if (side.equals("up")) {
            index = findIndex(i - 1, j);
            indexTwo = findIndex(i - 1, j + 2);
        } else if (side.equals("down")) {
            index = findIndex(i + 1, j);
            indexTwo = findIndex(i + 1, j + 2);
        }

        try {
            if (index != -1 & indexTwo != -1) {
                random_square.remove(index);
                random_square.remove(indexTwo);
            }
        } catch (IndexOutOfBoundsException e){
            return;
        }

    }

    private void Horizontal1221ClosedSquares(int i, int j, String side) {
        int index = -1;
        int indexTwo = -1;
        if (side.equals("up")) {
            index = findIndex(i - 1, j + 1);
            indexTwo = findIndex(i - 1, j + 2);
        } else if (side.equals("down")) {
            index = findIndex(i + 1, j + 1);
            indexTwo = findIndex(i + 1, j + 2);
        }

        try {
            if (index != -1 & indexTwo != -1) {
                random_square.remove(index);
                random_square.remove(indexTwo);
            }
        } catch (IndexOutOfBoundsException e){
            return;
        }
    }

    private void Vertical121ClosedSquares(int i, int j, String side) {
        int index = -1;
        int indexTwo = -1;
        if (side.equals("left")) {
            index = findIndex(i, j - 1);
            indexTwo = findIndex(i + 2, j - 1);
            System.out.println("I mine in the left 121");
        } else if (side.equals("right")) {
            index = findIndex(i, j + 1);
            indexTwo = findIndex(i + 2, j + 1);
            System.out.println("I mine in the right 121");
        }

        if (index != -1 & indexTwo != -1) {
            random_square.remove(index);
            random_square.remove(indexTwo);
        }
    }

    private void Vertical111ClosedSquares(int i, int j, String side) {

        int index = -1;
        if (side.equals("left")) {
            index = findIndex(i + 1, j - 1);
            System.out.println("I mine in the left 111");
        } else if (side.equals("right")) {
            System.out.println("I mine in the right 111");
            index = findIndex(i + 1, j + 1);
        }

        if (index != -1) {
            random_square.remove(index);
        }
    }

    private void Vertical1111ClosedSquares(int i, int j, String side) {
        int index = -1;
        int indexTwo = -1;
        if (side.equals("left")) {
            index = findIndex(i, j - 1);
            indexTwo = findIndex(i + 3, j - 1);
        } else if (side.equals("right")) {
            index = findIndex(i, j + 1);
            indexTwo = findIndex(i + 3, j + 1);
        }
        try {
            if (index != -1 & indexTwo != -1) {
                random_square.remove(index);
                random_square.remove(indexTwo);
            }
        } catch (IndexOutOfBoundsException e){
            return;
        }
    }

    private void Vertical1221ClosedSquares(int i, int j, String side) {
        int index = -1;
        int indexTwo = -1;
        if (side.equals("left")) {
            index = findIndex(i + 1, j - 1);
            indexTwo = findIndex(i + 2, j - 1);
        } else if (side.equals("right")) {
            index = findIndex(i + 1, j + 1);
            indexTwo = findIndex(i + 2, j + 1);
        }

        try {
            if (index != -1 & indexTwo != -1) {
                random_square.remove(index);
                random_square.remove(indexTwo);
            }
        } catch (IndexOutOfBoundsException e){
            return;
        }
    }

    private boolean IfThreeHorizontalOnes(int i, int j, int firstvalue, int secondvalue, int thirdvalue) {
        return fieldForBot.get(i).get(j) == firstvalue & fieldForBot.get(i).get(j + 1) == secondvalue
                & fieldForBot.get(i).get(j + 2) == thirdvalue;
    }

    private boolean IfFourHorizontalOnes(int i, int j, int firstvalue, int secondvalue, int thirdvalue,
                                         int fourvalue) {
        return fieldForBot.get(i).get(j) == firstvalue & fieldForBot.get(i).get(j + 1) == secondvalue
                & fieldForBot.get(i).get(j + 2) == thirdvalue & fieldForBot.get(i).get(j + 3) == fourvalue;
    }

    private boolean IfThreeVerticalOnes(int i, int j, int firstvalue, int secondvalue, int thirdvalue) {
        return fieldForBot.get(i).get(j) == firstvalue & fieldForBot.get(i + 1).get(j) == secondvalue
                & fieldForBot.get(i + 2).get(j) == thirdvalue;
    }

    private boolean IfFourVerticalOnes(int i, int j, int firstvalue, int secondvalue, int thirdvalue,
                                       int fourvalue) {
        return fieldForBot.get(i).get(j) == firstvalue & fieldForBot.get(i + 1).get(j) == secondvalue
                & fieldForBot.get(i + 2).get(j) == thirdvalue & fieldForBot.get(i + 3).get(j) == fourvalue;
    }

    private int findIndex(int x, int y) {
        for (int k = 0; k < random_square.size(); k++) {
            if (random_square.get(k).getX() == x & random_square.get(k).getY() == y) {
                return k;
            }
        }
        return -1;
    }

}
