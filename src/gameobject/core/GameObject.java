package gameobject.core;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


/**
 * Lớp cơ sở cho mọi vật thể trong game (gạch, bóng, paddle, item, v.v.)
 * Cung cấp vị trí, kích thước, hình ảnh và khung va chạm cơ bản.
 */
public abstract class GameObject {

    protected double x;       // Tọa độ X
    protected double y;       // Tọa độ Y
    protected double width;   // Chiều rộng
    protected double height;  // Chiều cao
    protected Image image;
    protected ImageView imageView;// Ảnh đại diện của vật thể

    // ======== CONSTRUCTOR ========
    public GameObject(double x, double y, double width, double height) {
        this(x, y, width, height, null);
    }

    public GameObject(double x, double y, double width, double height, Image image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
    }

    // ======== GETTER / SETTER ========

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }
    public ImageView getImageView() {
        return imageView;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    // ======== HÀM CHUNG ========

    /** Vẽ vật thể lên màn hình (nếu có ảnh) */
    public void draw(GraphicsContext gc) {
        if (image != null) {
            gc.drawImage(image, x, y, width, height);
        }
    }

    /** Trả về khung va chạm (collision bounds) */
    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    /** Hàm cập nhật mỗi frame (override trong class con nếu cần) */
    public void update(double deltaTime) {
        // Mặc định không làm gì
    }
}
