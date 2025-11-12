package gameobject.core;

import application.GameManager;
import gameobject.dynamic.Paddle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import utils.SoundManager;

public abstract class PowerUp extends MovableObject {
    protected Pane gameRoot;
    protected Paddle paddle;

    public PowerUp(Pane gameRoot, Paddle paddle, double x, double y, String imagePath) {
        super(x, y, 45, 45, new Image(PowerUp.class.getResourceAsStream(imagePath)));
        this.gameRoot = gameRoot;
        this.paddle = paddle;
        this.dy = 100.0 ; //tốc độ rơi

        Image image = new Image(getClass().getResourceAsStream(imagePath));
        this.imageView = new ImageView(image);
        imageView.setFitWidth(45);
        imageView.setFitHeight(45);
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
        gameRoot.getChildren().add(this.imageView);

        GameManager.getInstance().addGameObject(this);
    }

    @Override
    public void update(double deltaTime) {
        setY(getY() + dy * deltaTime);
        imageView.setLayoutY(getY());

        if (getY() > gameRoot.getHeight()) {
            remove();
            return;
        }

        if (checkCollision()) {
            utils.SoundManager.getInstance().playSoundEffect("/sounds/PickUp.mp3");
            applyEffect(paddle);
            remove();
        }
    }

    private boolean checkCollision() {
        return imageView.getBoundsInParent()
                .intersects(paddle.getImageView().getBoundsInParent());
    }

    protected abstract void applyEffect(Paddle paddle);

    private void remove() {
        GameManager.getInstance().removeGameObject(this);
        gameRoot.getChildren().remove(imageView);
    }

    public ImageView getImageView() {
        return imageView;
    }
}
