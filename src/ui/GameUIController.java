package Arkanoid.ui;

import application.GameManager;
import application.GameManager.GameState;
import application.LevelManager;
import gameobject.dynamic.Boss;
import javafx.animation.AnimationTimer;
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
    private AnimationTimer uiUpdater;
    private double maxBossHealthBarWidth;

    @FXML
    public void initialize() {
        gameManager = GameManager.getInstance();
        heartImage = new Image(getClass().getResourceAsStream("/images/items/heart.png"));

        if (bossHealthBar != null) {
            maxBossHealthBarWidth = bossHealthBar.getWidth();
        }

        setBossUIVisible(false);

        uiUpdater = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateUI();
            }
        };
        uiUpdater.start();
    }

    /**
     * Phương thức chính được gọi mỗi khung hình để cập nhật toàn bộ giao diện.
     */
    private void updateUI() {
        if (gameManager == null) return;

        updateLives(gameManager.getLives());

        GameState currentState = gameManager.getGameState();

        if (currentState == GameState.BOSS_FIGHT) {
            setBossUIVisible(true);
            levelLabel.setVisible(false);

            Boss boss = gameManager.getBoss();
            if (boss != null && boss.isAlive()) {
                updateBossHealth(boss.getHealth(), boss.getMaxHealth());
            }
        }
        else if (currentState == GameState.PLAYING) {
            setBossUIVisible(false);
            levelLabel.setVisible(true);
            updateLevel(LevelManager.getInstance().getCurrentLevelNumber());
        }
    }


    public void updateLives(int lives) {
        if (livesContainer.getChildren().size() == lives) return;

        livesContainer.getChildren().clear();
        for (int i = 0; i < lives; i++) {
            ImageView heartImageView = new ImageView(heartImage);
            heartImageView.setFitWidth(35);
            heartImageView.setFitHeight(35);
            livesContainer.getChildren().add(heartImageView);
        }
    }

    public void updateLevel(int level) {
        levelLabel.setText("Level " + level);
    }

    public void updateBossHealth(int currentHealth, int maxHealth) {
        if (maxHealth <= 0) return;

        double healthPercentage = (double) currentHealth / maxHealth;
        double newWidth = maxBossHealthBarWidth * healthPercentage;

        bossHealthBar.setWidth(Math.max(0, newWidth));
    }

    private void setBossUIVisible(boolean isVisible) {
        if (bossHealthBarContainer != null) {
            bossHealthBarContainer.setVisible(isVisible);
        }
        if (bossNameLabel != null) {
            bossNameLabel.setVisible(isVisible);
        }
    }
}
