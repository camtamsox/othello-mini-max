package othello;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.util.ArrayList;

public class ComputerPlayer implements Player{
    private int playerColor;
    private int movesSkippedInARow; // to check for game over
    private int[][] boardWeights;
    private Board board;
    private int intelligenceLevel;
    private Referee referee;
    private double moveDelay;
    public ComputerPlayer(int playerColor, Board board, int intelligenceLevel,
                          Referee referee, double moveDelay){
        this.playerColor = playerColor;
        this.movesSkippedInARow = 0;
        this.boardWeights = this.makeBoardWeights();
        this.board = board;
        this.intelligenceLevel = intelligenceLevel;
        this.referee = referee;
        this.moveDelay = moveDelay;
    }
    public int[] getBestMove(BoardCopy boardCopy, int intelligence, int currentPlayer){
        BoardCopy initialBoardCopy = new BoardCopy(boardCopy);
        ArrayList<int[]> possibleMoves = boardCopy.getPossibleMoves(currentPlayer);
        boolean gameIsOver = this.movesSkippedInARow > 0;
        if (possibleMoves.size() == 0){
            if (intelligence == 1 || gameIsOver){ // at base case or game is over
                return new int[] {-1,-1}; // -1 means no move was possible
            } else {
                this.movesSkippedInARow++;
                // switch current player
                if (currentPlayer == Constants.WHITE){
                    currentPlayer = Constants.BLACK;
                } else {
                    currentPlayer = Constants.WHITE;
                }
                return this.getBestMove(boardCopy, intelligence - 1, currentPlayer);
            }
        }
        this.movesSkippedInARow = 0;
        int[] moveValues = new int[possibleMoves.size()];
        for (int i = 0; i < possibleMoves.size(); i++){
            boardCopy = new BoardCopy(initialBoardCopy); // resetting board
            int[] move = possibleMoves.get(i);
            boardCopy.makeMove(move, currentPlayer);
            if (intelligence == 1){ // at base case
                moveValues[i] = this.boardEval(boardCopy, currentPlayer, gameIsOver);
            } else {
                // switch current player
                if (currentPlayer == Constants.WHITE){
                    currentPlayer = Constants.BLACK;
                } else {
                    currentPlayer = Constants.WHITE;
                }
                int[] bestMove = this.getBestMove(new BoardCopy(boardCopy), intelligence - 1, currentPlayer);
                boardCopy.makeMove(bestMove, currentPlayer);
                // negate because evaluating value for opponent
                moveValues[i] = -1 * this.boardEval(boardCopy, currentPlayer, gameIsOver);
                // switch current player back
                if (currentPlayer == Constants.WHITE){
                    currentPlayer = Constants.BLACK;
                } else {
                    currentPlayer = Constants.WHITE;
                }
            }
        }
        // find move with the highest value
        int bestMoveIndex = 0;
        int bestMoveValue = moveValues[0];
        for (int i = 0; i < moveValues.length; i++){
            if (moveValues[i] > bestMoveValue){
                bestMoveIndex = i;
                bestMoveValue = moveValues[i];
            }
        }
        return possibleMoves.get(bestMoveIndex); // return move with the highest value
    }
    public BoardCopy makeBoardCopy(Board board){
        return new BoardCopy(board);
    }
    private int boardEval(BoardCopy boardCopy, int currentPlayer, boolean gameIsOver){
        int currentPlayerScore = 0;
        int opponentScore = 0;
        int[][] boardState = boardCopy.getBoardState();
        if (!gameIsOver) {
            // evaluate using weights
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    int currentSquare = boardState[i + 1][j + 1]; // boardState includes borders
                    int currentWeight = this.boardWeights[j][i];
                    if (currentSquare == currentPlayer) {
                        currentPlayerScore += currentWeight;
                    } else if (currentSquare != -1) {
                        // when square isn't empty (and isn't currentPlayer), it must be opponent player
                        opponentScore += currentWeight;
                    }
                }
            }
            return currentPlayerScore - opponentScore;
        } else {
            // count number of pieces each player has
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    int currentSquare = boardState[i + 1][j + 1]; // boardState includes borders
                    if (currentSquare == currentPlayer) {
                        currentPlayerScore += 1;
                    } else if (currentSquare != -1) {
                        // when square isn't empty (and isn't currentPlayer), it must be opponent player
                        opponentScore += 1;
                    }
                }
            }
            // assign arbitrarily large value to winning player
            if (currentPlayerScore > opponentScore){
                return 10000;
            } else if (opponentScore > currentPlayer){
                return -10000;
            } else {
                return 0; // 0 for tie
            }
        }
    }
    private int[][] makeBoardWeights(){
        return new int[][]{
                {200,-70,30,25,25,30,-70,200},
                {-70,-100,-10,-10,-10,-10,-100,-70},
                {30,-10,2,2,2,2,-10,30},
                {25,-10,2,2,2,2,-10,25},
                {25,-10,2,2,2,2,-10,25},
                {30,-10,2,2,2,2,-10,30},
                {-70,-100,-10,-10,-10,-10,-100,-70},
                {200,-70,30,25,25,30,-70,200}
        };
    }
    public void handleMouseClick(MouseEvent e){
        // do nothing on click
    }
    /*
     called by referee when the players switch turns. Gets computer move when referee
     is listening for move
     */
    public void setListeningForMove(boolean listeningForMove){
        if (listeningForMove){
            this.getMoveDelayed();
        }
    }
    /*
     timeline delays computer move by one second so that player can see where it moves. Called
     in setListeningForMove
     */
    private void getMoveDelayed(){
        KeyFrame kf = new KeyFrame(Duration.seconds(this.moveDelay),
                (ActionEvent e)->this.makeMove());
        Timeline timeline = new Timeline(kf);
        timeline.setCycleCount(1);
        timeline.play();
    }
    // called by timeline in getMoveDelayed
    private void makeMove(){
        int[] move = this.getBestMove(this.makeBoardCopy(this.board), this.intelligenceLevel, this.playerColor);
        this.board.handleComputerMove(move,this.playerColor);
        if (move[0] == -1) { // no possible move
            this.referee.changePlayerTurns(false);
        } else {
            this.referee.changePlayerTurns(true);
        }
    }
    public int getPlayerColor(){return this.playerColor;}
}
