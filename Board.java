package othello;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Board {
    private OthelloSquare[][] boardSquares;
    private Referee referee;
    private ArrayList<OthelloSquare> possibleMoves;
    private Pane pane;
    public Board(Pane pane){
        this.pane = pane;
        this.boardSquares = new OthelloSquare[10][10];
        this.possibleMoves = new ArrayList<>();
        this.createSquares(pane);
        this.setupStartingPieces();
    }
    public void createReferee(int whitePlayerMode, int blackPlayerMode){
        this.referee = new Referee(this.pane, this, whitePlayerMode, blackPlayerMode);
    }
    public void setupLabels(Label scoreLabel, Label turnLabel){
        this.referee.setupLabels(scoreLabel,turnLabel);
    }
    private void createSquares(Pane pane){
        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++){
                int x = i * Constants.SQUARE_WIDTH;
                int y = j * Constants.SQUARE_WIDTH;
                Color color;
                // Check if square is border square
                if (i == 0 || i == 9 || j == 0 || j == 9){
                    color = Color.DARKRED;
                } else{
                    color = Color.DARKGREEN;
                }
                this.boardSquares[i][j] = new OthelloSquare(x, y, pane, color);
            }
        }
    }
    private void setupStartingPieces(){
        this.boardSquares[4][4].addPiece(Constants.WHITE);
        this.boardSquares[4][5].addPiece(Constants.BLACK);
        this.boardSquares[5][4].addPiece(Constants.BLACK);
        this.boardSquares[5][5].addPiece(Constants.WHITE);
    }
    // called by SetupGame when the user wants to reset the game
    public void resetBoard(){
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                this.boardSquares[i][j].resetSquare();
            }
        }
        this.setupStartingPieces();
    }
    // method is called by HumanPlayers themselves when they receive a MouseEvent
    public void handlePlayerClick(double clickX, double clickY, int playerColor){
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                OthelloSquare square = this.boardSquares[i][j];
                // check if click location is inside the square
                if (clickX > square.getX() && clickX < square.getX() + Constants.SQUARE_WIDTH &&
                        clickY > square.getY() && clickY < square.getY() + Constants.SQUARE_WIDTH){
                    // check that square clicked is a possible move
                    for (OthelloSquare possibleSquare: this.possibleMoves){
                        if (square.getX() == possibleSquare.getX() && square.getY() == possibleSquare.getY()){
                            square.addPiece(playerColor);
                            // flip sandwiched pieces
                            for (int directionToCheck = 0; directionToCheck < 8; directionToCheck++) {
                                this.checkSandwich(square, playerColor, directionToCheck, true);
                            }
                            // change which player's turn it is
                            this.referee.changePlayerTurns(true);
                        }
                    }
                }
            }
        }
    }
    public boolean showPossibleMoves(int playerColor){
        // loop through every square
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                OthelloSquare currentSquare = this.boardSquares[i][j];
                if (currentSquare.getPieceColor() == -1) { // square must be empty for it to be valid
                    // check sandwich in every possible direction
                    for (int directionToCheck = 0; directionToCheck < 8; directionToCheck++) {
                        this.checkSandwich(currentSquare, playerColor, directionToCheck, false);
                    }
                }
            }
        }
        return this.possibleMoves.size() > 0; // return if there are possible moves
    }
    /*
     Called before every move to highlight possible moves and after every move to flip pieces. Also updates
     this.possibleMoves variable to be used in other methods
     */
    public void checkSandwich(OthelloSquare currentSquare, int playerColor, int directionToCheck, boolean needsToFlipPieces){
        OthelloSquare initialSquare = currentSquare;
        boolean middleSquareExists = false; // to make sure there is a square of the opposite color being sandwiched
        // keep track of the chain of squares looped through that may need to be flipped at the end
        ArrayList<OthelloSquare> potentialSquaresToFlip = new ArrayList<>();
        /*
         Recursive loop that passes on the task of finding a sandwich to the next square in the
         directionToCheck. The loop stops when a square either has no piece on it or is the color
         of the original piece and there is a sandwich.
         */
        while(true){
            double currentSquareX = currentSquare.getX();
            double currentSquareY = currentSquare.getY();
            // get next square in direction
            switch (directionToCheck){
                case Constants.UP:
                    currentSquare = this.boardSquares[(int) (currentSquareX/Constants.SQUARE_WIDTH)]
                            [(int) (currentSquareY/Constants.SQUARE_WIDTH) - 1];
                    break;
                case Constants.DOWN:
                    currentSquare = this.boardSquares[(int) (currentSquareX/Constants.SQUARE_WIDTH)]
                            [(int) (currentSquareY/Constants.SQUARE_WIDTH) + 1];
                    break;
                case Constants.RIGHT:
                    currentSquare = this.boardSquares[(int) (currentSquareX/Constants.SQUARE_WIDTH) + 1]
                            [(int) (currentSquareY/Constants.SQUARE_WIDTH)];
                    break;
                case Constants.LEFT:
                    currentSquare = this.boardSquares[(int) (currentSquareX/Constants.SQUARE_WIDTH) - 1]
                            [(int) (currentSquareY/Constants.SQUARE_WIDTH)];
                    break;
                case Constants.UP_RIGHT:
                    currentSquare = this.boardSquares[(int) (currentSquareX/Constants.SQUARE_WIDTH) + 1]
                            [(int) (currentSquareY/Constants.SQUARE_WIDTH) - 1];
                    break;
                case Constants.UP_LEFT:
                    currentSquare = this.boardSquares[(int) (currentSquareX/Constants.SQUARE_WIDTH) - 1]
                            [(int) (currentSquareY/Constants.SQUARE_WIDTH) - 1];
                    break;
                case Constants.DOWN_RIGHT:
                    currentSquare = this.boardSquares[(int) (currentSquareX/Constants.SQUARE_WIDTH) + 1]
                            [(int) (currentSquareY/Constants.SQUARE_WIDTH) + 1];
                    break;
                case Constants.DOWN_LEFT:
                    currentSquare = this.boardSquares[(int) (currentSquareX/Constants.SQUARE_WIDTH) - 1]
                            [(int) (currentSquareY/Constants.SQUARE_WIDTH) + 1];
                    break;
            }
            if (currentSquare.getPieceColor() == -1){ // no piece is on square
                return;
            } else if (currentSquare.getPieceColor() == playerColor && middleSquareExists){
                //is only a sandwich if the ends are the same color and there is at least one square in between

                if (!needsToFlipPieces){
                    this.addToPossibleMoves(initialSquare);
                    initialSquare.highlightValidMove(); // visually show that this square is a valid move
                } else {
                    for (OthelloSquare square : potentialSquaresToFlip){
                        square.flipPiece();
                    }
                }
                return;
            } else if (currentSquare.getPieceColor() != playerColor){
                // while loop keeps passing on the task to other pieces
                // as long as the piece color is the opponents color
                middleSquareExists = true;
                if (needsToFlipPieces){
                    potentialSquaresToFlip.add(currentSquare);
                }
            }

        }
    }
    // called in referee when players switch turns and the possible moves need to be reset
    public void resetPossibleMoves(){
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                this.boardSquares[i][j].unHighlightMove();
            }
        }
        this.possibleMoves = new ArrayList<>();
    }
    /*
     called by this.checkSandwich when it finds a valid move. This method adds move to
     this.possibleMoves and makes sure it has not already been added
     */
    private void addToPossibleMoves(OthelloSquare squareToAdd){
        for (OthelloSquare possibleSquare : this.possibleMoves){
            if (possibleSquare.getX() == squareToAdd.getX() && possibleSquare.getY() == squareToAdd.getY()){
                return;
            }
        }
        this.possibleMoves.add(squareToAdd);
    }
    // called by Referee in its updateScore method and in its gameOver method (find winning player)
    public int[] getScore(){
        int whiteScore = 0;
        int blackScore = 0;
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                if (this.boardSquares[i][j].getPieceColor() == Constants.WHITE){
                    whiteScore++;
                } else if (this.boardSquares[i][j].getPieceColor() == Constants.BLACK){
                    blackScore++;
                }
            }
        }
        int[] score = new int[2];
        score[0] = whiteScore;
        score[1] = blackScore;
        return score;
    }
    /*
     called by ComputerPlayer when it wants to move. No checking if move is valid because
     ComputerPlayer already does this and would never try to do an invalid move.
     */
    public void handleComputerMove(int[] move, int playerColor){
        int moveX = move[0] * Constants.SQUARE_WIDTH;
        int moveY = move[1] * Constants.SQUARE_WIDTH;
        OthelloSquare moveSquare = null;
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                OthelloSquare boardSquare = this.boardSquares[i][j];
                if (boardSquare.getX() == moveX && boardSquare.getY() == moveY){
                    boardSquare.addPiece(playerColor);
                    moveSquare = boardSquare;
                }
            }
        }
        // flip pieces
        if (moveSquare != null) {
            for (int directionToCheck = 0; directionToCheck < 8; directionToCheck++) {
                this.checkSandwich(moveSquare, playerColor, directionToCheck, true);
            }
        }

    }
    public OthelloSquare[][] getBoardSquares(){return this.boardSquares;}
    public Referee getReferee(){return this.referee;}
}
