import Controller.Client;
import Controller.ClientSomething;
import Model.*;
import View.GameField;
import View.MineSweeperPane;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mockito;
import server.Server;


import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class ModelTest {
    private static Thread thread;
    private static Thread threadServer;
    private Minefield field;
    private GameField gameField = new GameField();
    private static final Client client  = new Client("alex",3244);

    @BeforeAll
    static void mainF() {
        threadServer = new Thread(() -> {
            try {
                Server.main(new String[]{""});
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        threadServer.start();

        thread = new Thread(() -> {
            client.main(new String[]{""});
        });
        thread.start();


    }

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
        Assert.assertEquals(field.getMines(), 10);

        field.getSquare(0, 0).reveal();

        int mines = 0;
        for (int i = 0; i < field.getRowCount(); i++) {
            for (int j = 0; j < field.getColumnCount(); j++) {
                if (field.getSquare(i, j).isMine()) {
                    mines++;
                }
            }
        }

        Assert.assertEquals(mines, field.getMines());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void OutOfBounds() {
        field.getSquare(-1, 0);
    }

    @Test
    public void scaleRow() {
        FieldCanvas fieldCanvas = new FieldCanvas();
        int scale = -1;
        scale = fieldCanvas.scaleRow(24);

        assertEquals(scale, 1);
    }

    @Test
    public void reset() {
        Minefield minefield = Mockito.mock(Minefield.class);

        minefield.reset();
    }


    @Test
    public void GameField() {
        GameField f = Mockito.mock(GameField.class);
        f.setParametrField();
    }

    @Test(expected = ExceptionInInitializerError.class)
    public void getTable() {
        Client client = Mockito.mock(Client.class);
        GameField gameField = Mockito.mock(GameField.class);
        MineSweeperPane pane = new MineSweeperPane(field, gameField, client);
        Squares res = pane.getTable(field);
    }

    @Test
    public void getSendMessage() throws IOException {
        Client c = Mockito.mock(Client.class);
        when(c.getSendMessage("blank")).thenReturn(10);
        int res = c.getSendMessage("blank");
        assertEquals(10, res);
    }

//    @Test
//    public void isConnected() throws IOException {
//        client.setWorkForServer();
//    }


    @AfterAll
    static void destroy() {
        thread.interrupt();
        threadServer.interrupt();
    }


}
