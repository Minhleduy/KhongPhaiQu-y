package gameobject;

import gameobject.core.PowerUp;
import gameobject.dynamic.Paddle;
import javafx.scene.layout.Pane;

/**
 * Item trái tim - khi ăn được sẽ tăng thêm 1 mạng cho người chơi
 */
public class HeartItem extends PowerUp {

    public HeartItem(Pane gameRoot, Paddle paddle, double x, double y) {
        super(gameRoot, paddle, x, y, "/resources/images/items/Item5.png");
    }

    @Override
    protected void applyEffect(Paddle paddle) {
        // Tăng mạng cho người chơi
        // GIẢ SỬ: Paddle có method addLife() hoặc tăng biến lives
        // paddle.addLife(); hoặc paddle.setLives(paddle.getLives() + 1);

        System.out.println("Heart Item activated! Extra life added.");


    }
}