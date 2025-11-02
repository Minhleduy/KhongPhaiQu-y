package gameobject.bricks;

import gameobject.core.Brick;
import javafx.scene.layout.Pane;
import application.Config;

public class NormalBrick extends Brick {

    public NormalBrick(double x, double y, Pane gamePane) {
        super(x, y, Config.BRICK_WIDTH, Config.BRICK_HEIGHT, 1, "/images/brick/Brick11.png", gamePane);
    }

    // KHÔNG CẦN override hit() vì đã dùng logic mặc định từ Brick
}
