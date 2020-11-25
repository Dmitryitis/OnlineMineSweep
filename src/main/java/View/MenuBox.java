package View;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MenuBox extends Pane {
    static SubMenu subMenu;
    private String type;
    Rectangle bg = new Rectangle(900, 600, Color.BLACK);

    public MenuBox(SubMenu subMenu) {
        MenuBox.subMenu = subMenu;
        this.type = subMenu.get_type();
        setVisible(false);
        bg.setOpacity(1);
        getChildren().addAll(bg, subMenu);
    }

    public void setSubMenu(SubMenu subMenu) {
        getChildren().remove(MenuBox.subMenu);
        MenuBox.subMenu = subMenu;
        this.type = subMenu.get_type();
        System.out.println("MenuBox: "+this.type);
        if (type.equals("gameField")){
            System.out.println("211");
            bg.setFill(Color.WHITE);
            bg.setOpacity(1);
        }
        getChildren().add(MenuBox.subMenu);
    }

    public String getType() {
        return this.type;
    }
}
