package gameobject.items;

import application.GameManager;
import gameobject.core.PowerUp;
import gameobject.dynamic.Ball;
import gameobject.dynamic.Paddle;
import javafx.scene.layout.Pane;

public class DoubleBallItem extends PowerUp {
    public DoubleBallItem(Pane gameRoot, Paddle paddle, double x, double y) {
        super(gameRoot, paddle, x, y, "/images/items/Item4.png");
    }

    @Override
    protected void applyEffect(Paddle paddle) {
        Pane gameRoot = (Pane) paddle.getImageView().getParent();
        double newBallX = paddle.getX() + paddle.getWidth() / 2 - 10;
        double newBallY = paddle.getY() - 20;

        Ball newBall = new Ball(
                gameRoot,
                newBallX,
                newBallY,
                gameRoot.getWidth(),
                gameRoot.getHeight()
        );
        GameManager.getInstance().addGameObject(newBall); // THÊM VÀO DANH SÁCH
        System.out.println("Double Ball Item activated! New ball created.");
    }
}
