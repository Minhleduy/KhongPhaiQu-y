package gameobject.bricks;

import gameobject.items.DoubleBallItem;
import gameobject.items.ExpandPaddleItem;  // SỬA THÀNH items.ExpandPaddleItem
import gameobject.items.HeartItem;
import gameobject.core.Brick;
import gameobject.dynamic.Paddle;
import javafx.scene.layout.Pane;
import java.util.Random;

/**
 * Gạch bí ẩn (QuestionBrick) — khi phá sẽ sinh ra một item ngẫu nhiên.
 */
public class QuestionBrick extends Brick {

    private final Random random = new Random();
    private Paddle paddle;

    public QuestionBrick(double x, double y, Pane gamePane, Paddle paddle) {
        super(x, y, 50, 20, 1, "/resources/images/bricks/Brick7.png", gamePane);
        this.paddle = paddle;
    }

    @Override
    public void hit(Pane gamePane) {
        super.hit(gamePane);

        if (isDestroyed()) {
            spawnRandomItem(gamePane);
        }
    }

    private void spawnRandomItem(Pane gamePane) {
        int type = random.nextInt(3);
        double itemX = getX() + getWidth() / 2;
        double itemY = getY() + getHeight();

        switch (type) {
            case 0:
                new DoubleBallItem(gamePane, paddle, itemX, itemY);
                break;
            case 1:
                new ExpandPaddleItem(gamePane, paddle, itemX, itemY);  // GIỜ ĐÚNG PACKAGE
                break;
            case 2:
                new HeartItem(gamePane, paddle, itemX, itemY);
                break;
        }
    }
}