package Model;

import Interfaces.FieldHandler;
import Interfaces.HandlerRegistration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Minefield {
    private final int columns;
    private final int rows;
    private final int mines;
    private int unrevealed;
    private State state;
    private final Square[][] table;
    private final List<FieldHandler> handlers = new CopyOnWriteArrayList<>();
    private final List<Square> mineSet = new ArrayList<>();
    private final Random random;

    public Minefield(int rows, int columns, int mines) {
        this(rows, columns, mines, new Random());
    }

    public Minefield(int rows, int columns, int mines, Random random) {

        this.rows = rows;
        this.columns = columns;
        this.mines = mines;
        this.random = random;

        table = new Square[rows][columns];

        reset();
    }

    public HandlerRegistration addFieldHandler(final FieldHandler handler) {
        handlers.add(handler);

        updateBoard();

        return () -> handlers.remove(handler);
    }

    public int getColumnCount() {
        return columns;
    }

    public State getState() {
        return state;
    }

    public int getMines() {
        return mines;
    }

    public int getRowCount() {
        return rows;
    }

    public Square getSquare(int row, int column) {
        return table[row][column];
    }

    public boolean isGameOver() {
        return (state == State.LOST || state == State.WON);
    }

    void updateSquare(Square square) {
        for (FieldHandler handler : handlers) {
            handler.updateSquare(square);
        }
    }


    void reveal(Square square) {
        assert !isGameOver() && square.getType() == Squares.BLANK;

        if (state == State.START) {
            firstClick(square);
        }

        cascade(square);
    }

    void onGameLost() {
        for (Square[] columns : table) {
            for (Square square : columns) {
                square.onGameLost();
            }
        }

        updateBoard();
        setState(State.LOST);
    }

    List<Square> findNeighbors(Square square) {
        List<Square> neighbors = new ArrayList<>(8);
        int row = square.getRow();
        int column = square.getColumn();

        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = column - 1; c <= column + 1; c++) {
                if ((r != row || c != column) && r >= 0 && c >= 0 && r < rows && c < columns) {
                    neighbors.add(table[r][c]);
                }
            }
        }

        return neighbors;
    }

    public void reset() {
        mineSet.clear();
        unrevealed = (rows * columns) - mines;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                table[r][c] = new Square(this, r, c);
            }
        }

        updateBoard();
        setState(State.START);
    }

    void updateBoard() {
        handlers.forEach(FieldHandler::updateBoard);
    }

    private void cascade(Square start) {
        int exposed = start.visit();

        unrevealed -= exposed;

        if (unrevealed == 0) {
            mineSet.forEach(Square::onGameWon);

            setState(State.WON);
            updateBoard();

        } else if (exposed == 1) {
            updateSquare(start);
        } else {
            updateBoard();
        }
    }

    private void firstClick(Square first) {
        setState(State.PLAYING);

        List<Square> flat = new ArrayList<>(rows * columns);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (table[i][j] != first) {
                    flat.add(table[i][j]);
                }
            }
        }

        Collections.shuffle(flat, random);

        mineSet.addAll(flat.subList(0, mines));

        for (Square square : mineSet) {
            square.setMine(true);

            findNeighbors(square).forEach(Square::addNearbyMine);
        }
    }

    private void setState(State state) {
        if (this.state != state) {
            this.state = state;

            for (FieldHandler handler : handlers) {
                handler.changeState(state);
            }
        }
    }
}
