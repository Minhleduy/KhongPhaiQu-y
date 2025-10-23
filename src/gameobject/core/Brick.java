package gameobject.core;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

// Lop bo cua cac loai gach
public abstract class Brick extends GameObject {
    protected int hitPoints;        // Số lần cần trúng để vỡ
    protected boolean destroyed;    // Đã bị phá chưa?
    protected ImageView imageView;  //  ảnh của gạch

    public Brick(double x, double y, double width, double height, int hitPoints, String imagePath, Pane gamePane) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
        this.destroyed = false;

        // Tạo ảnh gạch
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);

        // Thêm vào gamePane
        gamePane.getChildren().add(imageView);
    }

    /**
     * Xử lý khi bóng chạm vào gạch
     */
    public void hit(Pane gamePane) {
        if (destroyed) {
            return;
        }

        hitPoints--;
        if (hitPoints <= 0) {
            destroy(gamePane);
        } else {
            imageView.setOpacity(0.7); // hiệu ứng bị đánh
        }
    }

  //Phá huy gach
    public void destroy(Pane gamePane) {
        destroyed = true;
        gamePane.getChildren().remove(imageView);
    }

    public boolean isDestroyed() {

        return destroyed;

    }

    public ImageView getImageView() {
        return imageView;
    }
    public boolean isUnbreakable() {
        return false;
    } //dinh danh xem co phai gach pha duoc ko



    public void update() {
        // Gạch không di chuyển
    }
}
