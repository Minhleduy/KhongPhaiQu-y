package gameobject.dynamic;

import gameobject.core.MovableObject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class Paddle extends MovableObject {
    private ImageView imageView;
    private final double MOVE_SPEED = 8;
    private double sceneWidth;
    private int lives = 3;

    // SỬA CONSTRUCTOR - gọi super()
    public Paddle(Pane gameRoot, double startX, double startY, double sceneWidth) {
        // GỌI SUPER() VỚI ĐÚNG THAM SỐ
        super(startX, startY, 0, 0, null); // Tạm thời width=0, height=0, image=null

        this.sceneWidth = sceneWidth;

        // Load ảnh paddle
        Image image = new Image(getClass().getResourceAsStream("/resources/images/paddle/paddle1.png"));
        imageView = new ImageView(image);

        // Cập nhật width, height từ ảnh thực tế
        setWidth(image.getWidth());
        setHeight(image.getHeight());

        // Đặt vị trí ban đầu
        imageView.setLayoutX(startX);
        imageView.setLayoutY(startY);

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

    // SỬA: Dùng getter từ GameObject
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

    // SỬA: Ghi đè setter để cập nhật cả ImageView
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

    // --- Di chuyển bằng phím ---
    public void moveLeft() {
        setX(getX() - MOVE_SPEED);
        if (getX() < 0) setX(0);
    }

    public void moveRight() {
        setX(getX() + MOVE_SPEED);
        if (getX() + getWidth() > sceneWidth) setX(sceneWidth - getWidth());
    }

    // --- Cập nhật vị trí ImageView ---
    private void updatePosition() {
        imageView.setLayoutX(getX());
        imageView.setLayoutY(getY());
    }

    // --- Di chuyển theo chuột ---
    private void enableMouseControl(Pane gameRoot) {
        gameRoot.setOnMouseMoved((MouseEvent e) -> {
            double mouseX = e.getX() - getWidth() / 2;
            if (mouseX < 0) mouseX = 0;
            if (mouseX + getWidth() > sceneWidth) mouseX = sceneWidth - getWidth();
            setX(mouseX);
        });
    }

    // SỬA: Thêm update method nếu cần
    @Override
    public void update(double deltaTime) {
        // Paddle di chuyển theo input, không cần update tự động
    }
}