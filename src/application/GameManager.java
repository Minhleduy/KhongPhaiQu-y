package application;

import javafx.scene.image.ImageView;
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
        // Khối 1: Cập nhật TẤT CẢ đối tượng
        if (currentState == GameState.PLAYING || currentState == GameState.BOSS_FIGHT) {
            for (GameObject obj : new ArrayList<>(gameObjects)) {
                obj.update(deltaTime); // <-- Ball.update() sẽ tự lo toàn bộ va chạm
            }
            if (currentState == GameState.PLAYING && isLevelComplete()) {
                LevelManager.getInstance().progressToNextStage();
            }
        }

        // Khối 2: CHỈ kiểm tra điều kiện thắng
        if (currentState == GameState.BOSS_FIGHT) {
            // Logic va chạm đã được chuyển vào Ball.java

            // CHỈ GIỮ LẠI logic kiểm tra chiến thắng
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

        // 1. Xóa quả bóng bị rơi (Giữ nguyên)
        fallenBall.removeGraphics();
        removeGameObject(fallenBall);

        // 2. Đếm bóng còn lại (Giữ nguyên)
        long remainingBalls = getGameObjects().stream()
                .filter(obj -> obj instanceof Ball)
                .count();

        // 3. Logic mới
        if (remainingBalls == 0) {
            // Không còn quả bóng nào!

            // 3a. Gọi logic mất mạng (có thể được khiên đỡ)
            loseLife();

            // 3b. Hồi sinh bóng nếu game chưa kết thúc
            // Kiểm tra: Nếu game CHƯA KẾT THÚC (tức là còn mạng, hoặc khiên vừa đỡ)
            // thì phải hồi sinh bóng mới.
            if (currentState != GameState.GAME_OVER) {
                resetBall(); // (Hàm resetBall của bạn sẽ gọi spawnNewBall)
            }
        }
        // (Nếu remainingBalls > 0, không làm gì cả, đúng như bạn muốn)
    }


    private boolean isLevelComplete() {
        return gameObjects.stream()
                .filter(obj -> obj instanceof Brick)
                .map(obj -> (Brick) obj)
                .noneMatch(brick -> brick.isBreakable() && !brick.isDestroyed());
    }

public void startBossFight() {
    setCurrentState(GameState.BOSS_FIGHT);

    // 1. Dọn dẹp Level 4 (Đã sửa)
    clearGameObjects();

    // 2. Cập nhật Giao diện (UI) cho màn Boss
    if (gameUIController != null) {
        gameUIController.showBossUI(); // <-- Hiển thị thanh máu Boss
    }

    // 3. Tạo Boss và thêm vào game
    this.boss = new Boss(this.gamePane, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
    addGameObject(this.boss);

    // 4. Lấy vị trí bắt đầu
    double paddleStartX = (Config.SCREEN_WIDTH - Config.PADDLE_WIDTH) / 2.0;
    double paddleStartY = Config.PADDLE_Y_POSITION;
    double ballStartX = Config.SCREEN_WIDTH / 2.0;
    double ballStartY = Config.SCREEN_HEIGHT / 2.0;

    // 5. (LỖI CÓ THỂ Ở ĐÂY) Tạo và thêm PADDLE MỚI
    // Hãy đảm bảo bạn CÓ dòng code này
    addGameObject(new Paddle(this.gamePane, paddleStartX, paddleStartY, Config.SCREEN_WIDTH));

    // 6. (LỖI CÓ THỂ Ở ĐÂY) Tạo và thêm BALL MỚI
    // Hãy đảm bảo bạn CÓ các dòng code này
    Ball bossBall = new Ball(this.gamePane, ballStartX, ballStartY, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
    addGameObject(bossBall);

//    int bossLevelID = 5; // (ID cho hình nền boss)
    if (backgroundManager != null) {
        backgroundManager.setBackgroundForLevel(5);
    }

    // Cập nhật thanh máu Boss lần đầu
    if (gameUIController != null && boss != null) {
        gameUIController.updateBossHealth(boss.getHealth(), boss.getMaxHealth());
    }
}
    public void addLife() {
        if (this.lives < Config.INITIAL_LIVES) { // Giả sử có giới hạn mạng tối đa
            this.lives++;
            if (gameUIController != null) {
                gameUIController.updateLives(this.lives);
            }
        }
    }
    public void clearGameObjects() {

        // 1. Lặp qua danh sách logic HIỆN TẠI (trước khi xóa)
        for (GameObject obj : gameObjects) {

            // 2. Lấy hình ảnh (ImageView) của từng đối tượng
            // (Chúng ta đã chuẩn hóa việc này ở các bước sửa lỗi trước)
            ImageView view = obj.getImageView();

            // 3. Yêu cầu gamePane xóa hình ảnh đó khỏi màn chơi
            if (view != null && gamePane != null) {
                gamePane.getChildren().remove(view);
            }
        }

        // 4. Sau khi đã xóa hết hình ảnh, BÂY GIỜ mới xóa danh sách logic
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
