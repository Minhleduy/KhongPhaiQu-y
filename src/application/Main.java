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
            @Override
            public void handle(long now) {
                // Gọi hàm update của GameManager
                GameManager.getInstance().update();
            }
        };
        // Bắt đầu vòng lặp
        gameLoop.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
