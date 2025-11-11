package level;

public class Level1 {
    /**
     * Trả về bản thiết kế cho màn 1.
     * N = NormalBrick, S = StrongBrick
     */
    public static String[] getMap() {
        return new String[]{
                "                    ",
                "     NNN    NNN     ",
                "    NDNNNDDNNNDN    ",
                "   NNNNNDDDNNNNNN   ",
                "    NNNDHNNHNDNN    ",
                "     NNNNDDNNNN     ",
                "      NDNNNNDN      ",
                "       NNDDNN       ",
                "        NNNN        ",
                "         NN         "
        };
    }
}
