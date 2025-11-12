package level;

public class Level2 {
    /**
     * Trả về bản thiết kế cho màn 2.
     * H = HeartBrick, D = DoubleBallBrick
     */
    public static String[] getMap() {
        return new String[]{
                "                     ",
                " N   N  T   T  N    N",
                " NN NN  S   S  N   N ",
                " N D N  T   T  N  N  ",
                " N H N  T   T  N N   ",
                " N   N  TSSST  DN    ",
                " D   D  T   T  N N   ",
                " N   N  T   T  N  N  ",
                " N   N  S   S  N   N ",
                " N   N  T   T  N    N",
                " UUUUU  UUUUU  UUUUU ",
                "          N          ",
        };
    }
}
