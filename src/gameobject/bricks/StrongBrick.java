package gameobject.bricks;

import gameobject.StrongBallItem;
import gameobject.core.Brick;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;


public class StrongBrick extends Brick {

    public StrongBrick(double x, double y, Pane gamePane) {
        // hitPoints = 1, chỉ dùng 1 ảnh duy nhất
        super(x, y, 40, 20, 1, "resources/images/brick/Brick3.png", gamePane);
    }

    @Override
    public void hit(Pane gamePane) {
        // Trừ máu (từ 1 xuống 0)
        this.hitPoints--;

        // Nếu hết máu
        if (this.hitPoints <= 0) {
            // Phá hủy viên gạch
            destroy(gamePane);

            // Chỉ cần dòng này là đủ. Item sẽ tự thêm vào màn hình và tự rơi.
            new StrongBallItem(getX(), getY(), gamePane);
        }
    }
}