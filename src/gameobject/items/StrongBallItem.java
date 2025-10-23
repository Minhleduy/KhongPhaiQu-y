package gameobject;

import gameobject.core.PowerUp;
import gameobject.dynamic.Paddle;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class StrongBallItem extends PowerUp {

    public StrongBallItem(double x, double y, Pane gamePane) {
        super(x, y, "/resources/images/items/Item1.png", gamePane);
    }

    @Override
    public void activate(Paddle paddle, Pane gamePane) {
        // Khi bắt được item, làm bóng to hơn
        for (var node : gamePane.getChildren()) {
            if (node instanceof javafx.scene.image.ImageView iv && "ball".equals(iv.getId())) {
                iv.setScaleX(1.5);
                iv.setScaleY(1.5);
            }
        }

        // Xóa item khỏi game
        gamePane.getChildren().remove(getImageView());
    }
}
