package utils;

import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Map;

public class ImageLoader {

    private static final Map<String, Image> imageCache = new HashMap<>();
    public static Image loadImage(String path) {
        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        }

        try {
            Image image = new Image(ImageLoader.class.getResourceAsStream(path));
            imageCache.put(path, image);
            return image;
        } catch (Exception e) {
            System.err.println("Không thể tải ảnh: " + path);
            e.printStackTrace();
            return null;
        }
    }

    public static void clearCache() {
        imageCache.clear();
        System.out.println("Đã xóa cache hình ảnh.");
    }
}
