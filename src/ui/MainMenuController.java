package Arkanoid.ui;

import Arkanoid.util.AnimationHelper;
import application.GameManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import application.SceneManager;

public class MainMenuController {

    @FXML
    private Button startButton;

    @FXML
    private Button exitButton;

    @FXML
    void handleStartButtonAction(ActionEvent event) {
        System.out.println("Nút Bắt Đầu đã được nhấn!");
        GameManager.getInstance().startGame();
    }

    @FXML
    void handleExitButtonAction(ActionEvent event) {
        System.out.println("Nút Thoát đã được nhấn! Đang đóng ứng dụng...");
        Platform.exit();
    }
    @FXML
    public void initialize() {
        // Áp dụng hiệu ứng fade-in cho các nút khi menu vừa mở lên
        // Nút sẽ hiện ra trong 1 giây (1000 mili giây)
        AnimationHelper.applyFadeIn(startButton, 3000);
        AnimationHelper.applyFadeIn(exitButton, 3000);
    }
}
