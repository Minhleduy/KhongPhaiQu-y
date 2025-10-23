package level;

public class Level2 {
    /**
     * Trả về bản thiết kế cho màn 2.
     * H = HeartBrick, D = DoubleBallBrick
     */
    public static String[] getMap() {
        return new String[]{
                " S S S S S S S S S S ",
                " S N N N N N N N N S ",
                " S N H S S S S H N S ",
                " S N N N D N N N N S ",
                " S N S S S S S S N S ",
                "   N N N N N N N N   "
        };
    }
}
