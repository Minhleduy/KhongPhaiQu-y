package Arkanoid.ui;

import Arkanoid.util.AnimationHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class StoryDialogController {

    @FXML
    private ImageView npcImageView;
    @FXML
    private Label dialogueLabel;

    @FXML
    private Button continueButton;
    @FXML
    private HBox choicesBox;
    @FXML
    private Button choiceAButton;
    @FXML
    private Button choiceBButton;

    /**
     * Chuyển hộp thoại sang CHẾ ĐỘ KỂ CHUYỆN.
     * Hiện lời thoại và nút "Tiếp Tục".
     */
    public void showDialogue(Image npcImage, String text) {
        npcImageView.setImage(npcImage);
        dialogueLabel.setText(text);
        continueButton.setVisible(true);
        choicesBox.setVisible(false);
    }

    /**
     * Chuyển hộp thoại sang CHẾ ĐỘ CÂU HỎI.
     * Hiện câu hỏi và các nút lựa chọn.
     */
    public void showQuestion(String question, String answerA, String answerB) {
        dialogueLabel.setText(question);
        choiceAButton.setText(answerA);
        choiceBButton.setText(answerB);

        continueButton.setVisible(false);

        choicesBox.setVisible(true);
    }

    public void initialize() {
        AnimationHelper.applyFadeIn(continueButton, 2000);
        AnimationHelper.applyFadeIn(choicesBox, 2000);
    }
    @FXML
    void handleContinueAction(ActionEvent event) {
        System.out.println("Người dùng nhấn 'Tiếp Tục'.");
    }

    @FXML
    void handleChoiceAAction(ActionEvent event) {
        System.out.println("Người dùng chọn A.");
    }

    @FXML
    void handleChoiceBAction(ActionEvent event) {
        System.out.println("Người dùng chọn B.");
    }
}
