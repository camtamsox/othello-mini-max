package othello;

import javafx.scene.input.MouseEvent;

public class HumanPlayer implements Player{
    private boolean listeningForClick;
    private Board board;
    private int playerColor;
    public HumanPlayer(Board board, int playerColor){
        this.listeningForClick = false;
        this.board = board;
        this.playerColor = playerColor;
    }
    public void handleMouseClick(MouseEvent e){
        if (this.listeningForClick){
            // give board coords of click so it can determine if the click is
            // in a valid square
            this.board.handlePlayerClick(e.getX(),e.getY(), this.playerColor);
        }
    }
    public void setListeningForMove(boolean listeningForClick){
        this.listeningForClick = listeningForClick;
    }
    public int getPlayerColor(){return this.playerColor;}
}
