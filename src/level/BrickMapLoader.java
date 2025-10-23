package level;

import gameobject.core.Brick;
import gameobject.bricks.*;
import java.util.ArrayList;
import java.util.List;

public class BrickMapLoader {

    // Kích thước và khoảng cách để vẽ các viên gạch
    private static final int BRICK_WIDTH = 70;
    private static final int BRICK_HEIGHT = 25;
    private static final int PADDING = 5; // Khoảng trống giữa các viên gạch
    private static final int MAP_OFFSET_X = 30; // Căn lề cho toàn bộ bản đồ gạch
    private static final int MAP_OFFSET_Y = 50;

    /**
     * Tải và tạo ra một danh sách các đối tượng Brick cho một màn chơi cụ thể.
     * @param levelNumber Số thứ tự của màn chơi cần tải.
     * @return Một danh sách (List) các đối tượng Brick.
     */
    public static List<Brick> load(int levelNumber) {
        List<Brick> bricks = new ArrayList<>();
        String[] mapData;

        // Chọn bản thiết kế dựa trên số thứ tự màn chơi
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
                // Nếu không tìm thấy, tải màn 1 làm mặc định
                mapData = Level1.getMap();
                break;
        }

        // Duyệt qua bản thiết kế để tạo gạch
        for (int y = 0; y < mapData.length; y++) {
            String row = mapData[y];
            for (int x = 0; x < row.length(); x++) {
                char brickType = row.charAt(x);
                if (brickType == ' ') {
                    continue; // Bỏ qua nếu là khoảng trống
                }

                double brickX = MAP_OFFSET_X + x * (BRICK_WIDTH + PADDING);
                double brickY = MAP_OFFSET_Y + y * (BRICK_HEIGHT + PADDING);

                Brick brick = null;
                // Tạo loại gạch tương ứng với ký tự
                switch (brickType) {
                    case 'N':
                        brick = new NormalBrick(brickX, brickY, BRICK_WIDTH, BRICK_HEIGHT);
                        break;
                    case 'S':
                        brick = new StrongBrick(brickX, brickY, BRICK_WIDTH, BRICK_HEIGHT);
                        break;
                    case 'D':
                        brick = new DoubleBallBrick(brickX, brickY, BRICK_WIDTH, BRICK_HEIGHT);
                        break;
                    case 'H':
                        brick = new HeartBrick(brickX, brickY, BRICK_WIDTH, BRICK_HEIGHT);
                        break;
                    case 'Q':
                        brick = new QuestionBrick(brickX, brickY, BRICK_WIDTH, BRICK_HEIGHT);
                        break;
                    case 'U':
                        brick = new UnbreakableBrick(brickX, brickY, BRICK_WIDTH, BRICK_HEIGHT);
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
