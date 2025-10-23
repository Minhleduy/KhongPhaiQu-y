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
            new ArrayList<>(gameObjects).forEach(GameObject::update);

            if (isLevelComplete()) {
                LevelManager.getInstance().progressToNextStage();
            }
        }
        else if (currentState == GameState.BOSS_FIGHT) {
            // Cập nhật tất cả các đối tượng (boss, bóng, thanh đỡ)
            new ArrayList<>(gameObjects).forEach(GameObject::update);

            // Tìm quả bóng và kiểm tra va chạm với boss
            Ball ball = findBall();
            if (ball != null && boss != null && boss.isAlive()) {
                if (boss.checkBallCollision(ball)) {
                    boss.takeDamage(1); // Boss mất 1 máu
                    ball.reverseSpeedY(); // Bóng nảy ngược lại
                }
            }

            //Kiểm tra xem boss đã bị đánh bại chưa
            if (boss != null && !boss.isAlive()) {
                setCurrentState(GameState.GAME_OVER);
                SceneManager.getInstance().showWinScreen();
            }
        }
    }

    public void showWinScreen() {
        System.out.println("Winner");
        switchScene("WinScreen.fxml");
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

        addGameObject(new Paddle());
        addGameObject(new Ball());
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

    public GameState getCurrentState() {
        return currentState;
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
}
