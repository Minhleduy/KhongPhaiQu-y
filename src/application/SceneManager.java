package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class SceneManager {

    private static SceneManager instance;
    private Stage primaryStage;

    private SceneManager() {}

    public static synchronized SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void switchScene(String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ui/" + fxmlFile)));
            Scene scene = new Scene(root, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
            primaryStage.setScene(scene);
            root.requestFocus();
        } catch (IOException e) {
            System.err.println("Không thể tải tệp FXML: " + fxmlFile);
            e.printStackTrace();
        }
    }

    public void showMainMenuScene() {
        switchScene("MainMenu.fxml");
    }

    public void showGameScene() {
        switchScene("GameUI.fxml");
    }

    public void showGameOverScene(int finalScore) {
        System.out.println("Chuyển sang màn hình Game Over. Điểm cuối cùng: " + finalScore);
        switchScene("GameOver.fxml");
    }

    public void showWinScreen() {
        System.out.println("Đang chuyển sang màn hình chiến thắng...");
        switchScene("WinScreen.fxml");
    }
}
