package application;

import Arkanoid.ui.GameUIController;
import Arkanoid.util.BackgroundManager;
import Arkanoid.util.SoundManager;
import gameobject.core.Brick;
import gameobject.core.GameObject;
import gameobject.dynamic.Ball;
import gameobject.dynamic.Boss;
import gameobject.dynamic.Paddle;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.List;

public class GameManager {

    private static GameManager instance;
    private Boss boss;
    private Pane gamePane;
    final double deltaTime = 1.0;

    public enum GameState {
        MENU, PLAYING, PAUSED, GAME_OVER, LEVEL_TRANSITION, BOSS_FIGHT
    }

    private GameState currentState;
    private int score;
    private int lives;
    private List<GameObject> gameObjects;

    private SceneManager sceneManager;
    private BackgroundManager backgroundManager;
    private SoundManager soundManager;
    private GameUIController gameUIController;

    private GameManager() {
        this.gameObjects = new ArrayList<>();
        this.sceneManager = SceneManager.getInstance();
    }

    public static synchronized GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    /**
     * Được gọi bởi MainMenuController khi nhấn nút "Bắt Đầu"
     */
    public void startGame() {
        this.score = 0;
        this.lives = Config.INITIAL_LIVES;
        this.currentState = GameState.PLAYING;

        sceneManager.showGameScene();

        this.gamePane = sceneManager.getGamePane();
        this.gameUIController = sceneManager.getGameUIController();
        System.out.println("GameManager: Đã lấy gamePane từ SceneManager: " + this.gamePane);

        this.backgroundManager = new BackgroundManager(this.gamePane);
        this.soundManager = new SoundManager();


        this.soundManager.playMusic();


        LevelManager.getInstance().loadLevel(1);

        this.backgroundManager.setBackgroundForLevel(1);

        this.gameUIController.updateLevel(1);
        this.gameUIController.updateLives(this.lives);

    }


    public void update() {
        if (currentState == GameState.PLAYING) {
            // ... (code update game)

            if (isLevelComplete()) {
                LevelManager.getInstance().progressToNextStage();
                // ✅ Cập nhật HUD khi qua màn
                // (Nên đặt trong LevelManager.progressToNextStage()
                //  nhưng ta thêm ở đây để đảm bảo)
                // int nextLevel = LevelManager.getInstance().getCurrentLevel() + 1;
                // gameUIController.updateLevel(nextLevel);
                // backgroundManager.setBackgroundForLevel(nextLevel);
            }
        }
        else if (currentState == GameState.BOSS_FIGHT) {
            // ... (code update boss)

            if (boss != null && !boss.isAlive()) {
                setCurrentState(GameState.GAME_OVER);

                // ✅ Dừng nhạc trước khi chuyển cảnh
                if (soundManager != null) {
                    soundManager.stopMusic();
                }
                sceneManager.showWinScreen();
            }
        }
    }

    private Ball findBall() {
        for (GameObject obj : gameObjects) {
            if (obj instanceof Ball) {
                return (Ball) obj;
            }
        }
        return null;
    }

    public void loseLife() {
        this.lives--;

        if (gameUIController != null) {
            gameUIController.updateLives(this.lives);
        }

        if (this.lives <= 0) {
            this.currentState = GameState.GAME_OVER;

            if (soundManager != null) {
                soundManager.stopMusic();
            }
            sceneManager.showGameOverScene(this.score);
        } else {
            // ... (code reset bóng, giữ nguyên)
        }
    }

    private boolean isLevelComplete() {
        // ... (giữ nguyên)
        return gameObjects.stream()
                .filter(obj -> obj instanceof Brick)
                .map(obj -> (Brick) obj)
                .noneMatch(Brick::isBreakable);
    }

    public void startBossFight() {
        setCurrentState(GameState.BOSS_FIGHT);
        clearGameObjects();

        // ✅ Sử dụng this.gamePane (lấy từ SceneManager)
        this.boss = new Boss(this.gamePane, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
        addGameObject(this.boss);

        double paddleStartX = (Config.SCREEN_WIDTH - Config.PADDLE_WIDTH) / 2.0;
        double paddleStartY = Config.PADDLE_Y_POSITION;
        double ballStartX = Config.SCREEN_WIDTH / 2.0;
        double ballStartY = Config.SCREEN_HEIGHT / 2.0;

        // ✅ Sử dụng this.gamePane
        addGameObject(new Paddle(this.gamePane, paddleStartX, paddleStartY, Config.SCREEN_WIDTH));
        addGameObject(new Ball(this.gamePane, ballStartX, ballStartY, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT));

        // ✅ Cập nhật HUD và Nền cho màn Boss
        // (Giả sử màn boss là level 5, hoặc ID đặc biệt)
        int bossLevelID = 5;
        if(gameUIController != null) {
            gameUIController.updateLevel(bossLevelID);
        }
        if(backgroundManager != null) {
            backgroundManager.setBackgroundForLevel(bossLevelID);
        }
    }

    // ... (Giữ nguyên các hàm add/remove/clear/get GameObjects)
    public void addGameObject(GameObject obj) {
        this.gameObjects.add(obj);
    }
    public void removeGameObject(GameObject obj) {
        this.gameObjects.remove(obj);
    }
    public void clearGameObjects() {
        this.gameObjects.clear();
    }
    public List<GameObject> getGameObjects() {
        return this.gameObjects;
    }

    // ... (Giữ nguyên các hàm Get/Set State, Score, Lives)
    public GameState getGameState() {
        return this.currentState;
    }
    public void setCurrentState(GameState state) {
        this.currentState = state;
    }
    public int getScore() {
        return score;
    }
    public void addScore(int points) {
        this.score += points;
        // (Không cần cập nhật UI vì bạn đã bỏ điểm số)
    }
    public int getLives() {
        return lives;
    }

    // ⛔ XÓA HÀM NÀY ĐI (Không cần nữa, gamePane được lấy từ SceneManager)
    // public void setGameRoot(Pane root) {
    //     this.gameRoot = root;
    // }

    public Boss getBoss() {
        return this.boss;
    }

    /**
     * ✅ SỬA HÀM NÀY
     * Trả về Pane của lớp game để các đối tượng (gạch, bóng) có thể thêm mình vào
     */
    public Pane getGamePane() { // Đổi tên từ getGameRoot
        return this.gamePane;
    }
}
