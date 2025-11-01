package level;

public class Level1 {
    /**
     * Trả về bản thiết kế cho màn 1.
     * N = NormalBrick, S = StrongBrick
     */
    public static String[] getMap() {
        return new String[]{
                " N N Q N N S S S N N ",
                " N S S N D N H S S N ",
                "   N S N N Q N S N   ",
                "     N H S S N N     ",
                "       N D S N       "
        };
    }
}
