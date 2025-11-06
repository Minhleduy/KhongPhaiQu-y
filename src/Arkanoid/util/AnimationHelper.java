package Arkanoid.util; // Hoặc Arkanoid.utils nếu bạn tạo package mới

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Lớp tiện ích chứa các hàm static để tạo hiệu ứng animation.
 */
public class AnimationHelper {

    /**
     * Áp dụng hiệu ứng mờ dần (Fade In) cho một đối tượng bất kỳ.
     * @param node Đối tượng cần áp dụng hiệu ứng (ví dụ: Button, Label, ImageView).
     * @param durationMillis Thời gian diễn ra hiệu ứng (tính bằng mili giây).
     */
    public static void applyFadeIn(Node node, double durationMillis) {
        // Thiết lập trạng thái ban đầu: hoàn toàn trong suốt
        node.setOpacity(0.0);

        // Tạo một hiệu ứng FadeTransition
        FadeTransition ft = new FadeTransition(Duration.millis(durationMillis), node);
        ft.setFromValue(0.0); // Mờ (trong suốt)
        ft.setToValue(1.0);   // Rõ (không trong suốt)
        ft.setCycleCount(1);  // Chạy 1 lần
        ft.setAutoReverse(false); // Không tự động đảo ngược

        // Bắt đầu chạy hiệu ứng
        ft.play();
    }
}
