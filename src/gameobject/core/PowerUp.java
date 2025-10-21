package gameobject.core;
import gameobject.dynamic.Paddle;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Lớp cha cho tất cả các PowerUp (item rơi ra khi phá gạch)
 */
public abstract class PowerUp extends MovableObject {
    protected Pane gameRoot;
    protected Paddle paddle;         // reference trực tiếp đến paddle
    protected boolean active = true;
    private AnimationTimer fallTimer;

    public PowerUp(Pane gameRoot, Paddle paddle, double x, double y, String imagePath) {
        super(x, y, 30, 30, new Image(PowerUp.class.getResourceAsStream(imagePath)));
        this.gameRoot = gameRoot;
        this.paddle = paddle;

        // Tạo ảnh item - GIỮ NGUYÊN logic từ code cũ
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        imageView = new ImageView(image);
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
        gameRoot.getChildren().add(imageView);

        startFalling();
    }

    /**
     * Bắt đầu cho item rơi xuống
     */
    private void startFalling() {
        fallTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!active) {
                    stop();
                    return;
                }

                // Di chuyển item - GIỮ NGUYÊN logic từ code cũ
                y += 2; // speedY = 2
                imageView.setLayoutY(y);

                // Nếu rơi quá màn hình thì xóa
                if (y > gameRoot.getHeight()) {
                    remove();
                    return;
                }

                // Kiểm tra va chạm với paddle
                if (checkCollision()) {
                    applyEffect(paddle);
                    remove();
                }
            }
        };
        fallTimer.start();
    }

    /**
     * Kiểm tra va chạm item với paddle
     */
    private boolean checkCollision() {
        return imageView.getBoundsInParent()
                .intersects(paddle.getImageView().getBoundsInParent());
    }

    /**
     * Áp dụng hiệu ứng khi paddle nhận item
     */
    protected abstract void applyEffect(Paddle paddle);

    /**
     * Xóa item khỏi màn hình
     */
    protected void remove() {
        active = false;
        fallTimer.stop();
        gameRoot.getChildren().remove(imageView);
    }

    /**
     * Có thể dùng để update logic thêm nếu cần
     */
    public void update() {
        // hiện tại không cần làm gì, AnimationTimer đã xử lý
    }

    @Override
    public void update(double deltaTime) {
        // Ghi đè từ MovableObject nhưng giữ nguyên logic cũ
        update();
    }
}