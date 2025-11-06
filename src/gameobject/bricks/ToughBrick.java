package gameobject.bricks;

import application.Config;
import gameobject.core.Brick;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * Gạch "Cứng" - bây giờ cần 3 LẦN va chạm để vỡ.
 */
public class ToughBrick extends Brick {

    // 1. Cần 2 ảnh cho 2 trạng thái hỏng
    private Image imageState2; // Ảnh khi còn 2 máu
    private Image imageState1; // Ảnh khi còn 1 máu

    public ToughBrick(double x, double y, Pane gamePane) {

        // 2. Gọi hàm "super" với hitPoints = 3
        // CHÚ Ý: Bạn cần một ảnh mới cho gạch này (ảnh gốc 3 máu).
        // Tôi tạm dùng "/images/brick/Brick_3mau.png" làm ví dụ.
        super(x, y, Config.BRICK_WIDTH, Config.BRICK_HEIGHT, 3, "/images/brick/Brick6.png", gamePane);

        // 3. Tải trước 2 ảnh bị hư hỏng
        // (Bạn sẽ cần tạo file ảnh này, ví dụ: "Brick_2mau.png" và "Brick_1mau.png")
        try {
            // Ảnh cho trạng thái còn 2 máu
            imageState2 = new Image(getClass().getResourceAsStream("/images/brick/Brick5.png"));
            // Ảnh cho trạng thái còn 1 máu
            imageState1 = new Image(getClass().getResourceAsStream("/images/brick/Brick11.png"));
        } catch (Exception e) {
            System.err.println("Không thể tải ảnh cho gạch 3-hit!");
            imageState2 = null;
            imageState1 = null;
        }
    }

    /**
     * Ghi đè (Override) hàm hit() để thay đổi ảnh theo 3 cấp độ
     */
    @Override
    public void hit(Pane gamePane) {
        if (isDestroyed()) {
            return;
        }

        // Giảm máu
        this.hitPoints--;

        // Cập nhật hình ảnh dựa trên số máu còn lại
        if (this.hitPoints == 2) {
            // Còn 2 máu -> Chuyển sang ảnh hỏng (cấp 1)
            if (imageState2 != null) {
                this.imageView.setImage(imageState2);
            } else {
                this.imageView.setOpacity(0.8); // Hiệu ứng dự phòng
            }
        } else if (this.hitPoints == 1) {
            // Còn 1 máu -> Chuyển sang ảnh hỏng (cấp 2)
            if (imageState1 != null) {
                this.imageView.setImage(imageState1);
            } else {
                this.imageView.setOpacity(0.5); // Hiệu ứng dự phòng
            }
        } else if (this.hitPoints <= 0) {
            // Hết máu -> Phá hủy
            destroy(gamePane);
        }
    }
}
