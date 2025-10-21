package gameobject.bricks;

import gameobject.core.Brick;
import javafx.scene.layout.Pane;

public class NormalBrick extends Brick {

    public NormalBrick(double x, double y, Pane gamePane) {
        super(x, y, 50, 20, 1, "/resources/images/brick/Brick9.png", gamePane);
    }

    // KHÔNG CẦN override hit() vì đã dùng logic mặc định từ Brick
}