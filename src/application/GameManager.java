package application;

import javafx.scene.image.ImageView;
import Arkanoid.ui.GameUIController;
import utils.BackgroundManager;
import Arkanoid.util.SoundManager;
import gameobject.core.Brick;
import gameobject.core.GameObject;
import gameobject.dynamic.Ball;
import gameobject.dynamic.Boss;
import gameobject.dynamic.Paddle;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameManager {
    private static GameManager instance;
    private Boss boss;
    private Pane gamePane;
    public enum GameState { MENU, PLAYING, PAUSED, GAME_OVER, LEVEL_TRANSITION, BOSS_FIGHT }
    private GameState currentState;
    private int score;
    private int lives;
    private List<GameObject> gameObjects;
    private SceneManager sceneManager;
    private BackgroundManager backgroundManager;
    private SoundManager soundManager;
    private GameUIController gameUIController;
    public static boolean IS_GAME_PAUSED_FOR_STORY = false;

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
        if (IS_GAME_PAUSED_FOR_STORY) {
            return;
        }
        if (currentState == GameState.PLAYING || currentState == GameState.BOSS_FIGHT) {
            for (GameObject obj : new ArrayList<>(gameObjects)) {
                obj.update(deltaTime);
            }
            if (currentState == GameState.PLAYING && isLevelComplete()) {
                utils.SoundManager.getInstance().playSoundEffect("/sounds/levelup.mp3");
                LevelManager.getInstance().progressToNextStage();
            }
        }

        if (currentState == GameState.BOSS_FIGHT) {
            if (boss != null && !boss.isAlive()) {
                setCurrentState(GameState.GAME_OVER);
                if (soundManager != null) soundManager.stopMusic();
                sceneManager.showWinScreen();
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
        if (gameobject.items.BlockItem.useShield()) {
            System.out.println("Khiên đã đỡ một mạng!");
            resetBall();
            return;
        }

        this.lives--;
        utils.SoundManager.getInstance().playSoundEffect("/sounds/LoseLifeSound.mp3");
        if (gameUIController != null) {
            gameUIController.updateLives(this.lives);
        }
        if (this.lives <= 0) {
            this.currentState = GameState.GAME_OVER;
            if (soundManager != null) soundManager.stopMusic();
            sceneManager.showGameOverScene(this.score);
        }
    }

    private void resetBall() {
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

            if (currentState != GameState.GAME_OVER) {
                resetBall();
            }
        }
    }


    private boolean isLevelComplete() {
        List<Brick> allBricks = gameObjects.stream()
                .filter(obj -> obj instanceof Brick)
                .map(obj -> (Brick) obj)
                .collect(Collectors.toList());

        if (allBricks.isEmpty()) {
            return false;
        }
        return allBricks.stream()
                .noneMatch(brick -> brick.isBreakable() && !brick.isDestroyed());
    }


public void startBossFight() {
    setCurrentState(GameState.BOSS_FIGHT);

    clearGameObjects();

    if (gameUIController != null) {
        gameUIController.showBossUI();
    }

    this.boss = new Boss(this.gamePane, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
    addGameObject(this.boss);

    double paddleStartX = (Config.SCREEN_WIDTH - Config.PADDLE_WIDTH) / 2.0;
    double paddleStartY = Config.PADDLE_Y_POSITION;
    double ballStartX = Config.SCREEN_WIDTH / 2.0;
    double ballStartY = Config.SCREEN_HEIGHT / 2.0;

    addGameObject(new Paddle(this.gamePane, paddleStartX, paddleStartY, Config.SCREEN_WIDTH));

    Ball bossBall = new Ball(this.gamePane, ballStartX, ballStartY, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
    addGameObject(bossBall);


    int bossLevelID = 5;

    if (backgroundManager != null) {
        backgroundManager.setBackgroundForLevel(5);
    }

    if (gameUIController != null && boss != null) {
        gameUIController.updateBossHealth(boss.getHealth(), boss.getMaxHealth());
    }
}
    public void addLife() {
        if (this.lives < Config.INITIAL_LIVES) {
            this.lives++;
            if (gameUIController != null) {
                gameUIController.updateLives(this.lives);
            }
        }
    }
    public void clearGameObjects() {

        for (GameObject obj : gameObjects) {

            ImageView view = obj.getImageView();

            if (view != null && gamePane != null) {
                gamePane.getChildren().remove(view);
            }
        }

        this.gameObjects.clear();
    }

    public BackgroundManager getBackgroundManager() {
        return this.backgroundManager;
    }
    public void addGameObject(GameObject obj) { this.gameObjects.add(obj); }
    public void removeGameObject(GameObject obj) { this.gameObjects.remove(obj); }
    //public void clearGameObjects() { this.gameObjects.clear(); }
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
