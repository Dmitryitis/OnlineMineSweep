package View;

import Controller.Client;
import Controller.ClientTwo;
import Model.Minefield;

import javafx.geometry.Insets;
import javafx.geometry.Pos;


import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class GameField extends Pane {
    public static Pane canvas = new Pane();
    public static int timer;
    private static boolean spawnMode;
    public static BorderPane bp;
    public static StackPane sp = new StackPane();
    public static Client client;
    public static ClientTwo clientTwo;
    public static MineSweeperPane minesweeper;
    private String name_of_game;

    public GameField() {
        spawnMode = true;
        this.name_of_game = "singleGame";
    }

    public GameField(Client client,String name_of_game){
        this.client = client;
        this.name_of_game = name_of_game;
    }

    public static Client getClient() {
        return client;
    }

    public static void setClient(Client client) {
        GameField.client = client;
    }

    public void setParametrField() {
        if (!spawnMode) {
            return;
        }
        Text name = new Text("OnlineMineSweeper");
        name.setFont(Font.font("file:src/main/java/webapp/fonts/OptimusPrinceps.ttf", FontWeight.BOLD, 30));
        name.setFill(Color.WHITE);

        Rectangle rect = new Rectangle(150, 40, Color.LIGHTBLUE);
        Text text_back = new Text("Выйти");
        text_back.setFill(Color.WHITE);
        text_back.setFont(Font.font("file:src/main/java/webapp/fonts/OptimusPrinceps.ttf", FontWeight.BOLD, 18));
        text_back.setVisible(true);
        sp.setLayoutX(600);
        sp.setPadding(new Insets(30, 30, 30, 0));
        sp.setAlignment(Pos.CENTER);
        sp.getChildren().addAll(rect, text_back);

        HBox hBox = new HBox(name);

        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(20, 0, 20, 30));
        hBox.setMinHeight(100);
        hBox.setMaxHeight(100);
        hBox.setMinWidth(800);
        hBox.setMaxWidth(800);
        hBox.setStyle("-fx-background-color: #336699;");
        bp = new BorderPane(hBox);

        Minefield minefield = new Minefield(10, 10, 10);
        MineSweeperPane minesweeper = new MineSweeperPane(minefield, this,client,clientTwo);
        canvas.getChildren().addAll(minesweeper.asParent());
    }

    public void onPaneClicked(MouseEvent event) {
        if (!spawnMode)
            return;

        Minefield minefield = new Minefield(10, 10, 10);

        MineSweeperPane minesweeper = new MineSweeperPane(minefield, this,client,clientTwo);

        canvas.getChildren().add(minesweeper.asParent());
        spawnMode = false;

    }

    public void setTimer(){
        timer = minesweeper.getTimer();
        System.out.println(timer);
    }

    public Pane getCanvas() {
        return this.canvas;
    }

    public static BorderPane getBp() {
        return bp;
    }

    public static StackPane getSp() {
        return sp;
    }

    public boolean getSpawnMode() {
        return spawnMode;
    }

    public void setSpawnMode(boolean spawn_bool) {
        spawnMode = spawn_bool;
    }

    public String getName_of_game() {
        return name_of_game;
    }
}
