package application;

import Arkanoid.ui.GameUIController; // ✅ IMPORT CONTROLLER CỦA BẠN
import javafx.fxml.FXMLLoader;
import utils.BackgroundManager;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane; // ✅ IMPORT PANE
import javafx.scene.layout.StackPane; // ✅ IMPORT STACKPANE
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class SceneManager {

    private static SceneManager instance;
    private Stage primaryStage;

    // ✅ THÊM CÁC BIẾN ĐỂ LƯU CÁC LỚP GAME
    private Pane gamePane; // Lớp dưới (chứa nền, bóng, gạch)
    private Parent gameUIRoot; // Lớp trên (HUD của bạn)
    private GameUIController gameUIController; // Controller của HUD

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

    /**
     * ✅ SỬA LẠI HÀM NÀY HOÀN TOÀN
     * Tạo ra màn hình game bằng cách XẾP CHỒNG 2 LỚP:
     * Lớp Dưới (gamePane) + Lớp Trên (gameUIRoot)
     */
    public void showGameScene() {
        try {
            // 1. Tạo Lớp Dưới (Lớp Gameplay)
            // Đây là nơi GameManager sẽ vẽ nền, gạch, bóng...
            gamePane = new Pane();
            gamePane.setPrefSize(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);

            // 2. Tải Lớp Trên (HUD của bạn) bằng FXMLLoader để lấy Controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/GameUI.fxml"));
            gameUIRoot = loader.load();

            gameUIRoot.setMouseTransparent(true); // Làm cho HUD trong suốt với chuột

            // 3. Lấy và lưu lại Controller của HUD
            gameUIController = loader.getController();

            // 4. Tạo StackPane để xếp chồng
            StackPane root = new StackPane();
            root.getChildren().add(gamePane);     // Thêm Lớp Game (Dưới)
            root.getChildren().add(gameUIRoot); // Thêm Lớp HUD (Trên)

            // 5. Tạo Scene mới và hiển thị
            Scene scene = new Scene(root, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
            primaryStage.setScene(scene);
            root.requestFocus();

        } catch (IOException e) {
            System.err.println("Không thể tải tệp FXML: GameUI.fxml");
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

    // ✅ THÊM CÁC HÀM GETTER ĐỂ GAMEMANAGER SỬ DỤNG

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
}
