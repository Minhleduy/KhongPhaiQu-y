package application;

public final class Config {

    private Config() {}

    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;

    public static final int INITIAL_LIVES = 3;
    public static final String GAME_TITLE = "Arkanoid";

    public static final double PADDLE_WIDTH = 120;
    public static final double PADDLE_HEIGHT = 20;
    public static final double PADDLE_Y_POSITION = SCREEN_HEIGHT - 80;
    public static final double PADDLE_SPEED = 12.0;

    public static final double BALL_RADIUS = 8.0;
//    public static final double BALL_INITIAL_SPEED_X = 5.0;
//    public static final double BALL_INITIAL_SPEED_Y = -5.0;
    public static final double BALL_SPEED_Y = -400.0; // Tốc độ dọc (LUÔN LÀ SỐ ÂM để bay lên)
    public static final double BALL_MAX_SPEED_X = 400.0; // Tốc độ ngang TỐI ĐA

    public static final double BRICK_WIDTH = 55.0;
    public static final double BRICK_HEIGHT = 24.0;
}
