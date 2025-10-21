package Arkanoid.ui;

import Arkanoid.util.AnimationHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WinScreenController {

    @FXML
    private Button playAgainButton;

    @FXML
    private Button mainMenuButton;

    @FXML
    void handlePlayAgainAction(ActionEvent event) {
        System.out.println("Nút Chơi Lại (Win Screen) đã được nhấn!");
    }
    @FXML
    public void initialize() {
        AnimationHelper.applyFadeIn(playAgainButton, 3000);
        AnimationHelper.applyFadeIn(mainMenuButton, 3000);
    }
    @FXML
    void handleMainMenuAction(ActionEvent event) {
        System.out.println("Nút Về Menu (Win Screen) đã được nhấn!");
    }
}
