package utils;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;

/**
 * Quản lý việc hiển thị và thay đổi ảnh nền cho một Pane.
 */
public class BackgroundManager {

    private final Pane gamePane; // Pane chính của game

    /**
     * Khởi tạo trình quản lý với một Pane cụ thể.
     * @param gamePane Pane sẽ được đặt ảnh nền.
     */
    public BackgroundManager(Pane gamePane) {
        this.gamePane = gamePane;
    }

    /**
     * Đặt ảnh nền mới cho Pane.
     * @param imagePath Đường dẫn đến file ảnh nền.
     */
    public void setBackground(String imagePath) {
        Image image = ImageLoader.loadImage(imagePath);
        if (image != null) {
            BackgroundImage backgroundImage = new BackgroundImage(
                    image,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(1.0, 1.0, true, true, false, false)
            );
            gamePane.setBackground(new Background(backgroundImage));
        } else {
            System.err.println("Không thể đặt ảnh nền vì không tải được ảnh: " + imagePath);
        }
    }
}
