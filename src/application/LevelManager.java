package application;

import Arkanoid.ui.GameUIController;
import utils.BackgroundManager;
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

        if (gm.getGameUIController() != null) {
            gm.getGameUIController().updateLevel(levelNumber);
        }
        if (gm.getBackgroundManager() != null) {
            gm.getBackgroundManager().setBackgroundForLevel(levelNumber);
        }

        String story = StoryManager.getInstance().getStoryForLevel(levelNumber);
        if (story != null) {
            // 1. Khóa game
            GameManager.IS_GAME_PAUSED_FOR_STORY = true;

            // 2. Hiển thị dialog
            SceneManager sm = SceneManager.getInstance();

            sm.getStoryDialogController().showDialogue(story);

            sm.getStoryDialogRoot().setVisible(true);
            sm.getStoryDialogRoot().setMouseTransparent(false);
        }
    }

    /**
     * Chuyển sang giai đoạn tiếp theo của game.
     */
    public void progressToNextStage() {
        if (currentLevel == MAX_REGULAR_LEVELS) {
            System.out.println("Đã hoàn thành màn 4! Chuẩn bị đấu Boss...");
            String story = StoryManager.getInstance().getStoryForLevel(5); // Key 5 = Boss

            GameManager.IS_GAME_PAUSED_FOR_STORY = true;

            // 3. Hiển thị dialog
            SceneManager sm = SceneManager.getInstance();

            sm.getStoryDialogController().showDialogue(story); // Bỏ "null" đi

            sm.getStoryDialogRoot().setVisible(true);
            sm.getStoryDialogRoot().setMouseTransparent(false);

            GameManager.getInstance().startBossFight();

        } else if (currentLevel < MAX_REGULAR_LEVELS) {
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
