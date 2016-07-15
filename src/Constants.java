public class Constants {
	public static final int B_WIDTH = 500;
    public static final int B_HEIGHT = 500;
    public static final int DELAY = 25;

    public static final String KEY_LEFT_ARROW = "VK_LEFT";
    public static final String KEY_RIGHT_ARROW = "VK_RIGHT";
    public static final String KEY_DOWN_ARROW = "VK_DOWN";
    public static final String KEY_UP_ARROW = "VK_UP";
    public static final String KEY_SOFTKEY1 = "VK_ENTER";
    public static final String KEY_SOFTKEY2 = "VK_SPACE";

    public static final int startX = 4;
    public static final int startY = 4;
    public static final int MAX_BALLS = 12;
    public static final int CELL_SIZE = 20; //These 3 constanst needs to be completely parametric, now it is only available for 6 pixel balls
    public static final int BOARD_WIDTH = CELL_SIZE * 20 + 8;
    public static final int BOARD_HEIGHT = CELL_SIZE * 20 + 8;
    public static final int BALL_LENGTH = 3;
    public static final long speed = 400;
    public static int[] transparency = {
        0xcc2C3A90, 
        0x332C3A90
    };

    public static final int LEVEL = 1, HIGHSCORES = 2, ISCONTINUE = 12, LOADTHEM = 13;

    public static String[] strMenu = {
        "New Game", 
        "Continue", 
        "Finish Game",
        "Help", 
        "About",
        "Exit", 
        "Level", 
        "YES", 
        "NO"
    };
    
    public static int[][] MenuModes = {
        { 0, 6, 3, 4, 5 },
        { 1, 2, 3, 5 },
        { 7, 8 }
    };

    public static final int GAME_MODE_STANDARD = 0;
    public static final int GAME_MODE_SPLASH = 1;
    public static final int GAME_MODE_MENU = 2;
    public static final int GAME_MODE_EXIT = 3;

    public static final int OFF   = 0;
    public static final int FATAL = 100;
    public static final int ERROR = 200;
    public static final int WARN  = 300;
    public static final int INFO  = 400;
    public static final int DEBUG = 500;
    public static final int TRACE = 600;
    public static final int ALL   = Integer.MAX_VALUE;

    public static int LOG_LEVEL = ERROR;
}
