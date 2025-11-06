package Arkanoid.ui;
import application.GameManager;
import application.SceneManager;
import Arkanoid.util.AnimationHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class GameOverController {

    //Button có fx:id="playAgainButton"
    @FXML
    private Button playAgainButton;

    // Button có fx:id="mainMenuButton"
    @FXML
    private Button mainMenuButton;

    @FXML
    void handlePlayAgainAction(ActionEvent event) {
        System.out.println("Nút Chơi Lại (Game Over) đã được nhấn!");
        GameManager.getInstance().startGame();
    }
    @FXML
    public void initialize() {
        AnimationHelper.applyFadeIn(playAgainButton, 3000);
        AnimationHelper.applyFadeIn(mainMenuButton, 3000);
    }

    @FXML
    void handleMainMenuAction(ActionEvent event) {
        System.out.println("Nút Về Menu (Game Over) đã được nhấn!");
        SceneManager.getInstance().showMainMenuScene();
    }
}
