package utils;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;

public class BackgroundManager {
    private final Pane gamePane;

    public BackgroundManager(Pane gamePane) {
        this.gamePane = gamePane;
    }

    public void setBackgroundForLevel(int level) {
        String path = switch (level) {
            case 1 -> "/images/background/Background1.png";
            case 2 -> "/images/background/Background2.png";
            case 3 -> "/images/background/Background3.png";
            case 4 -> "/images/background/Background4.png";
            case 5 -> "/images/background/Background5.png";
            default -> "/images/background/Background1.png";
        };
        Image image = ImageLoader.loadImage(path);
        if (image != null) {
            BackgroundImage bg = new BackgroundImage(
                    image,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(1.0, 1.0, true, true, false, false)
            );
            gamePane.setBackground(new Background(bg));
        }
    }
}
