package gameobject.utilis;

import gameobject.dynamic.Ball;
import gameobject.core.Brick;
import gameobject.dynamic.Paddle;
import gameobject.core.PowerUp;

/**
 * Công cụ phát hiện va chạm
 */
public class CollisionDetector {

    // Va chạm ball vs paddle
    public static boolean checkBallPaddle(Ball ball, Paddle paddle) {
        return ball.getImageView().getBoundsInParent()
                .intersects(paddle.getImageView().getBoundsInParent());
    }

    // Va chạm ball vs brick
    public static boolean checkBallBrick(Ball ball, Brick brick) {
        return ball.getImageView().getBoundsInParent()
                .intersects(brick.getImageView().getBoundsInParent());
    }

    // Va chạm powerup vs paddle
    public static boolean checkPowerUpPaddle(PowerUp powerUp, Paddle paddle) {
        return powerUp.getImageView().getBoundsInParent()
                .intersects(paddle.getImageView().getBoundsInParent());
    }

    // Va chạm ball vs tường
    public static boolean checkBallWall(Ball ball, double sceneWidth, double sceneHeight) {
        return ball.getX() <= 0 ||
                ball.getX() + ball.getWidth() >= sceneWidth ||
                ball.getY() <= 0;
    }

    // Va chạm ball vs đáy (mất mạng)
    public static boolean checkBallBottom(Ball ball, double sceneHeight) {
        return ball.getY() >= sceneHeight;
    }
}