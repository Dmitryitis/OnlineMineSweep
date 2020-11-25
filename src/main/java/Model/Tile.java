package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import javax.swing.text.html.ImageView;

public class Tile {
    public static final Image BLANK = new Image("file:src/main/webapp/images/blank.png");
    public static final Image FLAG = new Image("file:src/main/webapp/images/flagRed.png");
    public static final Image MINE = new Image("file:src/main/webapp/images/mine.png");
    public static final Image HITMINE = new Image("file:src/main/webapp/images/hitmine.png");
    public static final Image WRONGMINE = new Image("file:src/main/webapp/images/wrongmine.png");
    public static final Image EXPOSED = new Image("file:src/main/webapp/images/exposed.png");
    public static final Image[] digits = new Image[9];

    static {
        digits[0] = EXPOSED;
        for (int i = 1; i < digits.length; i++) {
            digits[i] = new Image("file:src/main/webapp/images/" + String.format("number%d.png", i));
        }
    }

    public static Image getImage(Squares square) {
        switch (square) {
            case BLANK:
                return Tile.BLANK;
            case FLAG:
                return Tile.FLAG;
            case MINE:
                return Tile.MINE;
            case EXPOSED:
                return Tile.EXPOSED;
            case HITMINE:
                return Tile.HITMINE;
            case WRONGMINE:
                return Tile.WRONGMINE;
            default:
                throw new AssertionError("Unknown square type: " + square);
        }
    }

    public static Image getDigit(int index) {
        for (int i = 0; i < digits.length; i++) {
            if (i == index) {
                return digits[i];
            }
        }
        return digits[index];
    }



}
