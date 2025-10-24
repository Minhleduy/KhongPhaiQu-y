package gameobject.items;

import application.GameManager; // <-- Import GameManager
import gameobject.core.GameObject;
import gameobject.core.PowerUp;
import gameobject.dynamic.Ball; // <-- Import Ball
import gameobject.dynamic.Paddle;
import javafx.scene.layout.Pane;

public class StrongBallItem extends PowerUp {

    public StrongBallItem(double x, double y, Pane gamePane) {
        super(x, y, "/resources/images/items/Item1.png", gamePane);
    }

    @Override
    public void activate(Paddle paddle, Pane gamePane) {
        for (GameObject obj : GameManager.getInstance().getGameObjects()) {
            if (obj instanceof Ball) {
                Ball ball = (Ball) obj;
                // Ra lệnh cho quả bóng tự kích hoạt chế độ mạnh
                ball.activateStrongMode();
            }
        }

        // Kích hoạt âm thanh khi ăn vật phẩm (ví dụ)
        // SoundManager.getInstance().playSoundEffect("/sounds/powerup.wav");

        gamePane.getChildren().remove(getImageView());
    }
}
