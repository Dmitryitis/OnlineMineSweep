import Controller.Client;
import Model.*;
import View.GameField;
import View.MineSweeperPane;
import javafx.scene.image.Image;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class ModelTest {
    private Minefield field;
    private Client client = new Client();
    private GameField gameField = new GameField();

    @Before
    public void setUp() throws Exception {
        field = new Minefield(10, 10, 10);
    }

    @Test(expected = NegativeArraySizeException.class)
    public void constructor() {
        field = new Minefield(-1, 0, 0);
    }

    @Test
    public void countMines() {
        assertEquals(field.getMines(), 10);

        field.getSquare(0, 0).reveal();

        int mines = 0;
        for (int i = 0; i < field.getRowCount(); i++) {
            for (int j = 0; j < field.getColumnCount(); j++) {
                if (field.getSquare(i, j).isMine()) {
                    mines++;
                }
            }
        }

        assertEquals(mines, field.getMines());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void OutOfBounds() {
        field.getSquare(-1, 0);
    }

    @Test
    public void scaleRow(){
        FieldCanvas fieldCanvas = new FieldCanvas();
        int scale = -1;
        scale = fieldCanvas.scaleRow(24);

        assertEquals(scale,1);
    }

    @Test
    public void reset(){
        Minefield minefield = Mockito.mock(Minefield.class);

        minefield.reset();
    }


    @Test
    public void GameField(){
        GameField f = Mockito.mock(GameField.class);
        f.setParametrField();
    }


}
