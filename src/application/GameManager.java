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
    //private final double deltaTime = 1.0 / 60.0;
    public enum GameState { MENU, PLAYING, PAUSED, GAME_OVER, LEVEL_TRANSITION, BOSS_FIGHT }
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
        if (instance == null) instance = new GameManager();
        return instance;
    }

    public void startGame() {
        this.score = 0;
        this.lives = Config.INITIAL_LIVES;
        this.currentState = GameState.PLAYING;
        sceneManager.showGameScene();
        this.gamePane = sceneManager.getGamePane();
        if (this.gamePane == null) {
            System.err.println("LỖI: gamePane là null!");
            return;
        }
        this.gameUIController = sceneManager.getGameUIController();
        this.backgroundManager = new BackgroundManager(this.gamePane);
        this.soundManager = new SoundManager();
        this.soundManager.playMusic();
        LevelManager.getInstance().loadLevel(1);
        this.backgroundManager.setBackgroundForLevel(1);
        if (gameUIController != null) {
            gameUIController.updateLevel(1);
            gameUIController.updateLives(this.lives);
        }
    }

    public void update(double deltaTime) {
        if (currentState == GameState.PLAYING || currentState == GameState.BOSS_FIGHT) {
            for (GameObject obj : new ArrayList<>(gameObjects)) {
                obj.update(deltaTime);
            }
            if (currentState == GameState.PLAYING && isLevelComplete()) {
                LevelManager.getInstance().progressToNextStage();
            }
            if (currentState == GameState.BOSS_FIGHT) {
                Ball ball = findBall();
                if (ball != null && boss != null && boss.isAlive()) {
                    if (boss.checkBallCollision(ball)) {
                        boss.takeDamage(1);

                        if (gameUIController != null) { // <-- THÊM DÒNG NÀY
                            gameUIController.updateBossHealth(boss.getHealth(), boss.getMaxHealth());
                        }

                        ball.reverseSpeedY();
                    }
                }
                if (boss != null && !boss.isAlive()) {
                    setCurrentState(GameState.GAME_OVER);
                    if (soundManager != null) soundManager.stopMusic();
                    sceneManager.showWinScreen();
                }
            }
        }
    }

    private Ball findBall() {
        for (GameObject obj : gameObjects) {
            if (obj instanceof Ball) return (Ball) obj;
        }
        return null;
    }

    public void loseLife() {
        // KIỂM TRA KHIÊN TRƯỚC
        if (gameobject.items.BlockItem.useShield()) {
            System.out.println("Khiên đã đỡ một mạng!");
            resetBall(); // Vẫn reset bóng nhưng không trừ mạng
            return; // Dừng hàm tại đây
        }

        this.lives--;
        if (gameUIController != null) {
            gameUIController.updateLives(this.lives);
        }
        if (this.lives <= 0) {
            this.currentState = GameState.GAME_OVER;
            if (soundManager != null) soundManager.stopMusic();
            sceneManager.showGameOverScene(this.score);
        } else {
            resetBall();
        }
    }

    private void resetBall() {
//        Ball ball = findBall();
//        if (ball != null) {
//            ball.setPosition(Config.SCREEN_WIDTH / 2.0, Config.SCREEN_HEIGHT / 2.0);
//            ball.resetVelocity();
//        }
        spawnNewBall();
    }

    private void spawnNewBall() {
        double ballStartX = Config.SCREEN_WIDTH / 2.0;
        double ballStartY = Config.SCREEN_HEIGHT / 2.0;

        Ball newBall = new Ball(
                this.gamePane,
                ballStartX,
                ballStartY,
                Config.SCREEN_WIDTH,
                Config.SCREEN_HEIGHT
        );
        addGameObject(newBall);
    }

    public void onBallFallen(Ball fallenBall) {
        fallenBall.removeGraphics();
        removeGameObject(fallenBall);
        long remainingBalls = getGameObjects().stream()
                .filter(obj -> obj instanceof Ball)
                .count();

        if (remainingBalls == 0) {
            loseLife();
        }
    }

    private boolean isLevelComplete() {
        return gameObjects.stream()
                .filter(obj -> obj instanceof Brick)
                .map(obj -> (Brick) obj)
                .noneMatch(Brick::isBreakable);
    }

    public void startBossFight() {
        setCurrentState(GameState.BOSS_FIGHT);

        if (gameUIController != null) { // <-- THÊM KHỐI LỆNH NÀY
            gameUIController.showBossUI();
        }

        clearGameObjects();
        this.boss = new Boss(this.gamePane, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);

        if (gameUIController != null) { // <-- THÊM CẬP NHẬT MÁU BAN ĐẦU
            gameUIController.updateBossHealth(this.boss.getHealth(), this.boss.getMaxHealth());
        }
        addGameObject(this.boss);

        double paddleStartX = (Config.SCREEN_WIDTH - Config.PADDLE_WIDTH) / 2.0;
        double paddleStartY = Config.PADDLE_Y_POSITION;
        double ballStartX = Config.SCREEN_WIDTH / 2.0;
        double ballStartY = Config.SCREEN_HEIGHT / 2.0;

        addGameObject(new Paddle(this.gamePane, paddleStartX, paddleStartY, Config.SCREEN_WIDTH));
        Ball bossBall = new Ball(this.gamePane, ballStartX, ballStartY, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
        addGameObject(bossBall);

        int bossLevelID = 5;
        if (gameUIController != null) gameUIController.updateLevel(bossLevelID);
        if (backgroundManager != null) backgroundManager.setBackgroundForLevel(bossLevelID);
    }
    public void addLife() {
        if (this.lives < Config.INITIAL_LIVES) { // Giả sử có giới hạn mạng tối đa
            this.lives++;
            if (gameUIController != null) {
                gameUIController.updateLives(this.lives);
            }
        }
    }

    public void addGameObject(GameObject obj) { this.gameObjects.add(obj); }
    public void removeGameObject(GameObject obj) { this.gameObjects.remove(obj); }
    public void clearGameObjects() { this.gameObjects.clear(); }
    public List<GameObject> getGameObjects() { return this.gameObjects; }
    public GameState getGameState() { return currentState; }
    public void setCurrentState(GameState state) { this.currentState = state; }
    public int getScore() { return score; }
    public void addScore(int points) { this.score += points; }
    public int getLives() { return lives; }
    public Pane getGamePane() { return this.gamePane; }
    public Boss getBoss() { return this.boss; }
    public GameUIController getGameUIController() { return gameUIController; }
}
