package Controller;

import View.GameField;
import View.MenuBox;
import View.MenuItem;
import View.SubMenu;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.Socket;

import static View.GameField.bp;
import static View.GameField.canvas;

public class Main extends Application {
    final int SIZE_W = 800;
    final int SIZE_H = 600;
    final String n_game = "Minesweeper Online";
    final String help_t = "Нажмите на Escape, чтобы начать";
    boolean escappe = false;
    Client client = new Client();
    static GameField gF = new GameField();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();

//        name_game.setX(270);
//        name_game.setY(300);
        Text name_game = new Text(n_game);
        name_game.setFill(Color.WHITE);
        name_game.setFont(Font.font("file:MineSweeperClient/src/main/java/webapp/fonts/OptimusPrinceps.ttf", FontWeight.BOLD, 30));

        Text minesweeper = new Text(n_game);
        minesweeper.setFill(Color.WHITE);
        minesweeper.setFont(Font.font("file:MineSweeperClient/src/main/java/webapp/fonts/OptimusPrinceps.ttf", FontWeight.BOLD, 40));

        Text help_text = new Text(help_t);
        help_text.setFill(Color.WHITE);
        help_text.setFont(Font.font("file:MineSweeperClient/src/main/java/webapp/fonts/OptimusPrinceps.ttf", FontWeight.BOLD, 14));


        FadeTransition h_t = new FadeTransition(Duration.seconds(1), help_text);
        h_t.setFromValue(0);
        h_t.setToValue(1);
        h_t.setAutoReverse(true);
        h_t.setCycleCount(Animation.INDEFINITE);
        h_t.play();

        FadeTransition title = new FadeTransition(Duration.seconds(2), minesweeper);
        title.setFromValue(0);
        title.setToValue(1);
        title.play();

        SubMenu start_scene = new SubMenu(minesweeper, help_text);
//        start_scene.setLayoutX(150);
//        start_scene.setLayoutY(150);

        MenuBox menuBox = new MenuBox(start_scene);
        menuBox.setVisible(true);
        root.getChildren().addAll(menuBox);

        MenuItem newGame = new MenuItem("ИГРАТЬ");
        MenuItem options = new MenuItem("УПРАВЛЕНИЕ");
        MenuItem rules = new MenuItem("ПРАВИЛА");
        MenuItem exitGame = new MenuItem("ВЫХОД");

        SubMenu mainMenu = new SubMenu(name_game, newGame, options, rules, exitGame);
        mainMenu.setLayoutX(220);
        mainMenu.setLayoutY(100);

        MenuItem NG1 = new MenuItem("ИГРАТЬ ПО СЕТИ");
        MenuItem NG2 = new MenuItem("ИГРАТЬ С БОТОМ");
        MenuItem NG3 = new MenuItem("ОДИНОЧНАЯ ИГРА");
        MenuItem NG4 = new MenuItem("НАЗАД");

        SubMenu newGameMenu = new SubMenu(NG3, NG1, NG2, NG4);
        newGameMenu.setLayoutX(250);
        newGameMenu.setLayoutY(150);

        MenuItem options_one = new MenuItem("Открыть ячейку: Левая кнопка мыши");
        MenuItem options_two = new MenuItem("Поставить флаг: Правая кнопка мыши");
        MenuItem options_back = new MenuItem("Назад");

        SubMenu about_options = new SubMenu(options_one, options_two, options_back);
        about_options.setLayoutX(150);
        about_options.setLayoutY(150);

        Text rules_text = new Text("Цель игры: найти все мины на поле, отметив их флажками" + '\n' +
                "Игра проиграна, если вы открыли ячейку, с миной" + "\n" +
                "Цифры на поле помогут понять сколько мин вокруг");
        rules_text.setFill(Color.WHITE);
        rules_text.setFont(Font.font("file:MineSweeperClient/src/main/java/webapp/fonts/OptimusPrinceps.ttf", FontWeight.BOLD, 16));
        MenuItem rule_back = new MenuItem("НАЗАД");

        SubMenu about_rule = new SubMenu(rules_text, rule_back);
        about_rule.setLayoutX(150);
        about_rule.setLayoutY(150);

        gF.setVisible(false);

        Scene scene = new Scene(root, SIZE_W, SIZE_H);
        scene.setFill(Color.BLACK);

        String main_m = "MineSweeperClient/src/main/webapp/music/mainTheme.mp3";
        Media main_mus = new Media(new File(main_m).toURI().toString());
        MediaPlayer mainPlayer = new MediaPlayer(main_mus);
        mainPlayer.autoPlayProperty();
        mainPlayer.pause();


        options.setOnMouseClicked(event -> {
            menuBox.setSubMenu(about_options);
            root.getChildren().remove(menuBox);
            root.getChildren().addAll(menuBox);
        });
        newGame.setOnMouseClicked(event -> {
            menuBox.setSubMenu(newGameMenu);
            root.getChildren().remove(menuBox);
            root.getChildren().addAll(menuBox);
        });
        exitGame.setOnMouseClicked(event -> System.exit(0));
        NG4.setOnMouseClicked(event -> {
            menuBox.setSubMenu(mainMenu);
            root.getChildren().remove(menuBox);
            root.getChildren().addAll(menuBox);
        });
        NG3.setOnMouseClicked(event -> {
                    mainPlayer.pause();
                    menuBox.setVisible(false);
                    gF = new GameField();
                    gF.setSpawnMode(true);
                    gF.setParametrField();
                    root.getChildren().addAll(bp, GameField.sp);
                    root.getChildren().add(canvas);
                    gF.setVisible(true);
                    root.getChildren().addAll(gF);

                    root.getChildren().remove(name_game);
                    root.getChildren().remove(menuBox);
                    root.getChildren().remove(name_game);
                }
        );
        options_back.setOnMouseClicked(event -> {
            menuBox.setSubMenu(mainMenu);
            root.getChildren().remove(menuBox);
            root.getChildren().addAll(menuBox);
        });
        rules.setOnMouseClicked(event -> {
            menuBox.setSubMenu(about_rule);
            root.getChildren().remove(menuBox);
            root.getChildren().addAll(menuBox);
        });
        rule_back.setOnMouseClicked(event -> {
            menuBox.setSubMenu(mainMenu);
            root.getChildren().remove(menuBox);
            root.getChildren().addAll(menuBox);
        });


        if (gF.isVisible()) {
            gF.setOnMouseClicked(gF::onPaneClicked);
        }

        StackPane btn_back = GameField.getSp();
        btn_back.setOnMouseClicked(event -> {
            menuBox.setVisible(true);
            menuBox.setSubMenu(mainMenu);
            gF.setVisible(false);
            root.getChildren().remove(gF);
            root.getChildren().remove(btn_back);
            root.getChildren().remove(bp);
            root.getChildren().remove(GameField.canvas);
            root.getChildren().addAll(menuBox);
        });
        NG2.setOnMouseClicked(event -> {
           client = new Client("dima", 4004);
           ClientTwo clientTwo = new ClientTwo("alex",4004);
           GameField server_game = new GameField(client,"serverGame");
            try {
                client.setWorkForServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            mainPlayer.pause();
            menuBox.setVisible(false);

            server_game.setSpawnMode(true);
            server_game.setParametrField();
            root.getChildren().addAll(bp, GameField.sp);
            root.getChildren().add(canvas);
            server_game.setVisible(true);
            root.getChildren().addAll(server_game);

            root.getChildren().remove(name_game);
            root.getChildren().remove(menuBox);
            root.getChildren().remove(name_game);
        });



        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE && !escappe) {
                String path_music = "MineSweeperClient/src/main/webapp/music/sound_click.mp3";
                Media media = new Media(new File(path_music).toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();

                menuBox.setSubMenu(mainMenu);
                root.getChildren().remove(menuBox);
                root.getChildren().addAll(menuBox);

                FadeTransition ft = new FadeTransition(Duration.seconds(2), menuBox);
                escappe = true;

                ft.setFromValue(0);
                ft.setToValue(1);
                ft.play();
                PauseTransition wait = new PauseTransition(Duration.seconds(2));

                wait.setOnFinished(event1 -> {
                    mainPlayer.setStartTime(Duration.seconds(11));
                    mainPlayer.play();

                });
                wait.play();

            }
        });
        primaryStage.setScene(scene);
        primaryStage.setTitle("MineSweeper");
        primaryStage.show();
    }

//    public static void main(String[] args) {
//        launch(args);
//    }

}
