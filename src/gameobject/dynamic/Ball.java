package gameobject.dynamic;

import javafx.geometry.Rectangle2D;
import application.Config;
import application.GameManager;
import gameobject.core.Brick;
import gameobject.core.GameObject;
import gameobject.core.MovableObject;
import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import utils.SoundManager;

import java.util.List;
import java.util.stream.Collectors;

public class Ball extends MovableObject {
    private final double BALL_SPEED = 400; // Tốc độ cơ bản
    private boolean isStrong = false;
    private PauseTransition strongModeTimer;
    private double sceneWidth, sceneHeight;

    private double originalWidth;
    private double originalHeight;

    private static final double STRONG_MODE_SPEED_MODIFIER = 0.6;
    private static final double STRONG_MODE_SIZE_MODIFIER = 1.5;

    public Ball(Pane gameRoot, double startX, double startY, double sceneWidth, double sceneHeight) {

        super(startX, startY, 0, 0, null);

        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;

        Image image = new Image(getClass().getResourceAsStream("/images/ball/Ball2.png"));
        this.imageView = new ImageView(image);

        setWidth(image.getWidth());
        setHeight(image.getHeight());

        resetVelocity();

        imageView.setLayoutX(startX);
        imageView.setLayoutY(startY);
        gameRoot.getChildren().add(this.imageView);

    }

    public ImageView getImageView() {
        return imageView;
    }
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
        return getDx();
    }
    public void setSpeedX(double speedX) {
        setDx(speedX);
    }
    public double getSpeedY() {
        return getDy();
    }
    public void setSpeedY(double speedY) {
        setDy(speedY);
    }

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
    private void updatePosition() {
        imageView.setLayoutX(getX());
        imageView.setLayoutY(getY());
    }

    public void reverseSpeedY() {
        setDy(-getDy());
    }
    public void reverseSpeedX() {
        setDx(-getDx());
    }

    @Override

    public void update(double deltaTime) {
        // 1. Di chuyển bóng (lấy từ MovableObject)
        super.update(deltaTime);

        // 2. Lấy danh sách đối tượng từ GameManager
        GameManager gm = GameManager.getInstance();
        if (gm == null) return;
        List<GameObject> gameObjects = gm.getGameObjects();

        // 3. Kiểm tra va chạm Tường
        checkWallCollisions();

        // 4. Kiểm tra va chạm Thanh Đỡ (Paddle)
        for (GameObject obj : gameObjects) {
            if (obj instanceof Paddle) {
                checkPaddleCollision((Paddle) obj);
                break; // Chỉ có 1 paddle
            }
        }

        // Chỉ chạy khi đang ở màn Boss
        if (gm.getGameState() == GameManager.GameState.BOSS_FIGHT) {
            Boss boss = gm.getBoss();
            if (boss != null && boss.isAlive() && boss.checkBallCollision(this)) {

                boss.takeDamage(1);
                //SoundManager.getInstance().playSoundEffect("/sounds/hit.mp3");
                if (gm.getGameUIController() != null) {
                    gm.getGameUIController().updateBossHealth(boss.getHealth(), boss.getMaxHealth());
                }

                bounceFromBoss(boss);
                return;
            }
        }

        // 6. Kiểm tra va chạm Gạch (Bricks)
        List<Brick> bricks = gameObjects.stream()
                .filter(obj -> obj instanceof Brick)
                .map(obj -> (Brick) obj)
                .filter(brick -> !brick.isDestroyed())
                .collect(Collectors.toList());

        checkBrickCollision(bricks, gm.getGamePane());
    }


    /**
     * Kiểm tra va chạm với 4 cạnh màn hình
     */
    private void checkWallCollisions() {
        if (getX() <= 0) {
            setDx(Math.abs(getDx()));
            setX(0);
        }
        if (getX() + getWidth() >= sceneWidth) {
            setDx(-Math.abs(getDx()));
            setX(sceneWidth - getWidth());
        }
        if (getY() <= 0) {
            setDy(Math.abs(getDy()));
            setY(0);
        }
        if (getY() > sceneHeight) {
            GameManager.getInstance().onBallFallen(this);
        }
    }

    public void checkPaddleCollision(Paddle paddle) {
        if (getBounds().intersects(paddle.getBounds())) {
            utils.SoundManager.getInstance().playSoundEffect("/sounds/TouchPaddle.mp3");
            setDy(-Math.abs(Config.BALL_SPEED_Y));
            double hitPos = (getX() + getWidth() / 2) - (paddle.getX() + paddle.getWidth() / 2);
            double normalizedHitPos = hitPos / (paddle.getWidth() / 2);
            setDx(normalizedHitPos * Config.BALL_MAX_SPEED_X);
            if (isStrong) {
                setDx(getDx() * STRONG_MODE_SPEED_MODIFIER);
                setDy(getDy() * STRONG_MODE_SPEED_MODIFIER);
            }
        }
    }

    public void checkBrickCollision(List<Brick> bricks, Pane gameRoot) {
        for (Brick brick : bricks) {
            if (getBounds().intersects(brick.getBounds())) {
                if (!brick.isUnbreakable()) {
                    if (isStrong) {
                        brick.instantDestroy(gameRoot);
                    } else {
                        brick.hit(gameRoot);
                    }
                    bounceFromBrick(brick);
                } else {
                    bounceFromBrick(brick);
                }
                SoundManager.getInstance().playSoundEffect("/sounds/TouchBrick.mp3");
                break;
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

        double overlapX = (getWidth() / 2 + brick.getWidth() / 2) - Math.abs(dxDiff);
        double overlapY = (getHeight() / 2 + brick.getHeight() / 2) - Math.abs(dyDiff);

        if (overlapX < overlapY) {
            setDx(-getDx());

            if (dxDiff > 0) {
                setX(brick.getX() + brick.getWidth() + 1);
            } else {
                setX(brick.getX() - getWidth() - 1);
            }

        } else {
            setDy(-getDy());
            if (dyDiff > 0) {
                setY(brick.getY() + brick.getHeight() + 1);
            } else {
                setY(brick.getY() - getHeight() - 1);
            }
        }
    }


    public void setPosition(double x, double y) {
        setX(x);
        setY(y);
    }

    public void resetVelocity() {
        setDx(0);
        setDy(Config.BALL_SPEED_Y); // Bay lên trên
    }

    public void activateStrongMode() {
        System.out.println("🔥 Chế độ bóng mạnh (Lớn & Chậm) đã được kích hoạt!");

        if (isStrong) {
            if (strongModeTimer != null) {
                strongModeTimer.stop();
                strongModeTimer.play(); // Chạy lại 10 giây
            }
            return;
        }
        this.isStrong = true;

        this.originalWidth = getWidth();
        this.originalHeight = getHeight();
        setDx(getDx() * STRONG_MODE_SPEED_MODIFIER);
        setDy(getDy() * STRONG_MODE_SPEED_MODIFIER);
        double newWidth = this.originalWidth * STRONG_MODE_SIZE_MODIFIER;
        double newHeight = this.originalHeight * STRONG_MODE_SIZE_MODIFIER;

        super.setWidth(newWidth);
        super.setHeight(newHeight);
        this.imageView.setFitWidth(newWidth);
        this.imageView.setFitHeight(newHeight);

        if (strongModeTimer != null) {
            strongModeTimer.stop();
        }
        strongModeTimer = new PauseTransition(Duration.seconds(10));
        strongModeTimer.setOnFinished(event -> deactivateStrongMode());
        strongModeTimer.play();
    }

    private void deactivateStrongMode() {
        System.out.println(" Chế độ bóng mạnh đã kết thúc.");
        this.isStrong = false;
        double currentDx = getDx();
        double currentDy = getDy();

        double restoredDx = currentDx / STRONG_MODE_SPEED_MODIFIER;
        double restoredDy = currentDy / STRONG_MODE_SPEED_MODIFIER;


        setDx(restoredDx);
        setDy(restoredDy);


        super.setWidth(this.originalWidth);
        super.setHeight(this.originalHeight);
        this.imageView.setFitWidth(this.originalWidth);
        this.imageView.setFitHeight(this.originalHeight);
    }

    public boolean isStrong() {
        return isStrong;
    }

    public void removeGraphics() {
        Pane gamePane = (Pane) this.imageView.getParent();
        if (gamePane != null) {
            gamePane.getChildren().remove(this.imageView);
        }
    }
    public void bounceFromBoss(Boss boss) {
        double ballCenterX = getX() + getWidth() / 2;
        double ballCenterY = getY() + getHeight() / 2;
        double bossCenterX = boss.getX() + boss.getWidth() / 2;
        double bossCenterY = boss.getY() + boss.getHeight() / 2;

        double dxDiff = ballCenterX - bossCenterX;
        double dyDiff = ballCenterY - bossCenterY;

        double overlapX = (getWidth() / 2 + boss.getWidth() / 2) - Math.abs(dxDiff);
        double overlapY = (getHeight() / 2 + boss.getHeight() / 2) - Math.abs(dyDiff);

        if (overlapX < overlapY) {

            setDx(-getDx());

            if (dxDiff > 0) {
                setX(boss.getX() + boss.getWidth() + 2);
            } else {
                setX(boss.getX() - getWidth() - 2);
            }

        } else {
            setDy(-getDy());

            if (dyDiff > 0) {
                setY(boss.getY() + boss.getHeight() + 2);
            } else {
                setY(boss.getY() - getHeight() - 2);
            }
        }
    }


}
