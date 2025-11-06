package utils;

import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Map;

/**
 * Lớp tiện ích để tải và lưu trữ (cache) hình ảnh.
 * Giúp tránh việc tải lại các file ảnh giống nhau, tối ưu hóa bộ nhớ và hiệu suất.
 */
public class ImageLoader {

    // Kho chứa các ảnh đã được tải, với key là đường dẫn và value là đối tượng Image
    private static final Map<String, Image> imageCache = new HashMap<>();

    /**
     * Lấy một đối tượng Image từ đường dẫn được cung cấp.
     * Nếu ảnh đã được tải trước đó, nó sẽ được lấy từ cache.
     * Nếu chưa, ảnh sẽ được tải mới và thêm vào cache.
     * @param path Đường dẫn đến file ảnh (ví dụ: "/images/items/heart.png")
     * @return Đối tượng Image, hoặc null nếu không tìm thấy file.
     */
    public static Image loadImage(String path) {
        // Kiểm tra xem ảnh đã có trong cache chưa
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
            return null; // Trả về null nếu có lỗi
        }
    }

    /**
     * Xóa tất cả các ảnh đã được lưu trong cache.
     * Hữu ích khi chuyển từ màn chơi này sang màn chơi khác với bộ tài nguyên hoàn toàn mới.
     */
    public static void clearCache() {
        imageCache.clear();
        System.out.println("Đã xóa cache hình ảnh.");
    }
}
