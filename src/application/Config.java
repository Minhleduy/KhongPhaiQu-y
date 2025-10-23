package application;

public final class Config {

    private Config() {}

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;

    public static final int INITIAL_LIVES = 3;
    public static final String GAME_TITLE = "Arkanoid";

    public static final double PADDLE_WIDTH = 120;
    public static final double PADDLE_HEIGHT = 20;
    public static final double PADDLE_Y_POSITION = SCREEN_HEIGHT - 60;
    public static final double PADDLE_SPEED = 10.0;

    public static final double BALL_RADIUS = 8.0;
    public static final double BALL_INITIAL_SPEED_X = 4.0;
    public static final double BALL_INITIAL_SPEED_Y = -4.0;
}
