package gameobject.items;

import application.GameManager;
import gameobject.core.GameObject;
import gameobject.core.PowerUp;
import gameobject.dynamic.Ball;
import gameobject.dynamic.Paddle;
import javafx.scene.layout.Pane;

public class StrongBallItem extends PowerUp {

    public StrongBallItem(Pane gamePane, Paddle paddle, double x, double y) {

        super(gamePane, paddle, x, y, "/resources/images/items/Item1.png");
    }

    /**
     * SỬA LỖI 1: Đổi tên và chữ ký của phương thức.
     */
    @Override
    public void applyEffect(Paddle paddle) {
        // Tìm tất cả các đối tượng Ball trong game và kích hoạt chế độ "mạnh"
        for (GameObject obj : GameManager.getInstance().getGameObjects()) {
            if (obj instanceof Ball) {
                Ball ball = (Ball) obj;
                ball.activateStrongMode();
            }
        }

        if (this.gameRoot != null) {
            this.gameRoot.getChildren().remove(getImageView());
        }
    }
}
