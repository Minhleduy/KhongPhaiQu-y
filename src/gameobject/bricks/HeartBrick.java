package gameobject.bricks;

import gameobject.items.HeartItem;
import gameobject.core.Brick;
import gameobject.dynamic.Paddle;
import javafx.scene.layout.Pane;

/**
 * Gạch trái tim - khi phá sẽ rơi ra item tăng mạng
 */
public class HeartBrick extends Brick {

    private Paddle paddle;


    public HeartBrick(double x, double y, Pane gamePane, Paddle paddle) {
        super(x, y, 50, 20, 1, "/resources/images/bricks/Brick8.png", gamePane);
        this.paddle = paddle;


    }

    @Override
    public void hit(Pane gamePane) {
        super.hit(gamePane);

        if (isDestroyed()) {
            spawnHeartItem(gamePane);
        }
    }

    private void spawnHeartItem(Pane gamePane) {
        double itemX = getX() + getWidth() / 2;
        double itemY = getY() + getHeight();

        new HeartItem(gamePane, paddle, itemX, itemY);
    }
}