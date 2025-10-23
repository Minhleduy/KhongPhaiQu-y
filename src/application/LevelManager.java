package application;

import gameobject.core.Brick;
import gameobject.dynamic.Ball;
import gameobject.dynamic.Paddle;
import level.BrickMapLoader;
import java.util.List;

/**
 * Lớp Singleton xử lý việc tải và chuyển tiếp các màn chơi hoặc giai đoạn game.
 */
public class LevelManager {

    private static LevelManager instance;
    private int currentLevel = 1;
    private static final int MAX_REGULAR_LEVELS = 4;

    private LevelManager() {}

    public static synchronized LevelManager getInstance() {
        if (instance == null) {
            instance = new LevelManager();
        }
        return instance;
    }

    /**
     * Tải một màn chơi thông thường với các viên gạch.
     */
    public void loadLevel(int levelNumber) {
        this.currentLevel = levelNumber;
        GameManager gm = GameManager.getInstance();

        // Xóa các đối tượng của màn chơi cũ
        gm.clearGameObjects();

        List<Brick> bricks = BrickMapLoader.load(levelNumber);
        bricks.forEach(gm::addGameObject);
        gm.addGameObject(new Paddle());
        gm.addGameObject(new Ball());

        // Cốt truyện
        StoryManager.getInstance().showStoryForLevel(levelNumber);
    }

    /**
     * Chuyển sang giai đoạn tiếp theo của game.
     */
    public void progressToNextStage() {
        if (currentLevel == MAX_REGULAR_LEVELS) {
            System.out.println("Đã hoàn thành màn 4! Chuẩn bị đấu Boss...");
            // Ủy quyền cho GameManager để bắt đầu trận đấu boss
            GameManager.getInstance().startBossFight();
        }
        else if (currentLevel < MAX_REGULAR_LEVELS) {
            currentLevel++;
            loadLevel(currentLevel);
        }
    }
}
