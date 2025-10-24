package gameobject.items;

import gameobject.core.PowerUp;
import gameobject.dynamic.Ball;
import gameobject.dynamic.Paddle;
import javafx.scene.layout.Pane;

/**
 * Item Double Ball - khi ăn sẽ tạo thêm một quả bóng mới
 */
public class DoubleBallItem extends PowerUp {

    // SỬA: ĐÚNG CONSTRUCTOR - CẦN PADDLE
    public DoubleBallItem(Pane gameRoot, Paddle paddle, double x, double y) {
        super(gameRoot, paddle, x, y, "/resources/images/items/Item4.png");
    }

    @Override
    protected void applyEffect(Paddle paddle) {
        Pane gameRoot = (Pane) paddle.getImageView().getParent();

        double newBallX = paddle.getX() + paddle.getWidth() / 2 - 10;
        double newBallY = paddle.getY() - 20;

        // Tạo bóng mới
        Ball newBall = new Ball(
                gameRoot,
                newBallX,
                newBallY,
                gameRoot.getWidth(),
                gameRoot.getHeight()
        );

        System.out.println("Double Ball Item activated! New ball created.");
    }
}