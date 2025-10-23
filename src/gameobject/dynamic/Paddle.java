package gameobject.dynamic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class Paddle {
    private ImageView imageView;
    private double x, y;
    private double width, height;
    private final double MOVE_SPEED = 8;
    private double sceneWidth;
    private int lives = 3;

    public Paddle(Pane gameRoot, double startX, double startY, double sceneWidth) {
        this.x = startX;
        this.y = startY;
        this.sceneWidth = sceneWidth;

        // Load ảnh paddle
        Image image = new Image(getClass().getResourceAsStream("/resources/images/paddle/paddle1.png"));
        imageView = new ImageView(image);
        width = image.getWidth();
        height = image.getHeight();

        // Đặt vị trí ban đầu
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);

        // Thêm paddle vào Pane
        gameRoot.getChildren().add(imageView);

        // Gắn sự kiện di chuột
        enableMouseControl(gameRoot);
    }
    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void addLife() {
        if (lives < 3) { // Tối đa 3 mạng
            lives++;
        }
    }

    // --- Getter cho ImageView để PowerUp truy cập ---
    public ImageView getImageView() {
        return imageView;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getWidth() {
        return width;
    }
    public double getHeight() {
        return height;
    }

    public void setWidth(double width) {
        this.width = width;
        imageView.setFitWidth(width);
    }

    public void setX(double x) {
        this.x = x;
        updatePosition();
    }

    public void setY(double y) {
        this.y = y;
        updatePosition();
    }

    // --- Di chuyển bằng phím ---
    public void moveLeft() {
        x -= MOVE_SPEED;
        if (x < 0) x = 0;
        updatePosition();
    }

    public void moveRight() {
        x += MOVE_SPEED;
        if (x + width > sceneWidth) x = sceneWidth - width;
        updatePosition();
    }

    // --- Cập nhật vị trí ImageView ---
    private void updatePosition() {
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
    }

    // --- Di chuyển theo chuột ---
    private void enableMouseControl(Pane gameRoot) {
        gameRoot.setOnMouseMoved((MouseEvent e) -> {
            double mouseX = e.getX() - width / 2;
            if (mouseX < 0) mouseX = 0;
            if (mouseX + width > sceneWidth) mouseX = sceneWidth - width;
            x = mouseX;
            updatePosition();
        });
    }
}
