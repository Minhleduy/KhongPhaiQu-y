package application;

import gameobject.core.Brick;
import gameobject.dynamic.Ball;
import gameobject.dynamic.Paddle;
import level.BrickMapLoader;
import java.util.List;
import javafx.scene.layout.Pane;

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
        Pane gameRoot = gm.getGamePane();

        gm.clearGameObjects();

        double paddleStartX = (Config.SCREEN_WIDTH - Config.PADDLE_WIDTH) / 2.0;
        double paddleStartY = Config.PADDLE_Y_POSITION;
        Paddle paddle = new Paddle(gameRoot, paddleStartX, paddleStartY, Config.SCREEN_WIDTH);
        gm.addGameObject(paddle);

        double ballStartX = Config.SCREEN_WIDTH / 2.0;
        double ballStartY = Config.SCREEN_HEIGHT / 2.0;
        gm.addGameObject(new Ball(gameRoot, ballStartX, ballStartY, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT));

        List<Brick> bricks = BrickMapLoader.load(levelNumber, gameRoot, paddle);
        bricks.forEach(gm::addGameObject);

        StoryManager.getInstance().showStoryForLevel(levelNumber);
    }

    /**
     * Chuyển sang giai đoạn tiếp theo của game.
     */
    public void progressToNextStage() {
        if (currentLevel == MAX_REGULAR_LEVELS) {
            System.out.println("Đã hoàn thành màn 4! Chuẩn bị đấu Boss...");
            GameManager.getInstance().startBossFight();
        }
        else if (currentLevel < MAX_REGULAR_LEVELS) {
            currentLevel++;
            loadLevel(currentLevel);
        }
    }

    public static void setInstance(LevelManager instance) {
        LevelManager.instance = instance;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }


}
