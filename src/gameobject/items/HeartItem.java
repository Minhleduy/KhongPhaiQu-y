package gameobject.items;

import application.GameManager;
import gameobject.core.PowerUp;
import gameobject.dynamic.Paddle;
import javafx.scene.layout.Pane;

public class HeartItem extends PowerUp {
    public HeartItem(Pane gameRoot, Paddle paddle, double x, double y) {
        super(gameRoot, paddle, x, y, "/images/items/Item5.png");
    }

    @Override
    protected void applyEffect(Paddle paddle) {
        GameManager gm = GameManager.getInstance();
        gm.addLife(); // <-- Sửa lại dòng này
    }
}
