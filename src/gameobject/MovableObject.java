package gameobject;

import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * MovableObject kế thừa từ GameObject, đại diện cho các đối tượng có thể di chuyển.
 */
public abstract class MovableObject extends GameObject {
    protected float velocityX;
    protected float velocityY;

    public MovableObject(float x, float y, int width, int height) {
        super(x, y, width, height);
        this.velocityX = 0;
        this.velocityY = 0;
    }

    public void setVelocity(float vx, float vy) {
        this.velocityX = vx;
        this.velocityY = vy;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    /**
     * Cập nhật vị trí dựa trên vận tốc.
     */
    public void updatePosition() {
        this.x += velocityX;
        this.y += velocityY;
    }

    /**
     * Hàm update chung (các class con có thể override).
     */
    @Override
    public void update() {
        updatePosition();
    }

    @Override
    public abstract void draw(Graphics2D g);
}
