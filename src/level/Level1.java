package level;

public class Level1 {
    /**
     * Trả về bản thiết kế cho màn 1.
     * N = NormalBrick, S = StrongBrick
     */
    public static String[] getMap() {
        return new String[]{
                " N N N N N N N N N N ",
                " N S S N N N N S S N ",
                "   N S N N N N S N   ",
                "     N N N N N N     ",
                "       N S S N       "
        };
    }
}
