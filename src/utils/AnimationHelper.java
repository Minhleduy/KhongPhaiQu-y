package utils;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Lớp tiện ích chứa các phương thức tĩnh để tạo các hoạt ảnh phổ biến.
 */
public class AnimationHelper {

    /**
     * Tạo hiệu ứng mờ dần (hiện ra) cho một đối tượng.
     * @param node Đối tượng cần áp dụng hiệu ứng (ví dụ: Button, Label, Image)
     * @param durationMillis Thời gian hiệu ứng (tính bằng mili giây)
     */
    public static void fadeIn(Node node, int durationMillis) {
        node.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(durationMillis), node);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    /**
     * Tạo hiệu ứng mờ dần (biến mất) cho một đối tượng.
     * @param node Đối tượng cần áp dụng hiệu ứng
     * @param durationMillis Thời gian hiệu ứng
     */
    public static void fadeOut(Node node, int durationMillis) {
        FadeTransition ft = new FadeTransition(Duration.millis(durationMillis), node);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.play();
    }

    /**
     * Tạo hiệu ứng nảy lên (phóng to rồi thu nhỏ lại) cho một đối tượng.
     * Rất hữu ích khi người chơi nhấn nút hoặc ăn vật phẩm.
     * @param node Đối tượng cần áp dụng hiệu ứng
     */
    public static void bounce(Node node) {
        ScaleTransition st = new ScaleTransition(Duration.millis(150), node);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.2);
        st.setToY(1.2);
        st.setAutoReverse(true); // Tự động đảo ngược hiệu ứng
        st.setCycleCount(2);     // Chạy 2 lần (phóng to -> thu nhỏ)
        st.play();
    }
}
