package application;

import javafx.animation.AnimationTimer; // ✅ IMPORT
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    // ✅ Thêm biến Game Loop
    private AnimationTimer gameLoop;

    @Override
    public void start(Stage primaryStage) {
        try {
            SceneManager sceneManager = SceneManager.getInstance();
            sceneManager.setPrimaryStage(primaryStage);

            primaryStage.setTitle(Config.GAME_TITLE);
            primaryStage.setResizable(false);

            sceneManager.showMainMenuScene();
            primaryStage.show();

            // ✅ BẮT ĐẦU GAME LOOP
            startGameLoop();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ✅ THÊM HÀM NÀY
     * Tạo và bắt đầu Vòng Lặp Game.
     * Nó sẽ chạy 60 lần/giây và gọi GameManager.update().
     */
    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (lastUpdate == 0) lastUpdate = now;
                    double deltaTime = (now - lastUpdate) / 1_000_000_000.0; // giây
                    lastUpdate = now;

                // QUAN TRỌNG: Giới hạn deltaTime.
                // Nếu game bị treo 1 giây, bóng không nên bay xuyên tường.
                if (deltaTime > 0.032) { // Giới hạn ở ~30 FPS
                    deltaTime = 0.032;
                }

                // SỬA LẠI: Truyền deltaTime vào GameManager
                GameManager.getInstance().update(deltaTime);
}
        };
        gameLoop.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
