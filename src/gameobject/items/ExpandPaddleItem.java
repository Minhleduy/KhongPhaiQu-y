package gameobject.items;

import gameobject.core.PowerUp;
import gameobject.dynamic.Paddle;
import javafx.scene.layout.Pane;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

/**
 * Item giúp paddle dài ra tạm thời
 */
public class ExpandPaddleItem extends PowerUp {

    private static final double EXPAND_RATIO = 1.5;
    private static final double DURATION = 5;

    public ExpandPaddleItem(Pane gameRoot, Paddle paddle, double x, double y) {
        super(gameRoot, paddle, x, y, "/resources/images/items/Item3.png");
    }

    @Override
    protected void applyEffect(Paddle paddle) {
        double originalWidth = paddle.getWidth();

        paddle.setWidth(originalWidth * EXPAND_RATIO);

        PauseTransition pause = new PauseTransition(Duration.seconds(DURATION));
        pause.setOnFinished(e -> paddle.setWidth(originalWidth));
        pause.play();
    }
}
