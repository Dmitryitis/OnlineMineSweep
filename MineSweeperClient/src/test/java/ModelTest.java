import Model.Minefield;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class ModelTest {
    private Minefield field;

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
}
