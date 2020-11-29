package View;

import Controller.Client;
import Controller.ClientTwo;
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
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public final class MineSweeperPane implements HasParent {
    private final Parent root;
    private final Label status;
    private static Label label_timer = new Label();
    public static int timer = -1;
    Client client = new Client();
    ClientTwo clientTwo = new ClientTwo();
    private final int rows;
    private final int columns;
    private final Minefield field;
    private final FieldCanvas canvas;
    private final GameField appController;
    static boolean click = true;
    static Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1),
                    ae -> {
                        timer -= 1;
                        label_timer.setText("Времени осталось: " + timer);
                    }
            )
    );

    public MineSweeperPane(MineSweeperPane pane) {
        this(pane.field, pane.appController, pane.client, pane.clientTwo);
    }

    public MineSweeperPane(Minefield field, final GameField appController, Client client, ClientTwo clientTwo) {
        this.field = field;
        this.appController = appController;
        this.client = client;
        this.clientTwo = clientTwo;


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
        Square square = findSquare(event);
        int clicks = event.getClickCount();
        MouseButton button = event.getButton();

        if (button == MouseButton.MIDDLE
                || (clicks == 2 && button == MouseButton.PRIMARY)) {
            square.revealNearby();
        } else if (clicks == 1 && button == MouseButton.PRIMARY) {

            if (client == null) {
                System.out.println("ouuu clent");
            } else if (appController.getName_of_game().equals("serverGame") & client.isConnect()) {
                System.out.println(appController.getName_of_game());
                if (square.getType() == Squares.BLANK) {
                    try {
                        if (timer == 0) {
                            label_timer.setText("Время вышло. Ты проиграл");
                            timeline.stop();
                            timer = -1;
                            onNewGame();
                            return;
                        }
                        timer = client.getSendMessage("blank");

                        System.out.println("onCanvas: " + timer);
                        updateTime(timer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            canvas.clearSelection();
            square.reveal();
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

}
