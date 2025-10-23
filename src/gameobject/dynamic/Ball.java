package gameobject.dynamic;

import gameobject.core.Brick;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.List;

public class Ball {
    private ImageView imageView;
    private double x, y;
    private double width, height;
    private double dx, dy;
    private final double BALL_SPEED = 5;

    private double sceneWidth, sceneHeight;
    private Timeline timeline;

    public Ball(Pane gameRoot, double startX, double startY, double sceneWidth, double sceneHeight) {
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;

        // Load ảnh
        Image image = new Image(getClass().getResourceAsStream("/resources/images/ball/Ball1.png"));
        imageView = new ImageView(image);
        width = image.getWidth();
        height = image.getHeight();

        x = startX;
        y = startY;
        dx = BALL_SPEED;
        dy = -BALL_SPEED;

        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
        gameRoot.getChildren().add(imageView);

        startMoving();
    }

    // --- Getter để check va chạm ---
    public ImageView getImageView() {
        return imageView;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getWidth() {
        return width;
    }
    public double getHeight() {
        return height;
    }

    public void setX(double x) {
        this.x = x;
        updatePosition();
    }
    public void setY(double y) {
        this.y = y;
        updatePosition();
    }

    // --- Cập nhật ImageView ---
    private void updatePosition() {
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
    }

    // --- Di chuyển bóng ---
    private void startMoving() {
        timeline = new Timeline(new KeyFrame(Duration.millis(16), e -> update()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void update() {
        x += dx;
        y += dy;
        updatePosition();

        // Va chạm biên
        if (x <= 0 || x + width >= sceneWidth) dx *= -1;
        if (y <= 0) dy *= -1;

        // Nếu rơi ra ngoài màn hình
        if (y > sceneHeight) resetBall();
    }

    // --- Va chạm với paddle ---
    public void checkPaddleCollision(Paddle paddle) {
        if (imageView.getBoundsInParent().intersects(paddle.getImageView().getBoundsInParent())) {
            dy = -Math.abs(dy); // bật lên
            double hitPos = (x + width / 2) - (paddle.getX() + paddle.getWidth() / 2);
            dx = hitPos * 0.1;
        }
    }

    // --- Va chạm với gạch ---
    public void checkBrickCollision(List<? extends Brick> bricks, Pane gameRoot) {
        for (Brick brick : bricks) {
            if (imageView.getBoundsInParent().intersects(brick.getImageView().getBoundsInParent())) {
                if (brick.isUnbreakable()) {
                    bounceFromBrick(brick);
                } else {
                    brick.hit(gameRoot);
                    bounceFromBrick(brick);
                }
                break; // chỉ xử lý 1 viên gạch 1 frame
            }
        }
    }

    private void bounceFromBrick(Brick brick) {
        double ballCenterX = x + width / 2;
        double ballCenterY = y + height / 2;
        double brickCenterX = brick.getX() + brick.getWidth() / 2;
        double brickCenterY = brick.getY() + brick.getHeight() / 2;

        double dxDiff = ballCenterX - brickCenterX;
        double dyDiff = ballCenterY - brickCenterY;

        if (Math.abs(dxDiff) > Math.abs(dyDiff)) dx *= -1;
        else dy *= -1;
    }

    private void resetBall() {
        x = sceneWidth / 2 - width / 2;
        y = sceneHeight / 2 - height / 2;
        dx = BALL_SPEED;
        dy = -BALL_SPEED;
        updatePosition();
    }

    public void setPosition(double centerX, double centerY) {
        this.x = centerX - Config.BALL_RADIUS; // Tọa độ x của hình tròn là góc trái trên
        this.y = centerY - Config.BALL_RADIUS; // Tọa độ y của hình tròn là góc trái trên
    }

    public void resetVelocity() {
        this.speedX = Config.BALL_INITIAL_SPEED_X;
        // Dùng Math.abs để đảm bảo tốc độ Y luôn là số âm (bay lên trên)
        this.speedY = -Math.abs(Config.BALL_INITIAL_SPEED_Y);
    }
}
