package Arkanoid.ui;

import Arkanoid.util.AnimationHelper;
import application.GameManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.input.MouseEvent;

public class StoryDialogController {

    private Image robotImage;

    @FXML
    private AnchorPane storyDialogRootPane;
    @FXML
    private ImageView npcImageView; // fx:id="npcImageView"
    @FXML
    private Label dialogueLabel; // fx:id="dialogueLabel"
    @FXML
    private Button continueButton; // fx:id="continueButton"
    @FXML
    private HBox choicesBox; // fx:id="choicesBox"
    @FXML
    private Button choiceAButton; // fx:id="choiceAButton"
    @FXML
    private Button choiceBButton; // fx:id="choiceBButton"

    /**
     * Tự động chạy khi FXML được tải
     */
    @FXML
    public void initialize() {
        try {
            robotImage = new Image(getClass().getResourceAsStream("/images/npc/robot_dialog.png"));
            if (robotImage != null && npcImageView != null) {
                npcImageView.setImage(robotImage);
            }
        } catch (Exception e) {
            System.err.println("LỖI NGHIÊM TRỌNG: Không thể tải ảnh robot!");
            e.printStackTrace();
        }

        AnimationHelper.applyFadeIn(continueButton, 2000);
        AnimationHelper.applyFadeIn(choicesBox, 2000);
    }

    /**
     * Hiển thị hội thoại
     */
    public void showDialogue(String text) {
        if (npcImageView != null) {
            npcImageView.setImage(robotImage);
            npcImageView.setVisible(true);
        }
        if (dialogueLabel != null) {
            dialogueLabel.setText(text);
        }
        if (continueButton != null) {
            continueButton.setVisible(true);
        }
        if (choicesBox != null) {
            choicesBox.setVisible(false);
        }
    }

    /**
     * Hiển thị câu hỏi
     */
    public void showQuestion(String question, String answerA, String answerB) {
        if (npcImageView != null) {
            npcImageView.setImage(robotImage);
            npcImageView.setVisible(true);
        }

        dialogueLabel.setText(question);
        choiceAButton.setText(answerA);
        choiceBButton.setText(answerB);
        continueButton.setVisible(false);
        choicesBox.setVisible(true);
    }

    @FXML
    void handlePaneClicked(MouseEvent event) {
       if (continueButton != null && continueButton.isVisible()) {
            closeDialog();
        }
    }

    @FXML
    void handleContinueAction(ActionEvent event) {
        closeDialog();
    }

    @FXML
    void handleChoiceAAction(ActionEvent event) {
        System.out.println("Người dùng chọn A.");
        // closeDialog();
    }

    @FXML
    void handleChoiceBAction(ActionEvent event) {
        System.out.println("Người dùng chọn B.");
        // closeDialog();
    }

    /**
     * Hàm đóng dialog và MỞ KHÓA game
     */
    private void closeDialog() {
        System.out.println("Đóng dialog, mở khóa game...");
        if (storyDialogRootPane != null) {
            storyDialogRootPane.setVisible(false);
            storyDialogRootPane.setMouseTransparent(true);
        }
        // Mở khóa vòng lặp game
        GameManager.IS_GAME_PAUSED_FOR_STORY = false;
    }
}
