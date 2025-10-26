package application;

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
    private Pane gameRoot;
    final double deltaTime = 1.0;

    public enum GameState {
        MENU, PLAYING, PAUSED, GAME_OVER, LEVEL_TRANSITION, BOSS_FIGHT
    }

    private GameState currentState;
    private int score;
    private int lives;
    private List<GameObject> gameObjects;

    private GameManager() {
        this.gameObjects = new ArrayList<>();
    }

    public static synchronized GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void startGame() {
        this.score = 0;
        this.lives = Config.INITIAL_LIVES;
        this.currentState = GameState.PLAYING;
        LevelManager.getInstance().loadLevel(1);
    }


    public void update() {
        if (currentState == GameState.PLAYING) {

            for (GameObject obj : new ArrayList<>(gameObjects)) {
                obj.update(deltaTime);
            }

            if (isLevelComplete()) {
                LevelManager.getInstance().progressToNextStage();
            }
        }
        else if (currentState == GameState.BOSS_FIGHT) {
            for (GameObject obj : new ArrayList<>(gameObjects)) {
                obj.update(deltaTime);
            }

            Ball ball = findBall();
            if (ball != null && boss != null && boss.isAlive()) {
                if (boss.checkBallCollision(ball)) {
                    boss.takeDamage(1); // Boss mất 1 máu
                    ball.reverseSpeedY(); // Bóng nảy ngược lại
                }
            }

            if (boss != null && !boss.isAlive()) {
                setCurrentState(GameState.GAME_OVER);
                SceneManager.getInstance().showWinScreen();
            }
        }
    }


    /**
     * Phương thức trợ giúp để tìm đối tượng Ball trong danh sách gameObjects.
     */
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
        if (this.lives <= 0) {
            this.currentState = GameState.GAME_OVER;
            SceneManager.getInstance().showGameOverScene(this.score);
        } else {
            Paddle paddleToReset = null;
            Ball ballToReset = null;
            for (GameObject obj : gameObjects) {
                if (obj instanceof Paddle) paddleToReset = (Paddle) obj;
                if (obj instanceof Ball) ballToReset = (Ball) obj;
            }

            if (paddleToReset != null && ballToReset != null) {
                double paddleNewX = (Config.SCREEN_WIDTH - Config.PADDLE_WIDTH) / 2.0;
                paddleToReset.setX(paddleNewX);
                double ballNewCenterX = paddleNewX + (Config.PADDLE_WIDTH / 2.0);
                double ballNewCenterY = Config.PADDLE_Y_POSITION - Config.BALL_RADIUS;
                ballToReset.setPosition(ballNewCenterX, ballNewCenterY);
                ballToReset.resetVelocity();
            }
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
        clearGameObjects();

        this.boss = new Boss(gameRoot, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
        addGameObject(this.boss);

        double paddleStartX = (Config.SCREEN_WIDTH - Config.PADDLE_WIDTH) / 2.0;
        double paddleStartY = Config.PADDLE_Y_POSITION;

        double ballStartX = Config.SCREEN_WIDTH / 2.0;
        double ballStartY = Config.SCREEN_HEIGHT / 2.0;

        addGameObject(new Paddle(gameRoot, paddleStartX, paddleStartY, Config.SCREEN_WIDTH));
        addGameObject(new Ball(gameRoot, ballStartX, ballStartY, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT));
    }

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
    }
    public int getLives() {
        return lives;
    }
    public void setGameRoot(Pane root) {
        this.gameRoot = root;
    }
    public Boss getBoss() {
        return this.boss;
    }
    public Pane getGameRoot() {
        return this.gameRoot;
    }
}
