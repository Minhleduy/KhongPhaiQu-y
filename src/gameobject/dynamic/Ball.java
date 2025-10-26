package gameobject.dynamic;

import gameobject.core.Brick;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import application.Config;
import gameobject.core.MovableObject;
import javafx.animation.PauseTransition;

import java.util.List;

public class Ball extends MovableObject {
    private ImageView imageView;
    private final double BALL_SPEED = 5;
    private double speedX;
    private double speedY;
    private boolean isStrong = false;
    private PauseTransition strongModeTimer;
    private double sceneWidth, sceneHeight;
    private Timeline timeline;

    public Ball(Pane gameRoot, double startX, double startY, double sceneWidth, double sceneHeight) {

        super(startX, startY, 0, 0, null);

        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;

        // Load ảnh
        Image image = new Image(getClass().getResourceAsStream("/resources/images/ball/Ball1.png"));
        imageView = new ImageView(image);

        // Cập nhật width, height từ ảnh thực tế
        setWidth(image.getWidth());
        setHeight(image.getHeight());

        // Set vận tốc ban đầu
        setDx(BALL_SPEED);
        setDy(-BALL_SPEED);

        imageView.setLayoutX(startX);
        imageView.setLayoutY(startY);
        gameRoot.getChildren().add(imageView);

        startMoving();
    }

    // --- Getter để check va chạm ---
    public ImageView getImageView() {
        return imageView;
    }

    // SỬA: Dùng getter từ GameObject thay vì biến riêng
    public double getX() {
        return super.getX();
    }
    public double getY() {
        return super.getY();
    }
    public double getWidth() {
        return super.getWidth();
    }
    public double getHeight() {
        return super.getHeight();
    }

    public double getSpeedX() {
        return speedX;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    // SỬA: Ghi đè setX/setY để cập nhật cả ImageView
    @Override
    public void setX(double x) {
        super.setX(x);
        updatePosition();
    }

    @Override
    public void setY(double y) {
        super.setY(y);
        updatePosition();
    }

    // --- Cập nhật ImageView ---
    private void updatePosition() {
        imageView.setLayoutX(getX());
        imageView.setLayoutY(getY());
    }

    // --- Di chuyển bóng ---
    private void startMoving() {
        timeline = new Timeline(new KeyFrame(Duration.millis(16), e -> update(16.0/1000.0)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void reverseSpeedY() {
        this.speedY = -this.speedY;
    }

    public void reverseSpeedX() {
        this.speedX = -this.speedX;
    }

    // SỬA: Ghi đè update method
    @Override
    public void update(double deltaTime) {
        // Dùng logic di chuyển từ MovableObject
        super.update(deltaTime);

        // Va chạm biên
        if (getX() <= 0 || getX() + getWidth() >= sceneWidth) setDx(-getDx());
        if (getY() <= 0) setDy(-getDy());

        // Nếu rơi ra ngoài màn hình
        if (getY() > sceneHeight) resetBall();
    }

    // --- Va chạm với paddle ---
    public void checkPaddleCollision(Paddle paddle) {
        if (imageView.getBoundsInParent().intersects(paddle.getImageView().getBoundsInParent())) {
            setDy(-Math.abs(getDy())); // bật lên
            double hitPos = (getX() + getWidth() / 2) - (paddle.getX() + paddle.getWidth() / 2);
            setDx(hitPos * 0.1);
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
        double ballCenterX = getX() + getWidth() / 2;
        double ballCenterY = getY() + getHeight() / 2;
        double brickCenterX = brick.getX() + brick.getWidth() / 2;
        double brickCenterY = brick.getY() + brick.getHeight() / 2;

        double dxDiff = ballCenterX - brickCenterX;
        double dyDiff = ballCenterY - brickCenterY;

        if (Math.abs(dxDiff) > Math.abs(dyDiff)) setDx(-getDx());
        else setDy(-getDy());
    }

    private void resetBall() {
        setX(sceneWidth / 2 - getWidth() / 2);
        setY(sceneHeight / 2 - getHeight() / 2);
        setDx(BALL_SPEED);
        setDy(-BALL_SPEED);
        updatePosition();
    }

    public void setPosition(double centerX, double centerY) {
        setX(centerX - Config.BALL_RADIUS); // Tọa độ x của hình tròn là góc trái trên
        setY(centerY - Config.BALL_RADIUS); // Tọa độ y của hình tròn là góc trái trên
    }

    public void resetVelocity() {
        this.speedX = Config.BALL_INITIAL_SPEED_X;
        // Dùng Math.abs để đảm bảo tốc độ Y luôn là số âm (bay lên trên)
        this.speedY = -Math.abs(Config.BALL_INITIAL_SPEED_Y);
    }

    public void activateStrongMode() {
        System.out.println("🔥 Chế độ bóng mạnh đã được kích hoạt!");
        this.isStrong = true;

        if (strongModeTimer != null) {
            strongModeTimer.stop();
        }

        strongModeTimer = new PauseTransition(Duration.seconds(10));
        strongModeTimer.setOnFinished(event -> deactivateStrongMode());
        strongModeTimer.play();
    }

    private void deactivateStrongMode() {
        System.out.println("💧 Chế độ bóng mạnh đã kết thúc.");
        this.isStrong = false;
    }

    public boolean isStrong() {
        return isStrong;
    }
}
