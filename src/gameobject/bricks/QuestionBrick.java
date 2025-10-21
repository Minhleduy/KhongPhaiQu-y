package gameobject.bricks;

import gameobject.DoubleBallItem;
import gameobject.ExpandPaddleItem;
import gameobject.HeartItem;
import gameobject.StrongBallItem;
import gameobject.core.Brick;
import gameobject.dynamic.Paddle;
import javafx.scene.layout.Pane;
import java.util.Random;

/**
 * Gạch bí ẩn (QuestionBrick) — khi phá sẽ sinh ra một item ngẫu nhiên.
 */
public class QuestionBrick extends Brick {

    private final Random random = new Random();
    private Paddle paddle; // THÊM: reference đến paddle

    // SỬA CONSTRUCTOR - THÊM PADDLE
    public QuestionBrick(double x, double y, Pane gamePane, Paddle paddle) {
        super(x, y, 50, 20, 1, "/resources/images/bricks/Brick7.png", gamePane);
        this.paddle = paddle; // LƯU PADDLE
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
                new DoubleBallItem(gamePane, paddle, itemX, itemY); // TRUYỀN PADDLE
                break;
            case 1:
                new ExpandPaddleItem(gamePane, paddle, itemX, itemY); // TRUYỀN PADDLE
                break;
            case 2:
                new HeartItem(gamePane, paddle, itemX, itemY); // TRUYỀN PADDLE
                break;
        }
    }
}