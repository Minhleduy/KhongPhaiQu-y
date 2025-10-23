package gameobject.bricks;

import gameobject.DoubleBallItem;
import gameobject.core.Brick;
import gameobject.dynamic.Paddle;
import javafx.scene.layout.Pane;

/**
 * Gạch Double Ball - khi phá sẽ rơi ra item tạo thêm bóng
 */
public class DoubleBallBrick extends Brick {

    private Paddle paddle;

    public DoubleBallBrick(double x, double y, Pane gamePane, Paddle paddle) {
        super(x, y, 50, 20, 1, "/resources/images/bricks/Brick2.png", gamePane);
        this.paddle = paddle;
    }

    @Override
    public void hit(Pane gamePane) {
        super.hit(gamePane);

        if (isDestroyed()) {
            spawnDoubleBallItem(gamePane);
        }
    }

    private void spawnDoubleBallItem(Pane gamePane) {
        double itemX = getX() + getWidth() / 2;
        double itemY = getY() + getHeight();

        new DoubleBallItem(gamePane, paddle, itemX, itemY);
    }
}