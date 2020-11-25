package View;

import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class MenuItem extends StackPane {
    public static String color = "#7C4700";

    public MenuItem(String name) {

        Rectangle bg = new Rectangle(180, 30, Color.TRANSPARENT);

        if (name.startsWith("Открыть ячейку") || name.startsWith("Поставить флаг")){
            bg = new Rectangle(400, 30, Color.TRANSPARENT);
        }


        Text text = new Text(name);
        text.setFill(Color.WHITE);
        text.setFont(Font.font("file:src/main/java/webapp/fonts/OptimusPrinceps.ttf",FontWeight.BOLD,18));
        setAlignment(Pos.CENTER);
        getChildren().addAll(bg, text);

        FillTransition ft = new FillTransition(Duration.seconds(1), bg);
        setOnMouseEntered(event -> {
            ft.setToValue(Color.web(color));
            ft.setAutoReverse(false);
            ft.play();
        });

        Rectangle finalBg = bg;
        setOnMouseExited(event -> {
            ft.stop();
            finalBg.setFill(Color.TRANSPARENT);
        });

    }
}
