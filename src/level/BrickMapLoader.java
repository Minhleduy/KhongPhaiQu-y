package level;

import application.Config;
import gameobject.dynamic.Paddle;
import gameobject.core.Brick;
import gameobject.bricks.*;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.List;


public class BrickMapLoader {

//    private static final int BRICK_WIDTH = 70;
//    private static final int BRICK_HEIGHT = 25;
    private static final int PADDING = 5;
    private static final int MAP_OFFSET_X = 30;
    private static final int MAP_OFFSET_Y = 50;

    /**
     * Tải và tạo ra một danh sách các đối tượng Brick cho một màn chơi cụ thể.
     */
    public static List<Brick> load(int levelNumber, Pane gameRoot, Paddle paddle) {
        List<Brick> bricks = new ArrayList<>();
        String[] mapData;

        switch (levelNumber) {
            case 1:
                mapData = Level1.getMap();
                break;
            case 2:
                mapData = Level2.getMap();
                break;
            case 3:
                mapData = Level3.getMap();
                break;
            case 4:
                mapData = FinalLevel.getMap();
                break;
            default:
                mapData = Level1.getMap();
                break;
        }

        for (int y = 0; y < mapData.length; y++) {
            String row = mapData[y];
            for (int x = 0; x < row.length(); x++) {
                char brickType = row.charAt(x);
                if (brickType == ' ') {
                    continue;
                }

                double brickX = MAP_OFFSET_X + x * (Config.BRICK_WIDTH + PADDING);
                double brickY = MAP_OFFSET_Y + y * (Config.BRICK_HEIGHT + PADDING);

                Brick brick = null;
                switch (brickType) {
                    case 'N':
                        brick = new NormalBrick(brickX, brickY, gameRoot);
                        break;
                    case 'S':
                        brick = new StrongBrick(brickX, brickY, gameRoot, paddle);
                        break;
                    case 'D':
                        brick = new DoubleBallBrick(brickX, brickY, gameRoot, paddle);
                        break;
                    case 'H':
                        brick = new HeartBrick(brickX, brickY, gameRoot, paddle);
                        break;
                    case 'Q':
                        brick = new QuestionBrick(brickX, brickY, gameRoot, paddle);
                        break;
                    case 'U':
                        brick = new UnbreakableBrick(brickX, brickY, gameRoot);
                        break;
                    case 'B':
                        brick = new BlockBrick(brickX, brickY, gameRoot, paddle);
                        break;
                    case 'T':
                        brick = new ToughBrick(brickX, brickY, gameRoot);
                        break;
                }

                if (brick != null) {
                    bricks.add(brick);
                }
            }
        }
        return bricks;
    }
}
