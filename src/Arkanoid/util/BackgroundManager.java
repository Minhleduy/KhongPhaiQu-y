package Arkanoid.util;

import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Region;

import java.util.HashMap;
import java.util.Map;

public class BackgroundManager {

    // Dùng Map để lưu trữ các ảnh nền đã tải, tránh tải lại nhiều lần
    private Map<Integer, Image> backgroundImages;

    // Biến này sẽ giữ tham chiếu đến layout chính của game (do GameManager cung cấp)
    private Region gamePane;

    /**
     * Hàm khởi tạo
     * @param gamePane Layout chính của game (có thể là AnchorPane, StackPane...)
     */
    public BackgroundManager(Region gamePane) {
        this.gamePane = gamePane;
        this.backgroundImages = new HashMap<>();
        System.out.println("BackgroundManager: Đã khởi tạo. gamePane là: " + this.gamePane);
        preloadBackgrounds();
    }

    /**
     * Tải trước tất cả các ảnh nền vào bộ nhớ
     */
    private void preloadBackgrounds() {
        backgroundImages.put(1, new Image(getClass().getResourceAsStream("/images/background/Background1.png")));
        backgroundImages.put(2, new Image(getClass().getResourceAsStream("/images/background/Background2.png")));
        backgroundImages.put(3, new Image(getClass().getResourceAsStream("/images/background/Background3.png")));
        backgroundImages.put(4, new Image(getClass().getResourceAsStream("/images/background/Background4.png")));
        //ảnh nền cho Boss
        System.out.println("BackgroundManager: Đã tải " + backgroundImages.size() + " ảnh nền.");
        backgroundImages.put(5, new Image(getClass().getResourceAsStream("/images/npc/Boss.png")));
    }

    /**
     * Hàm public này sẽ được GameManager gọi để thay đổi ảnh nền.
     * @param level Màn chơi hiện tại (ví dụ: 1, 2, 3...)
     */
    public void setBackgroundForLevel(int level) {
        Image bgImage = backgroundImages.get(level);
        System.out.println("BackgroundManager: Được gọi setBackground. gamePane là: " + this.gamePane + ", ảnh là: " + bgImage);
        if (bgImage != null) {
            BackgroundImage backgroundImage = new BackgroundImage(
                    bgImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(1.0, 1.0, true, true, false, false)
            );

            gamePane.setBackground(new javafx.scene.layout.Background(backgroundImage));
            System.out.println("BackgroundManager: ĐÃ THỰC HIỆN SET BACKGROUND!");
        } else {
            System.err.println("Không tìm thấy ảnh nền cho Level " + level);
        }
    }
}
