package Model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class FieldCanvas extends Canvas {
    private final SelectionModel select = new SelectionModel();
    public static final int SQUAREW = 24;
    public static final int SQUAREH = 24;

    public void setSelection(int row, int column) {
        drawImg(row, column, Tile.EXPOSED);
        select.select(row, column);
    }

    public void clearSelection() {
        drawImg(select.getRow(), select.getColumn(), Tile.BLANK);
        select.clear();
    }

    public int scaleRow(double y) {
        return (int) (y / SQUAREH);
    }

    public int scaleColumn(double x) {
        return (int) (x / SQUAREW);
    }


    public void drawImg(int row, int column, Image image) {
        GraphicsContext gc = getGraphicsContext2D();
        gc.drawImage(image, column * SQUAREW, row * SQUAREH);
    }

}
