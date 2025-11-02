package gameobject.bricks;

import gameobject.items.BlockItem;
import gameobject.core.Brick;
import gameobject.dynamic.Paddle;
import javafx.scene.layout.Pane;
import application.Config;


public class BlockBrick extends Brick {

    private Paddle paddle;

    public BlockBrick(double x, double y, Pane gamePane, Paddle paddle) {
        super(x, y, Config.BRICK_WIDTH, Config.BRICK_HEIGHT, 1, "/images/brick/Brick1.png", gamePane);
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

        new BlockItem(gamePane, paddle, itemX, itemY);
    }
}
