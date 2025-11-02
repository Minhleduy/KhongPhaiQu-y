package gameobject.bricks;

import application.Config;
import gameobject.items.StrongBallItem;
import gameobject.core.Brick;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import gameobject.dynamic.Paddle;


public class StrongBrick extends Brick {

    // THÊM 1: Thêm biến để lưu lại Paddle
    private Paddle paddle;

    // THÊM 2: Nhận Paddle trong hàm khởi tạo
    public StrongBrick(double x, double y, Pane gamePane, Paddle paddle) {
        super(x, y, Config.BRICK_WIDTH, Config.BRICK_HEIGHT, 1, "/images/brick/Brick3.png", gamePane);

        // Lưu lại paddle để dùng sau
        this.paddle = paddle;
    }

    @Override
    public void hit(Pane gamePane) {
        this.hitPoints--;

        if (this.hitPoints <= 0) {
            destroy(gamePane);

            // THÊM 3: Truyền "this.paddle" khi tạo ra item
            new StrongBallItem(gamePane, this.paddle, getX(), getY());
        }
    }
}
