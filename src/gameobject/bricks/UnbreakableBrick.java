package gameobject.bricks;

import gameobject.core.Brick;
import javafx.scene.layout.Pane;
import application.Config;

/**
 * Gạch không thể phá hủy — bóng chạm vào sẽ bị bật lại.
 */
public class UnbreakableBrick extends Brick {

    public UnbreakableBrick(double x, double y, Pane gamePane) {
        // hitPoints = -1 để đánh dấu là "không phá hủy"
        super(x, y, Config.BRICK_WIDTH, Config.BRICK_HEIGHT, -1, "/item/brick/Brick1.png", gamePane);
    }

    @Override
    public void hit(Pane gamePane) {
        // Không làm gì cả — gạch không bị phá hủy.
        // Có thể thêm hiệu ứng nhỏ để người chơi biết là chạm vào.
        getImageView().setOpacity(0.6);

        javafx.animation.PauseTransition flash = new javafx.animation.PauseTransition(
                javafx.util.Duration.millis(100));
        flash.setOnFinished(e -> getImageView().setOpacity(1.0));
        flash.play();
    }

    @Override
    public boolean isUnbreakable() { //ham dinh danh loai gach (da them vao trong Brick)
        return true;
    }
    public boolean isBreakable() {
        // Gạch không thể phá hủy sẽ luôn trả về false
        return false;
    }
}
