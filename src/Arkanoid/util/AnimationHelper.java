package Arkanoid.util; // Hoặc Arkanoid.utils nếu bạn tạo package mới

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class AnimationHelper {

    public static void applyFadeIn(Node node, double durationMillis) {
        node.setOpacity(0.0);


        FadeTransition ft = new FadeTransition(Duration.millis(durationMillis), node);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);

        ft.play();
    }
}
