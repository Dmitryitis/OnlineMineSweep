package View;

import Controller.Client;
import Interfaces.FieldHandler;
import Interfaces.HasParent;
import Model.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.PaneBuilder;

import javafx.util.Duration;

import java.io.IOException;



public final class MineSweeperPane implements HasParent {
    private final Parent root;
    private final Label status;
    private static Label label_timer = new Label();
    public static int timer = -1;
    public static Square botSquare = null;
    Client client = new Client();
    private final int rows;
    private final int columns;
    private final Minefield field;
    private final FieldCanvas canvas;
    private final GameField appController;
    static boolean click = true;
    static boolean gameover = false;
    static int turn = 0;
    static Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1),
                    ae -> {
                        timer -= 1;
                        label_timer.setText("Времени осталось: " + timer);
                    }
            )
    );

    public MineSweeperPane(MineSweeperPane pane) {
        this(pane.field, pane.appController, pane.client);
    }

    public MineSweeperPane(Minefield field, final GameField appController, Client client) {
        this.field = field;
        this.appController = appController;
        this.client = client;

        gameover = false;
        rows = field.getRowCount();
        columns = field.getColumnCount();

        canvas = new FieldCanvas();
        canvas.setLayoutX(14);
        canvas.setLayoutY(30);
        canvas.setWidth(340);
        canvas.setHeight(340);


        label_timer.setText("");
        timeline.stop();


        canvas.setOnMouseClicked(this::onCanvasClicked);

        canvas.setOnMousePressed(this::onCanvasPressed);

        root = PaneBuilder.create()
                .style("-fx-border-color: black;"
                        + "-fx-border-width: 1;"
                        + "-fx-border-radius: 6;"
                        + "-fx-padding: 6;"
                        + "-fx-background-color: white;")
                .prefHeight(308)
                .prefWidth(268)
                .layoutX(250)
                .layoutY(200)
                .children(
                        label_timer = LabelBuilder.create()
                                .text("")
                                .layoutX(20)
                                .layoutY(10).build(),
                        canvas,
                        status = LabelBuilder.create()
                                .text("")
                                .layoutX(20)
                                .layoutY(270).build()
                ).build();

        field.addFieldHandler(new FieldHandler() {
            @Override
            public void updateSquare(Square square) {
                drawSquare(square);
            }

            @Override
            public void updateBoard() {
                drawBoard();
            }

            @Override
            public void changeState(State state) {
                updateText(state);
            }

        });


    }

    public Parent asParent() {
        return root;
    }

    public Parent asTimer() {
        return root;
    }

    public void onNewGame() {
        field.reset();
    }

    private void onCanvasClicked(MouseEvent event) {
        if (!gameover & !field.isGameOver() & botSquare != null & turn == 1 & appController.getName_of_game().equals("serverGame") & client.isConnect()) {
            label_timer.setText("Сейчас ходит сервер");
            turn = 0;
            if (botSquare != null && botSquare.isMine()) {
                gameover = true;
                label_timer.setText("You win.Server boom");
                timeline.stop();
                timer = -1;
                canvas.clearSelection();
                botSquare.reveal();
                drawSquare(botSquare);
                drawBoard();
                updateText(State.WON);
                return;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            canvas.clearSelection();
            botSquare.reveal();
            drawSquare(botSquare);
            drawBoard();
            try {
                timer = client.getSendMessage("blank");
            } catch (IOException e) {
                e.printStackTrace();
            }
            updateTime(timer);
            return;
        }
        turn = 1;
        Square square = findSquare(event);
        int clicks = event.getClickCount();
        MouseButton button = event.getButton();

        if (button == MouseButton.MIDDLE
                || (clicks == 2 && button == MouseButton.PRIMARY)) {
            square.revealNearby();
        } else if (clicks == 1 && button == MouseButton.PRIMARY) {

            if (client == null) {
                System.out.println("ouuu clent");
            } else if (!gameover & !field.isGameOver() & appController.getName_of_game().equals("serverGame") & client.isConnect()) {
                System.out.println(appController.getName_of_game());
                int[][] fieldForServer = new int[field.getRowCount()][field.getColumnCount()];
                String answer = "";
                String res = "";
                if (square.getType() == Squares.BLANK) {
                    try {
                        if (timer == 0) {
                            label_timer.setText("Время вышло. Ты проиграл");
                            timeline.stop();
                            timer = -1;

                            updateText(State.LOST);
                            gameover = true;
                            if (field.isGameOver()){
                                System.out.println("uraaaa i am lost");
                            }
                            return;
                        }

                        canvas.clearSelection();
                        square.reveal();
                        drawSquare(square);
                        drawBoard();


                        fieldForServer = getField(field);
                        String strForServer = "";
                        for (int i = 0; i < field.getRowCount(); i++) {
                            for (int j = 0; j < field.getColumnCount(); j++) {
                                strForServer += fieldForServer[i][j] + " ";
                            }
                            strForServer += "\n";
                        }
                        System.out.println(strForServer);
                        Squares hitmine = getTable(field);
                        if (hitmine == Squares.HITMINE) {
                            timeline.stop();
                            timer = -1;
                            return;
                        }

                        answer = client.getMessageFieldFromServer(strForServer);
                        res = client.getMessageToServerSquare("square");

                        res = res.replace("\n", "");
                        String[] sq = res.split(" ");
                        System.out.println(sq[0] + " " + sq[1]);
                        botSquare = field.getSquare(Integer.parseInt(sq[0]), Integer.parseInt(sq[1]));

                        if (!gameover) {
                            canvas.setOnMouseClicked(this::onCanvasClicked);
                        }

                        timer = client.getSendMessage("blank");

                        System.out.println("onCanvas: " + timer);
                        updateTime(timer);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (appController.getName_of_game().equals("singleGame")) {
                canvas.clearSelection();
                square.reveal();
            }
        }
    }

    private void onCanvasPressed(MouseEvent event) {
        Square square = findSquare(event);
        int row = square.getRow();
        int column = square.getColumn();

        if (event.isSecondaryButtonDown()) {
            square.toggleFlag();
        } else if (event.isPrimaryButtonDown() && square.isRevealable()) {
            canvas.setSelection(row, column);
        }
    }

    public int getTimer() {
        return timer;
    }

    private void drawSquare(Square square) {
        Image image = square.getType() == Squares.EXPOSED ? Tile.getDigit(square
                .getMineCount()) : Tile.getImage(square.getType());

        canvas.drawImg(square.getRow(), square.getColumn(), image);
    }

    private void updateText(State state) {
        String text;

        switch (state) {
            case LOST:
                text = "You lost!";
                break;
            case WON:
                text = "Congratulations, you won!";
                break;
            case ServerWin:
                text = "Server win";
                break;
            default:
                text = "";
        }

        if (text.equals("You lost!") || text.equals("Congratulations, you won!")) {
            timeline.stop();
            timer = -1;
        }

        status.setText(text);

    }

    private void drawBoard() {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                drawSquare(field.getSquare(row, column));
            }
        }
    }

    private Square findSquare(MouseEvent event) {
        return field.getSquare(canvas.scaleRow(event.getY()),
                canvas.scaleColumn(event.getX()));
    }


    private void updateTime(int tim) {
        timeline.stop();
        label_timer.setText("Времени осталось: " + tim);
        timeline.setCycleCount(tim);
        timeline.play();
    }

    public Squares getTable(Minefield field) {
        for (int i = 0; i < field.getRowCount(); i++) {
            for (int j = 0; j < field.getColumnCount(); j++) {
                if (field.getSquare(i, j).getType() == Squares.HITMINE) {
                    return Squares.HITMINE;
                }
            }
        }
        return Squares.BLANK;
    }

    public int[][] getField(Minefield field) {
        int[][] array_field = new int[field.getRowCount()][field.getColumnCount()];
        for (int i = 0; i < field.getRowCount(); i++) {
            for (int j = 0; j < field.getColumnCount(); j++) {
                if (field.getSquare(i, j).getType() == Squares.BLANK) {
                    array_field[i][j] = -2;
                } else if (field.getSquare(i, j).getType() == Squares.EXPOSED) {
                    int sq =field.getSquare(i,j).getMineCount();
                    System.out.println(sq+" ");
                    array_field[i][j] = sq;
                } else if (field.getSquare(i, j).getType() == Squares.HITMINE) {
                    array_field[i][j] = -1;
                }
            }
        }
        return array_field;
    }

}
