package othello;

public class Constants {

    public static final int WHITE = 0;
    public static final int BLACK = 1;
    public static final int CONTROLS_PANE_WIDTH = 250;
    public static final int SQUARE_WIDTH = 50;
    public static final int SCENE_WIDTH = CONTROLS_PANE_WIDTH + 30 + 10*SQUARE_WIDTH;
    public static final int SCENE_HEIGHT = 10*SQUARE_WIDTH;
    public static final int PIECE_RADIUS = (int)(0.45*SQUARE_WIDTH);
    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public static final int UP_LEFT = 4;
    public static final int UP_RIGHT = 5;
    public static final int DOWN_LEFT = 6;
    public static final int DOWN_RIGHT = 7;
    public static final double COMPUTER_MOVE_DELAY = 1;
}
