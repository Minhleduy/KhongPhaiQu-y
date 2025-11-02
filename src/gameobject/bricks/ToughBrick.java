package gameobject.bricks;

import application.Config;
import gameobject.core.Brick;
import javafx.scene.image.Image; // <-- Nhớ import
import javafx.scene.layout.Pane;

/**
 * Gạch "Cứng" - cần 2 lần va chạm để vỡ.
 */
public class ToughBrick extends Brick {

    // 1. Chỉ cần 1 ảnh cho trạng thái bị hỏng (còn 1 máu)
    private Image imageState1; // Ảnh khi còn 1 máu

    public ToughBrick(double x, double y, Pane gamePane) {

        // 2. Gọi hàm "super" với hitPoints = 2
        // CHÚ Ý: Bạn cần một ảnh mới cho gạch này.
        // Tôi tạm dùng "/images/brick/Brick_2mau.png" làm ví dụ.
        super(x, y, Config.BRICK_WIDTH, Config.BRICK_HEIGHT, 2, "/images/brick/Brick6.png", gamePane);

        // 3. Tải trước ảnh bị hư hỏng (cho 1 máu)
        // (Bạn sẽ cần tạo file ảnh này, ví dụ: "Brick_1mau.png")
        try {
            imageState1 = new Image(getClass().getResourceAsStream("/images/brick/Brick11.png"));
        } catch (Exception e) {
            System.err.println("Không thể tải ảnh cho gạch 2-hit!");
            imageState1 = null;
        }
    }

    /**
     * Ghi đè (Override) hàm hit() để thay đổi ảnh
     */
    @Override
    public void hit(Pane gamePane) {
        if (isDestroyed()) {
            return;
        }

        // Giảm máu
        this.hitPoints--;

        // Cập nhật hình ảnh dựa trên số máu còn lại
        if (this.hitPoints == 1) {
            // Còn 1 máu -> Chuyển sang ảnh hỏng
            if (imageState1 != null) {
                this.imageView.setImage(imageState1);
            } else {
                this.imageView.setOpacity(0.5); // Hiệu ứng dự phòng nếu không có ảnh
            }
        } else if (this.hitPoints <= 0) {
            // Hết máu -> Phá hủy
            destroy(gamePane);
        }
    }
}
