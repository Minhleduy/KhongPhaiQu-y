package application;

import Arkanoid.ui.GameUIController;
import javafx.fxml.FXMLLoader;
import utils.BackgroundManager;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import Arkanoid.ui.StoryDialogController;

import java.io.IOException;
import java.util.Objects;


public class SceneManager {

    private static SceneManager instance;
    private Stage primaryStage;

    private Pane gamePane;
    private Parent gameUIRoot;
    private GameUIController gameUIController;
    private Parent storyDialogRoot;
    private StoryDialogController storyDialogController;
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

    /**
     * Hàm này dùng để chuyển các SCENE ĐƠN LẺ (thay thế toàn bộ màn hình)
     * Ví dụ: Menu, Game Over, Win Screen
     */
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
        try {
            gamePane = new Pane();
            gamePane.setPrefSize(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/GameUI.fxml"));
            gameUIRoot = loader.load();
            gameUIRoot.setMouseTransparent(true);
            gameUIController = loader.getController();

            FXMLLoader storyLoader = new FXMLLoader(getClass().getResource("/ui/StoryDialog.fxml"));
            storyDialogRoot = storyLoader.load();
            storyDialogController = storyLoader.getController();
            storyDialogRoot.setVisible(false);

            StackPane root = new StackPane();
            root.getChildren().add(gamePane);
            root.getChildren().add(gameUIRoot);
            root.getChildren().add(storyDialogRoot);

            Scene scene = new Scene(root, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
            primaryStage.setScene(scene);
            root.requestFocus();

        } catch (IOException e) {
            System.err.println("Không thể tải tệp FXML!");
            e.printStackTrace();
        }
    }


    public void showGameOverScene(int finalScore) {
        System.out.println("Chuyển sang màn hình Game Over. Điểm cuối cùng: " + finalScore);
        switchScene("GameOver.fxml");
    }

    public void showWinScreen() {
        System.out.println("Đang chuyển sang màn hình chiến thắng...");
        switchScene("WinScreen.fxml");
    }

    /**
     * Trả về Pane của lớp game (lớp dưới)
     * @return Pane (để GameManager thêm gạch, bóng, nền...)
     */
    public Pane getGamePane() {
        return gamePane;
    }

    /**
     * Trả về Controller của lớp HUD (lớp trên)
     * @return GameUIController (để GameManager gọi updateLives, updateLevel)
     */
    public GameUIController getGameUIController() {
        return gameUIController;
    }

    /**
     * Trả về Controller của lớp Dialog (lớp trên cùng)
     */
    public StoryDialogController getStoryDialogController() {
        return storyDialogController;
    }

    /**
     * Trả về Giao diện (Root Node) của lớp Dialog
     */
    public Parent getStoryDialogRoot() {
        return storyDialogRoot;
    }

}
