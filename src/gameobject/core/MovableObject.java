package gameobject.core;

import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;

public abstract class MovableObject extends GameObject {
    protected double dx;  // vận tốc theo trục X
    protected double dy;  // vận tốc theo trục Y

    public MovableObject(double x, double y, double width, double height, Image image) {
        super(x, y, width, height, image);
        this.dx = 0;
        this.dy = 0;
    }

    // --- Getter và Setter cho vận tốc ---
    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    // --- Cập nhật vị trí theo vận tốc ---
    @Override
    public void update(double deltaTime) {
        // SỬA: Dùng setX/setY để cập nhật cả ImageView (nếu có)
        setX(getX() + dx * deltaTime);
        setY(getY() + dy * deltaTime);
    }

    // --- Vẽ vật thể ---
    @Override
    public void draw(GraphicsContext gc) {
        if (image != null) {
            gc.drawImage(image, x, y, width, height);
        } else {
            gc.fillRect(x, y, width, height);
        }
    }

    // --- Xử lý va chạm với biên ---
    public void checkBounds(double minX, double minY, double maxX, double maxY) {
        if (x < minX) {
            x = minX;
            dx = -dx;
        } else if (x + width > maxX) {
            x = maxX - width;
            dx = -dx;
        }

        if (y < minY) {
            y = minY;
            dy = -dy;
        } else if (y + height > maxY) {
            y = maxY - height;
            dy = -dy;
        }
    }
}
