package View;

import Interfaces.FieldHandler;
import Interfaces.HasParent;
import Model.*;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.PaneBuilder;


public final class MineSweeperPane implements HasParent {
    private final Parent root;
    private final Label status;
    private final int rows;
    private final int columns;
    private final Minefield field;
    private final FieldCanvas canvas;
    private final GameField appController;

    public MineSweeperPane(MineSweeperPane pane) {
        this(pane.field, pane.appController);
    }

    public MineSweeperPane(Minefield field, final GameField appController) {
        this.field = field;
        this.appController = appController;

        rows = field.getRowCount();
        columns = field.getColumnCount();

        canvas = new FieldCanvas();
        canvas.setLayoutX(14);
        canvas.setLayoutY(30);
        canvas.setWidth(340);
        canvas.setHeight(340);

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
                        canvas,
                        status = LabelBuilder.create()
                                .text("")
                                .layoutX(20)
                                .layoutY(270).build()
                ).build();


        field.addFieldHandler(new FieldHandler() {
            @Override public void updateSquare(Square square) {
                drawSquare(square);
            }

            @Override public void updateBoard() {
                drawBoard();
            }

            @Override public void changeState(State state) {
                updateText(state);
            }
        });
    }

    public Parent asParent() {
        return root;
    }

    private void onNewGame() {
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
            System.out.println("click");
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

    private void drawSquare(Square square) {
        Image image = square.getType() == Squares.EXPOSED ? Tile.getDigit(square
                .getMineCount()) : Tile.getImage(square.getType());

        canvas.drawImg(square.getRow(), square.getColumn(), image);
    }

    private void updateText(State state) {
        String text;

        switch(state) {
            case LOST:
                text = "You lost! Click New Game to try again.";
                break;
            case WON:
                text = "Congratulations, you won!";
                break;
            default:
                text = "";
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
}
