package gameobject.dynamic;

import application.GameManager; // ✅ ĐÃ THÊM
import gameobject.core.Brick;
import gameobject.core.GameObject; // ✅ ĐÃ THÊM
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import application.Config;
import gameobject.core.MovableObject;
import javafx.animation.PauseTransition;

import java.util.List;
import java.util.stream.Collectors; // ✅ ĐÃ THÊM

public class Ball extends MovableObject {
    private ImageView imageView;
    private final double BALL_SPEED = 5; // Tốc độ cơ bản
    // private double speedX; // Đã có dx trong MovableObject
    // private double speedY; // Đã có dy trong MovableObject
    private boolean isStrong = false;
    private PauseTransition strongModeTimer;
    private double sceneWidth, sceneHeight;
    // private Timeline timeline; // ⛔ LỖI 2: Đã xóa vòng lặp Timeline nội bộ

    public Ball(Pane gameRoot, double startX, double startY, double sceneWidth, double sceneHeight) {

        super(startX, startY, 0, 0, null);

        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;

        // Load ảnh
        Image image = new Image(getClass().getResourceAsStream("/images/ball/Ball1.png"));
        imageView = new ImageView(image);

        // Cập nhật width, height từ ảnh thực tế
        setWidth(image.getWidth());
        setHeight(image.getHeight());

        // Set vận tốc ban đầu (dùng dx, dy từ MovableObject)
        resetVelocity(); // Gọi hàm reset vận tốc

        imageView.setLayoutX(startX);
        imageView.setLayoutY(startY);
        gameRoot.getChildren().add(imageView);

        // startMoving(); // ⛔ LỖI 2: Đã xóa, GameManager sẽ lo việc update
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

    // SỬA: Dùng getter/setter của dx/dy từ MovableObject
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

    // --- Di chuyển bóng ---
    // private void startMoving() { ... } // ⛔ LỖI 2: Đã xóa

    public void reverseSpeedY() {
        setDy(-getDy());
    }
    public void reverseSpeedX() {
        setDx(-getDx());
    }

    // ✅✅✅ SỬA LỖI 1: VIẾT LẠI HOÀN TOÀN HÀM UPDATE() ✅✅✅
    @Override
    public void update(double deltaTime) {
        // 1. Di chuyển bóng (dùng logic của MovableObject)
        super.update(deltaTime);

        // 2. Lấy danh sách tất cả các đối tượng từ GameManager
        GameManager gm = GameManager.getInstance();
        if (gm == null) return; // Tránh lỗi nếu GameManager chưa sẵn sàng

        List<GameObject> gameObjects = gm.getGameObjects();

        // 3. Kiểm tra va chạm với tường
        checkWallCollisions();

        // 4. Kiểm tra va chạm với Thanh đỡ (Paddle)
        for (GameObject obj : gameObjects) {
            if (obj instanceof Paddle) {
                checkPaddleCollision((Paddle) obj);
                break; // Chỉ có 1 paddle
            }
        }

        // 5. Kiểm tra va chạm với Gạch (Bricks)
        // Lọc ra danh sách gạch còn sống
        List<Brick> bricks = gameObjects.stream()
                .filter(obj -> obj instanceof Brick)
                .map(obj -> (Brick) obj)
                .filter(brick -> !brick.isDestroyed()) // Chỉ kiểm tra gạch chưa vỡ
                .collect(Collectors.toList());

        // Truyền gamePane từ GameManager
        checkBrickCollision(bricks, gm.getGamePane());
    }

    /**
     * Hàm mới: Kiểm tra va chạm với 4 cạnh màn hình
     */
    private void checkWallCollisions() {
        // Va chạm biên trái / phải
        if (getX() <= 0 || getX() + getWidth() >= sceneWidth) {
            setDx(-getDx());
            // Đảm bảo bóng không bị kẹt trong tường
            if (getX() <= 0) setX(0);
            if (getX() + getWidth() >= sceneWidth) setX(sceneWidth - getWidth());
        }

        // Va chạm biên trên
        if (getY() <= 0) {
            setDy(-getDy());
            setY(0);
        }

        // Nếu rơi ra ngoài màn hình (biên dưới)
        if (getY() > sceneHeight) {
            GameManager.getInstance().loseLife(); // Báo cho GameManager biết
            // Không cần resetBall() ở đây, GameManager sẽ lo việc đó
        }
    }

    // --- Va chạm với paddle ---
    public void checkPaddleCollision(Paddle paddle) {
        if (getBounds().intersects(paddle.getBounds())) {
            // Đã va chạm

            // 1. Đảo chiều Y (luôn bật lên)
            setDy(-Math.abs(getDy()));

            // 2. Tính toán góc bật (lệch nhiều hay ít)
            // Lấy vị trí tâm bóng - tâm thanh đỡ
            double hitPos = (getX() + getWidth() / 2) - (paddle.getX() + paddle.getWidth() / 2);

            // Tính toán dx mới, hitPos càng lớn (càng xa tâm) thì bóng càng lệch
            // Giá trị 0.1 là "độ nhạy", có thể cần điều chỉnh
            setDx(hitPos * 0.1);
        }
    }

    // --- Va chạm với gạch ---
    public void checkBrickCollision(List<Brick> bricks, Pane gameRoot) {
        for (Brick brick : bricks) {
            // Dùng getBounds() thay vì getImageView().getBoundsInParent()
            if (getBounds().intersects(brick.getBounds())) {

                if (!brick.isUnbreakable()) {
                    // Nếu là bóng mạnh, phá gạch ngay
                    if (isStrong) {
                        brick.destroy(true); // Phá gạch và tạo item
                    } else {
                        brick.hit(gameRoot); // Gạch bị mất máu
                        bounceFromBrick(brick); // Bóng nảy lại
                    }
                } else {
                    // Nếu là gạch không thể phá vỡ
                    bounceFromBrick(brick); // Chỉ nảy lại
                }

                break; // Chỉ xử lý 1 viên gạch mỗi frame
            }
        }
    }

    // Hàm này tính toán hướng nảy (trên/dưới hay trái/phải)
    private void bounceFromBrick(Brick brick) {
        // ... (Logic này có vẻ ổn, giữ nguyên)
        double ballCenterX = getX() + getWidth() / 2;
        double ballCenterY = getY() + getHeight() / 2;
        double brickCenterX = brick.getX() + brick.getWidth() / 2;
        double brickCenterY = brick.getY() + brick.getHeight() / 2;

        double dxDiff = ballCenterX - brickCenterX;
        double dyDiff = ballCenterY - brickCenterY;

        // Tính toán phần giao nhau
        double overlapX = (getWidth() / 2 + brick.getWidth() / 2) - Math.abs(dxDiff);
        double overlapY = (getHeight() / 2 + brick.getHeight() / 2) - Math.abs(dyDiff);

        // Nảy theo chiều có phần giao nhau ít hơn
        if (overlapX < overlapY) {
            setDx(getDx() > 0 ? -Math.abs(getDx()) : Math.abs(getDx())); // Đảo chiều x
        } else {
            setDy(getDy() > 0 ? -Math.abs(getDy()) : Math.abs(getDy())); // Đảo chiều y
        }
    }

    // ⛔ Đã xóa hàm resetBall() vì GameManager sẽ xử lý

    public void setPosition(double x, double y) {
        setX(x);
        setY(y);
    }

    public void resetVelocity() {
        setDx(BALL_SPEED);
        setDy(-BALL_SPEED); // Bay lên trên
    }

    // ... (Các hàm StrongMode giữ nguyên)
    public void activateStrongMode() {
        // ...
    }
    private void deactivateStrongMode() {
        // ...
    }
    public boolean isStrong() {
        return isStrong;
    }

    // ✅ THÊM HÀM NÀY (Rất quan trọng cho va chạm)
    // Giúp lấy ra hình chữ nhật va chạm của bóng
    public javafx.geometry.Bounds getBounds() {
        return imageView.getBoundsInParent();
    }
}
