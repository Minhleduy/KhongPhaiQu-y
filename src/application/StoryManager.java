package application;

import java.util.HashMap;
import java.util.Map;

public class StoryManager {
    private static StoryManager instance;
    private final Map<Integer, String> levelStartDialogues;

    private StoryManager() {
        levelStartDialogues = new HashMap<>();
        // Tải trước nội dung cốt truyện
        levelStartDialogues.put(1, "Chào mừng! Hãy phá vỡ tất cả gạch để bắt đầu...");
        levelStartDialogues.put(2, "Làm tốt lắm! Nhưng chúng đang mạnh hơn!");
        levelStartDialogues.put(3, "Chỉ còn một chút nữa thôi...");
        levelStartDialogues.put(4, "Đây là thử thách cuối cùng trước khi gặp trùm!");
        levelStartDialogues.put(5, "CẨN THẬN! BOSS ĐÃ XUẤT HIỆN!");
    }

    public static synchronized StoryManager getInstance() {
        if (instance == null) {
            instance = new StoryManager();
        }
        return instance;
    }

    public String getStoryForLevel(int levelNumber) {
        return levelStartDialogues.get(levelNumber);
    }
}
