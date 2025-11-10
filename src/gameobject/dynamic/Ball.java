package gameobject.dynamic;

// ✅ CÁC IMPORT CẦN THIẾT
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
    //private ImageView imageView;
    private final double BALL_SPEED = 400; // Tốc độ cơ bản
    private boolean isStrong = false;
    private PauseTransition strongModeTimer;
    private double sceneWidth, sceneHeight;

    private double originalWidth;
    private double originalHeight;

    private static final double STRONG_MODE_SPEED_MODIFIER = 0.6; // Tốc độ giảm còn 60%
    private static final double STRONG_MODE_SIZE_MODIFIER = 1.5;  // Kích thước tăng 150%

    public Ball(Pane gameRoot, double startX, double startY, double sceneWidth, double sceneHeight) {

        super(startX, startY, 0, 0, null);

        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;

        Image image = new Image(getClass().getResourceAsStream("/images/ball/Ball2.png"));
        this.imageView = new ImageView(image);

        setWidth(image.getWidth());
        setHeight(image.getHeight());

        resetVelocity(); // Đặt vận tốc ban đầu

        imageView.setLayoutX(startX);
        imageView.setLayoutY(startY);
        gameRoot.getChildren().add(this.imageView);

        // startMoving(); // ⛔ LỖI 1: Đã xóa lệnh gọi Vòng Lặp Thừa
    }

    // --- Getter & Setter (Sửa lại để dùng dx, dy) ---
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

    // ✅✅✅ LỖI 2: VIẾT LẠI HOÀN TOÀN HÀM UPDATE() ✅✅✅
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

        // 5. --- LOGIC MỚI: KIỂM TRA VA CHẠM BOSS ---
        // Chỉ chạy khi đang ở màn Boss
        if (gm.getGameState() == GameManager.GameState.BOSS_FIGHT) {
            Boss boss = gm.getBoss();
            if (boss != null && boss.isAlive() && boss.checkBallCollision(this)) {

                // --- Toàn bộ logic va chạm Boss được chuyển vào đây ---
                boss.takeDamage(1);
                //SoundManager.getInstance().playSoundEffect("/sounds/hit.mp3");
                if (gm.getGameUIController() != null) {
                    gm.getGameUIController().updateBossHealth(boss.getHealth(), boss.getMaxHealth());
                }

                bounceFromBoss(boss); // <-- Dùng hàm vật lý của Ball

                // QUAN TRỌNG: Dừng update frame này tại đây
                // để ngăn nó kiểm tra va chạm Gạch (sửa lỗi Race Condition)
                return;
            }
        }

        // 6. Kiểm tra va chạm Gạch (Bricks)
        // (Chỉ chạy nếu KHÔNG va chạm với Boss ở trên)
        List<Brick> bricks = gameObjects.stream()
                .filter(obj -> obj instanceof Brick)
                .map(obj -> (Brick) obj)
                .filter(brick -> !brick.isDestroyed())
                .collect(Collectors.toList());

        checkBrickCollision(bricks, gm.getGamePane());
    }


    /**
     * Hàm mới: Kiểm tra va chạm với 4 cạnh màn hình
     */
    private void checkWallCollisions() {
        // Va chạm biên trái / phải
        if (getX() <= 0) {
            setDx(Math.abs(getDx())); // Luôn nảy về bên phải
            setX(0);
        }
        if (getX() + getWidth() >= sceneWidth) {
            setDx(-Math.abs(getDx())); // Luôn nảy về bên trái
            setX(sceneWidth - getWidth());
        }

        // Va chạm biên trên
        if (getY() <= 0) {
            setDy(Math.abs(getDy())); // Luôn nảy xuống
            setY(0);
        }

        // Rơi ra ngoài biên dưới
        if (getY() > sceneHeight) {
            GameManager.getInstance().onBallFallen(this);
        }
    }

    // --- Va chạm với paddle ---
    public void checkPaddleCollision(Paddle paddle) {
        if (getBounds().intersects(paddle.getBounds())) {
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

    // --- Va chạm với gạch ---
    public void checkBrickCollision(List<Brick> bricks, Pane gameRoot) {
        for (Brick brick : bricks) {
            if (getBounds().intersects(brick.getBounds())) {
                if (!brick.isUnbreakable()) {
                    if (isStrong) {
//                        brick.hitPoints = 0; // phá ngay
//                        brick.destroy(gameRoot);
                        brick.instantDestroy(gameRoot);
                    } else {
                        brick.hit(gameRoot);
                    }
                    bounceFromBrick(brick);
                } else {
                    bounceFromBrick(brick);
                }
                SoundManager.getInstance().playSoundEffect("/sounds/hit.mp3");
                break;
            }
        }
    }


    private void bounceFromBrick(Brick brick) {
        // ... (phần tính toán dxDiff, dyDiff, overlapX, overlapY giữ nguyên) ...
        double ballCenterX = getX() + getWidth() / 2;
        double ballCenterY = getY() + getHeight() / 2;
        double brickCenterX = brick.getX() + brick.getWidth() / 2;
        double brickCenterY = brick.getY() + brick.getHeight() / 2;

        double dxDiff = ballCenterX - brickCenterX;
        double dyDiff = ballCenterY - brickCenterY;

        double overlapX = (getWidth() / 2 + brick.getWidth() / 2) - Math.abs(dxDiff);
        double overlapY = (getHeight() / 2 + brick.getHeight() / 2) - Math.abs(dyDiff);

        // So sánh độ chồng lấn
        if (overlapX < overlapY) {
            // --- VA CHẠM NGANG (TRÁI/PHẢI) ---

            // 1. Đảo ngược vận tốc X
            setDx(-getDx());

            // 2. ĐẨY BÓNG RA (Push-out) - LOGIC ĐÃ SỬA
            if (dxDiff > 0) {
                // Ball ở bên PHẢI tâm gạch (va chạm mặt TRÁI)
                // -> Đẩy bóng sang PHẢI (ra xa)
                setX(brick.getX() + brick.getWidth() + 1);
            } else {
                // Ball ở bên TRÁI tâm gạch (va chạm mặt PHẢI)
                // -> Đẩy bóng sang TRÁI (ra xa)
                setX(brick.getX() - getWidth() - 1);
            }

        } else {
            // --- VA CHẠM DỌC (TRÊN/DƯỚI) ---

            // 1. Đảo ngược vận tốc Y
            setDy(-getDy());

            // 2. ĐẨY BÓNG RA (Push-out) - LOGIC ĐÃ SỬA
            if (dyDiff > 0) {
                // Ball ở BÊN DƯỚI tâm gạch (va chạm mặt TRÊN)
                // -> Đẩy bóng XUỐNG DƯỚI (ra xa)
                setY(brick.getY() + brick.getHeight() + 1);
            } else {
                // Ball ở BÊN TRÊN tâm gạch (va chạm mặt DƯỚI)
                // -> Đẩy bóng LÊN TRÊN (ra xa)
                setY(brick.getY() - getHeight() - 1);
            }
        }
    }






    // ⛔ Đã xóa hàm resetBall() (GameManager sẽ lo việc reset vị trí)

    public void setPosition(double x, double y) {
        setX(x);
        setY(y);
    }

    public void resetVelocity() {
        setDx(0);
        setDy(Config.BALL_SPEED_Y); // Bay lên trên
    }

    // Hàm getBounds() để va chạm chính xác
//    public javafx.geometry.Bounds getBounds() {
//        return imageView.getBoundsInParent();
//    }

    // --- Các hàm Strong Mode (Giữ nguyên) ---
    public void activateStrongMode() {
        System.out.println("🔥 Chế độ bóng mạnh (Lớn & Chậm) đã được kích hoạt!");

        // 1. Nếu bóng ĐÃ MẠNH RỒI -> chỉ cần reset thời gian
        if (isStrong) {
            if (strongModeTimer != null) {
                strongModeTimer.stop();
                strongModeTimer.play(); // Chạy lại 10 giây
            }
            return; // Không cần áp dụng hiệu ứng nữa
        }

        // 2. Nếu bóng CHƯA MẠNH -> áp dụng hiệu ứng
        this.isStrong = true;

        // 3. Lưu lại trạng thái gốc (CHỈ LƯU KÍCH THƯỚC)
        this.originalWidth = getWidth();   // Kích thước logic
        this.originalHeight = getHeight(); // Kích thước logic

        // 4. Áp dụng hiệu ứng: Chậm hơn (Sửa lỗi)
        // Lấy tốc độ HIỆN TẠI (getDx()) và nhân nhỏ lại
        setDx(getDx() * STRONG_MODE_SPEED_MODIFIER);
        setDy(getDy() * STRONG_MODE_SPEED_MODIFIER);

        // 5. Áp dụng hiệu ứng: To ra (cả logic và hình ảnh)
        double newWidth = this.originalWidth * STRONG_MODE_SIZE_MODIFIER;
        double newHeight = this.originalHeight * STRONG_MODE_SIZE_MODIFIER;

        super.setWidth(newWidth);   // Cập nhật kích thước logic
        super.setHeight(newHeight);
        this.imageView.setFitWidth(newWidth);   // Cập nhật kích thước hình ảnh
        this.imageView.setFitHeight(newHeight);

        // 6. Bắt đầu hẹn giờ (như cũ)
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

        // --- SỬA LỖI KHÔI PHỤC HƯỚNG ---

        // 1. Lấy tốc độ HIỆN TẠI (đang bị chậm)
        double currentDx = getDx();
        double currentDy = getDy();

        // 2. Khôi phục tốc độ bình thường bằng cách chia cho hệ số giảm tốc
        // (Đây là phép toán ngược của: getDx() * STRONG_MODE_SPEED_MODIFIER)
        double restoredDx = currentDx / STRONG_MODE_SPEED_MODIFIER;
        double restoredDy = currentDy / STRONG_MODE_SPEED_MODIFIER;

        // 3. Đặt lại tốc độ (bây giờ đã trở lại bình thường và đúng hướng)
        setDx(restoredDx);
        setDy(restoredDy);

        // 4. Khôi phục kích thước gốc (Phần này đã đúng)
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
        // ... (phần tính toán dxDiff, dyDiff, overlapX, overlapY giữ nguyên) ...
        double ballCenterX = getX() + getWidth() / 2;
        double ballCenterY = getY() + getHeight() / 2;
        double bossCenterX = boss.getX() + boss.getWidth() / 2;
        double bossCenterY = boss.getY() + boss.getHeight() / 2;

        double dxDiff = ballCenterX - bossCenterX;
        double dyDiff = ballCenterY - bossCenterY;

        double overlapX = (getWidth() / 2 + boss.getWidth() / 2) - Math.abs(dxDiff);
        double overlapY = (getHeight() / 2 + boss.getHeight() / 2) - Math.abs(dyDiff);

        // So sánh độ chồng lấn
        if (overlapX < overlapY) {
            // --- VA CHẠM NGANG (TRÁI/PHẢI) ---

            // 1. Đảo ngược vận tốc X
            setDx(-getDx());

            // 2. ĐẨY BÓNG RA (Push-out) - LOGIC ĐÃ SỬA
            if (dxDiff > 0) {
                // Ball ở bên PHẢI tâm Boss -> Đẩy bóng sang PHẢI (ra xa)
                setX(boss.getX() + boss.getWidth() + 2);
            } else {
                // Ball ở bên TRÁI tâm Boss -> Đẩy bóng sang TRÁI (ra xa)
                setX(boss.getX() - getWidth() - 2);
            }

        } else {
            // --- VA CHẠM DỌC (TRÊN/DƯỚI) ---

            // 1. Đảo ngược vận tốc Y
            setDy(-getDy());

            // 2. ĐẨY BÓNG RA (Push-out) - LOGIC ĐÃ SỬA
            if (dyDiff > 0) {
                // Ball ở BÊN DƯỚI tâm Boss -> Đẩy bóng XUỐNG DƯỚI (ra xa)
                setY(boss.getY() + boss.getHeight() + 2);
            } else {
                // Ball ở BÊN TRÊN tâm Boss -> Đẩy bóng LÊN TRÊN (ra xa)
                setY(boss.getY() - getHeight() - 2);
            }
        }
    }


}
