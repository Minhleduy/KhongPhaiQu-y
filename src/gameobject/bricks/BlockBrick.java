package gameobject.bricks;

import gameobject.items.BlockItem;  // SỬA: thêm items.
import gameobject.core.Brick;
import gameobject.dynamic.Paddle;
import javafx.scene.layout.Pane;

/**
 * Gạch khiên bảo vệ - khi phá sẽ rơi ra item Block (bảo vệ không mất mạng)
 */
public class BlockBrick extends Brick {

    private Paddle paddle;

    public BlockBrick(double x, double y, Pane gamePane, Paddle paddle) {
        super(x, y, 50, 20, 1, "/resources/images/bricks/block_brick.png", gamePane);
        this.paddle = paddle;
    }

    @Override
    public void hit(Pane gamePane) {
        super.hit(gamePane);

        if (isDestroyed()) {
            spawnBlockItem(gamePane);
        }
    }

    private void spawnBlockItem(Pane gamePane) {
        double itemX = getX() + getWidth() / 2;
        double itemY = getY() + getHeight();

        new BlockItem(gamePane, paddle, itemX, itemY);  // GIỜ ĐÚNG PACKAGE
    }
}