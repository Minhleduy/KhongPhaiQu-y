package gameobject.dynamic;

import application.GameManager;
import gameobject.core.GameObject;
import gameobject.core.MovableObject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.util.List;

/**
 * Lớp định nghĩa viên đạn do Boss bắn ra.
 * Nó di chuyển thẳng xuống và kiểm tra va chạm với Paddle.
 */
public class BossProjectile extends MovableObject {

    private Pane gameRoot;
    private double sceneHeight;
    private static final double PROJECTILE_SPEED = 200.0; // Tốc độ đạn (pixel/giây)

    public BossProjectile(Pane gameRoot, double x, double y) {
        // Bạn sẽ cần một file ảnh cho viên đạn, ví dụ: /images/npc/boss_shot.png
        super(x, y, 120, 120, null);

        this.gameRoot = gameRoot;
        this.sceneHeight = gameRoot.getHeight(); // Lấy chiều cao từ Pane

        // Tải ảnh
        try {
            Image image = new Image(getClass().getResourceAsStream("/images/npc/boss_shot.png"));
            this.imageView = new ImageView(image);
            this.imageView.setFitWidth(this.width);
            this.imageView.setFitHeight(this.height);
            this.imageView.setLayoutX(x);
            this.imageView.setLayoutY(y);

            // Thêm ảnh vào màn chơi
            gameRoot.getChildren().add(this.imageView);

        } catch (Exception e) {
            System.err.println("Không thể tải ảnh đạn của Boss!");
        }

        // Đặt vận tốc
        setDy(PROJECTILE_SPEED); // Bay xuống
    }

    /**
     * Hàm update() sẽ được GameManager tự động gọi 60 lần/giây
     */
    @Override
    public void update(double deltaTime) {
        // 1. Di chuyển viên đạn
        super.update(deltaTime); // (Lấy từ MovableObject, đã bao gồm deltaTime)

        // 2. Cập nhật vị trí hình ảnh
        if (this.imageView != null) {
            this.imageView.setLayoutY(getY());
        }

        GameManager gm = GameManager.getInstance();
        if (gm == null) {
            remove();
            return;
        }

        // 3. Kiểm tra va chạm với Paddle
        List<GameObject> gameObjects = gm.getGameObjects();
        for (GameObject obj : gameObjects) {
            if (obj instanceof Paddle) {
                // Phát hiện va chạm với Paddle
                if (getBounds().intersects(obj.getBounds())) {
                    System.out.println("GAME: Paddle bị trúng đạn!");
                    gm.loseLife(); // Trừ mạng người chơi
                    remove();      // Xóa viên đạn
                    return;        // Dừng frame này
                }
                break; // Chỉ có 1 paddle, không cần tìm nữa
            }
        }

        // 4. Kiểm tra nếu bay ra khỏi màn hình
        if (getY() > this.sceneHeight) {
            remove(); // Tự hủy
        }
    }

    /**
     * Hàm dọn dẹp, xóa viên đạn khỏi logic và khỏi màn chơi
     */
    private void remove() {
        // Xóa khỏi danh sách logic của GameManager
        GameManager.getInstance().removeGameObject(this);

        // Xóa hình ảnh khỏi màn chơi
        if (gameRoot != null && this.imageView != null) {
            gameRoot.getChildren().remove(this.imageView);
        }
    }
}
