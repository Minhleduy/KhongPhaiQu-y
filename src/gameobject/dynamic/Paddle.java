package gameobject.dynamic;

import gameobject.core.MovableObject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class Paddle extends MovableObject {
    private final double MOVE_SPEED = 8;
    private double sceneWidth;
    private int lives = 3;

    public Paddle(Pane gameRoot, double startX, double startY, double sceneWidth) {
        super(startX, startY, 0, 0, null);

        this.sceneWidth = sceneWidth;

        Image image = new Image(getClass().getResourceAsStream("/images/paddle/paddle1.png"));
        this.imageView = new ImageView(image);

        setWidth(image.getWidth());
        setHeight(image.getHeight());

        imageView.setLayoutX(startX);
        imageView.setLayoutY(startY);

        gameRoot.getChildren().add(this.imageView);
        enableMouseControl(gameRoot);
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void addLife() {
        if (lives < 3) {
            lives++;
        }
    }

    public ImageView getImageView() {
        return imageView;
    }

    public double getX() {
        return super.getX();
    }
    public double getY() {
        return super.getY();
    }
    public double getWidth() {
        return super.getWidth();
    }
    public double getHeight() {
        return super.getHeight();
    }

    @Override
    public void setWidth(double width) {
        super.setWidth(width);
        imageView.setFitWidth(width);
    }

    @Override
    public void setX(double x) {
        super.setX(x);
        updatePosition();
    }

    @Override
    public void setY(double y) {
        super.setY(y);
        updatePosition();
    }

    public void moveLeft() {
        setX(getX() - MOVE_SPEED);
        if (getX() < 0) setX(0);
    }

    public void moveRight() {
        setX(getX() + MOVE_SPEED);
        if (getX() + getWidth() > sceneWidth) setX(sceneWidth - getWidth());
    }

    private void updatePosition() {
        imageView.setLayoutX(getX());
        imageView.setLayoutY(getY());
    }

    private void enableMouseControl(Pane gameRoot) {
        gameRoot.setOnMouseMoved((MouseEvent e) -> {
            double mouseX = e.getX() - getWidth() / 2;
            if (mouseX < 0) mouseX = 0;
            if (mouseX + getWidth() > sceneWidth) mouseX = sceneWidth - getWidth();
            setX(mouseX);
        });
    }

    @Override
    public void update(double deltaTime) {
    }
}
