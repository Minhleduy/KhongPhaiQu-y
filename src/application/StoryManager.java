package application;

import java.util.HashMap;
import java.util.Map;

/**
 * Lớp Singleton để quản lý các yếu tố cốt truyện, như hội thoại hoặc cắt cảnh.
 */
public class StoryManager {

    private static StoryManager instance;
    private final Map<Integer, String> levelStartDialogues;

    private StoryManager() {
        levelStartDialogues = new HashMap<>();
        // Tải trước một số đoạn hội thoại ví dụ
        levelStartDialogues.put(1, "Màn 1: Sự khởi đầu!");
        levelStartDialogues.put(2, "Chúng đang mạnh hơn!");
        levelStartDialogues.put(4, "Thử thách cuối cùng!");
    }

    public static synchronized StoryManager getInstance() {
        if (instance == null) {
            instance = new StoryManager();
        }
        return instance;
    }

    public void showStoryForLevel(int levelNumber) {
        String dialogue = levelStartDialogues.get(levelNumber);
        if (dialogue != null) {
            // Trong một game thực tế, bạn sẽ hiển thị điều này trong một thành phần giao diện người dùng.
            // in ra console.
            System.out.println("CỐT TRUYỆN: " + dialogue);
        }
    }
}
