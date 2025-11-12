package Arkanoid.ui;

import application.GameManager;
import application.GameManager.GameState;
import application.LevelManager;
import gameobject.dynamic.Boss;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;

public class GameUIController {
    @FXML private AnchorPane gameUIPane;
    @FXML private HBox livesContainer;
    @FXML private Label levelLabel;
    @FXML private AnchorPane bossHealthBarContainer;
    @FXML private Rectangle bossHealthBar;
    @FXML private Label bossNameLabel;

    private Image heartImage;
    private GameManager gameManager;
    private double maxBossHealthBarWidth;

    @FXML
    public void initialize() {
        gameManager = GameManager.getInstance();
        heartImage = new Image(getClass().getResourceAsStream("/images/items/heart.png"));
        if (bossHealthBar != null) {
            maxBossHealthBarWidth = bossHealthBar.getWidth();
        }
        setBossUIVisible(false);
    }

    public void updateLives(int lives) {
        if (livesContainer.getChildren().size() == lives) return;
            livesContainer.getChildren().clear();
        for (int i = 0; i < lives; i++) {
            ImageView heart = new ImageView(heartImage);
            heart.setFitWidth(35);
            heart.setFitHeight(35);
            livesContainer.getChildren().add(heart);
        }
    }

    public void updateLevel(int level) {
        levelLabel.setText("Level " + level);
        levelLabel.setVisible(true);
        setBossUIVisible(false);
    }

    public void updateBossHealth(int current, int max) {
        if (max <= 0) return;
        double ratio = (double) current / max;
        bossHealthBar.setWidth(Math.max(0, maxBossHealthBarWidth * ratio));
    }

    public void showBossUI() {
        setBossUIVisible(true);
        levelLabel.setVisible(false);
    }

    private void setBossUIVisible(boolean visible) {
        if (bossHealthBarContainer != null) bossHealthBarContainer.setVisible(visible);
        if (bossNameLabel != null) bossNameLabel.setVisible(visible);
    }
}
