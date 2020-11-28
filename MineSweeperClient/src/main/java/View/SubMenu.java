package View;

import View.GameField;
import View.MenuItem;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SubMenu extends VBox {
    private String type;
    public SubMenu(MenuItem...menuItem) {
        setSpacing(15);
        setTranslateY(100);
        setTranslateX(50);
        this.type = "menu";
        for (MenuItem item : menuItem) {
            getChildren().addAll(item);
        }
    }

    public SubMenu(Text title,MenuItem...menuItem){
        setSpacing(15);
        setTranslateY(100);
        setTranslateX(50);
        setAlignment(Pos.CENTER);
        this.type = "menu";
        getChildren().add(title);
        for (MenuItem item : menuItem) {
            getChildren().addAll(item);
        }
    }

    public SubMenu(Text name_game,Text help_text){
        setSpacing(15);
        setTranslateX(200);
        setTranslateY(240);
        setAlignment(Pos.CENTER);
        getChildren().add(name_game);
        getChildren().add(help_text);
    }

    public SubMenu(GameField gameField){
        this.type = "gameField";
    }

    public String get_type(){
        System.out.println("SubMenu: "+this.type);
        return this.type;
    }
}
