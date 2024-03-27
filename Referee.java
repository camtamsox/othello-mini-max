package othello;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class Referee {
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Board board;
    private Label scoreLabel;
    private Label turnLabel;
    private int currentPlayerColor;
    public Referee(Pane pane, Board board, int whitePlayerMode, int blackPlayerMode){
        pane.setOnMouseClicked((MouseEvent e) -> this.handleMouseClick(e));
        this.board = board;
        this.currentPlayerColor = Constants.BLACK; // black always goes first
        this.createPlayers(whitePlayerMode,blackPlayerMode);
    }
    public void setupLabels(Label scoreLabel, Label turnLabel){
        this.scoreLabel = scoreLabel;
        this.turnLabel = turnLabel;
    }
    public void createPlayers(int whitePlayerMode, int blackPlayerMode){
        double computerMoveDelay = Constants.COMPUTER_MOVE_DELAY;
        // if both players are computers, make move delay small so game doesn't take too long
        if (whitePlayerMode > 0 && blackPlayerMode > 0){
            computerMoveDelay = computerMoveDelay*0.05;
        }
        if (blackPlayerMode == 0) {
            this.player1 = new HumanPlayer(this.board, Constants.BLACK);
        } else {
            this.player1 = new ComputerPlayer(Constants.BLACK, this.board,
                    blackPlayerMode, this, computerMoveDelay);
        }
        if (whitePlayerMode == 0) {
            this.player2 = new HumanPlayer(this.board, Constants.WHITE);
        } else {
            this.player2 = new ComputerPlayer(Constants.WHITE, this.board,
                    whitePlayerMode, this, computerMoveDelay);
        }
        if (this.currentPlayerColor == Constants.BLACK){
            this.currentPlayer = this.player1;
            this.player1.setListeningForMove(true);
            this.player2.setListeningForMove(false);
        } else {
            this.currentPlayer = this.player2;
            this.player2.setListeningForMove(true);
            this.player1.setListeningForMove(false);
        }
        this.board.resetPossibleMoves();
        this.board.showPossibleMoves(this.currentPlayerColor);
    }
    public void resetGame(){
        this.board.resetPossibleMoves();
        this.board.resetBoard();

        this.scoreLabel.setText("White:  Black:");
        this.turnLabel.setText("Black to move");
        this.currentPlayerColor = Constants.BLACK; // black always goes first
    }
    public void handleMouseClick(MouseEvent e){
        this.currentPlayer.handleMouseClick(e);
        e.consume();
    }
    public void changePlayerTurns(boolean opponentJustMoved){
        this.updateScore();
        this.board.resetPossibleMoves();
        // change which player's move it is
        if (this.currentPlayer == this.player1){
            this.currentPlayer = this.player2;
            this.currentPlayerColor = this.player2.getPlayerColor();
        } else {
            this.currentPlayer = this.player1;
            this.currentPlayerColor = this.player1.getPlayerColor();
        }
        boolean areMovesPossible = this.board.showPossibleMoves(this.currentPlayerColor);
        // check if game is over. If it is, don't listen for moves
        if (!areMovesPossible && !opponentJustMoved) {
            this.gameOver();
            this.player1.setListeningForMove(false);
            this.player2.setListeningForMove(false);
        } else if (!areMovesPossible){
            System.out.println("this.changePlayerTurns(false)");
            this.changePlayerTurns(false);
        } else {
            // listen for move from current player
            if (this.currentPlayer == this.player1){
                this.player1.setListeningForMove(true);
                this.player2.setListeningForMove(false);
            } else {
                this.player2.setListeningForMove(true);
                this.player1.setListeningForMove(false);
            }
            this.updateTurnLabel(this.currentPlayerColor);
        }
    }
    private void gameOver(){
        int[] score = this.board.getScore();
        if (score[0] > score[1]){
            this.turnLabel.setText("GAME OVER! White wins!");
        } else if (score[1] > score[0]){
            this.turnLabel.setText("GAME OVER! Black wins!");
        } else if (score[0] == score[1]){
            this.turnLabel.setText("GAME OVER! It is a tie!");
        }
    }
    private void updateScore(){
        int[] score = this.board.getScore();

        this.scoreLabel.setText("White: " + score[0] + "  Black: " + score[1]);
    }
    private void updateTurnLabel(int currentPlayerColor){
        if (currentPlayerColor == Constants.WHITE){
            this.turnLabel.setText("White to move");
        } else {
            this.turnLabel.setText("Black to move");
        }

    }
}
