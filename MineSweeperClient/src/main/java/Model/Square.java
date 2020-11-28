package Model;

import java.util.List;

public class Square {
    private final int column;
    private final int row;
    private final Minefield minefield;
    private boolean mine;
    private Squares type = Squares.BLANK;
    private int nearbyMines;

    Square(Minefield minefield, int row, int column) {
        this.minefield = minefield;
        this.row = row;
        this.column = column;
    }

    public Squares getType() {
        return type;
    }


    public boolean isRevealable() {
        return !minefield.isGameOver() && type == Squares.BLANK;
    }


    public int getRow() {
        return row;
    }


    public int getColumn() {
        return column;
    }


    public int getMineCount() {
        return nearbyMines;
    }


    public void toggleFlag() {
        if (minefield.isGameOver()) {
            return;
        }

        if (type == Squares.FLAG) {
            type = Squares.BLANK;
        } else if (type == Squares.BLANK) {
            type = Squares.FLAG;
        } else {
            return;
        }

        minefield.updateSquare(this);
    }


    public void reveal() {
        if (type != Squares.BLANK || minefield.isGameOver()) {
            return;
        }

        if (mine) {
            type = Squares.HITMINE;
            mine = false;
            minefield.onGameLost();
        } else {
            minefield.reveal(this);
        }
    }


    public void revealNearby() {
        if (minefield.isGameOver() || type != Squares.EXPOSED) {
            return;
        }

        List<Square> neighbors = minefield.findNeighbors(this);

        int nearbyFlags = neighbors.stream()
                .filter(square -> square.type == Squares.FLAG)
                .mapToInt(e -> 1)
                .sum();

        if (nearbyFlags == nearbyMines) {
            neighbors.forEach(Square::reveal);
        }
    }

    void addNearbyMine() {
        nearbyMines++;
    }

    public boolean isMine() {
        return mine;
    }

    void setMine(boolean isMine) {
        mine = isMine;
    }

    void onGameLost() {
        if (mine) {
            type = Squares.MINE;
        } else if (type == Squares.FLAG) {
            type = Squares.WRONGMINE;
        }
    }

    void onGameWon() {
        if (mine) {
            type = Squares.FLAG;
        }
    }

    int visit() {
        int exposed = 1;
        type = Squares.EXPOSED;

        if (nearbyMines == 0) {
            for (Square square : minefield.findNeighbors(this)) {
                if (square.type != Squares.EXPOSED) {
                    exposed += square.visit();
                }
            }
        }

        return exposed;
    }
}
