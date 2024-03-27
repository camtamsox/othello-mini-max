package othello;

import java.util.ArrayList;

public class BoardCopy {
    private int[][] boardState;
    private ArrayList<int[]> possibleMoves;
    public BoardCopy(Board board){
        OthelloSquare[][] boardSquares = board.getBoardSquares();
        this.boardState = new int[10][10]; // includes border squares
        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++){
                OthelloSquare currentSquare = boardSquares[i][j];
                this.boardState[i][j] = currentSquare.getPieceColor();
            }
        }
        this.possibleMoves = new ArrayList<>();
    }
    public BoardCopy(BoardCopy board){
        int[][] oldBoardState = board.getBoardState();
        this.boardState = new int[10][10]; // includes border squares
        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++){
                this.boardState[i][j] = oldBoardState[i][j];
            }
        }
        this.possibleMoves = new ArrayList<>();
    }
    public ArrayList<int[]> getPossibleMoves(int playerColor){
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                int currentPieceValue = this.boardState[i][j];
                if (currentPieceValue == -1) { // square must be empty for it to be valid
                    // check sandwich in every possible direction
                    for (int directionToCheck = 0; directionToCheck < 8; directionToCheck++) {
                        int[] currentSquareCoords = new int[2];
                        currentSquareCoords[0] = i;
                        currentSquareCoords[1] = j;

                        // checks for sandwiches and adds possible moves to this.possibleMoves
                        this.checkSandwich(currentSquareCoords, playerColor, directionToCheck, false);
                    }
                }
            }
        }
        return this.possibleMoves;
    }
    /*
        This checkSandwich method is similar to the Board checkSandwich method. The difference
        is that this method can't use OthelloSquares (because BoardCopy's boardState is an
        array of integers and never updates graphically). So, this method has to do everything
        with integers.
     */
    private void checkSandwich(int[] currentSquareCoords, int playerColor, int directionToCheck, boolean needsToFlipPieces){
        int[] initialSquareCoords = new int[2];
        initialSquareCoords[0] = currentSquareCoords[0];
        initialSquareCoords[1] = currentSquareCoords[1];
        boolean middleSquareExists = false; // to make sure there is a square of the opposite color being sandwiched
        // keep track of the chain of squares looped through that may need to be flipped at the end
        ArrayList<int[]> potentialSquaresToFlip = new ArrayList<>();
        /*
         Recursive loop that passes on the task of finding a sandwich to the next square in the
         directionToCheck. The loop stops when a square either has no piece on it or is the color
         of the original piece and there is a sandwich.
         */
        while(true){
            // get next square in direction
            switch (directionToCheck){
                case Constants.UP:
                    currentSquareCoords[1]-=1;
                    break;
                case Constants.DOWN:
                    currentSquareCoords[1]+=1;
                    break;
                case Constants.RIGHT:
                    currentSquareCoords[0]+=1;
                    break;
                case Constants.LEFT:
                    currentSquareCoords[0]-=1;
                    break;
                case Constants.UP_RIGHT:
                    currentSquareCoords[0]+=1;
                    currentSquareCoords[1]-=1;
                    break;
                case Constants.UP_LEFT:
                    currentSquareCoords[0]-=1;
                    currentSquareCoords[1]-=1;
                    break;
                case Constants.DOWN_RIGHT:
                    currentSquareCoords[0]+=1;
                    currentSquareCoords[1]+=1;
                    break;
                case Constants.DOWN_LEFT:
                    currentSquareCoords[0]-=1;
                    currentSquareCoords[1]+=1;
                    break;
            }
            int currentPieceColor = this.boardState[currentSquareCoords[0]][currentSquareCoords[1]];
            if (currentPieceColor == -1){ // no piece is on square so no sandwich
                return;
            } else if (currentPieceColor == playerColor && middleSquareExists){
                //is only a sandwich if the ends are the same color and there is at least one square in between

                if (!needsToFlipPieces){
                    // add to this.possible moves only if move is unique
                    boolean moveIsUnique = true;
                    for (int[] move : this.possibleMoves){
                        if (move[0] == initialSquareCoords[0] && move[1] == initialSquareCoords[1]){
                            moveIsUnique = false;
                        }
                    }
                    if (moveIsUnique) {
                        this.possibleMoves.add(initialSquareCoords);
                    }
                } else {
                    for (int[] squareCoords : potentialSquaresToFlip){
                        this.flipPiece(squareCoords);
                    }
                }
                return;
            } else if (currentPieceColor != playerColor){
                // while loop keeps passing on the task to other pieces
                // as long as the piece color is the opponents color
                middleSquareExists = true;
                if (needsToFlipPieces){
                    potentialSquaresToFlip.add(new int [] {currentSquareCoords[0],currentSquareCoords[1]});
                }
            }

        }
    }
    private void flipPiece(int[] pieceCoords){
        int pieceColor = this.boardState[pieceCoords[0]][pieceCoords[1]];
        if (pieceColor == Constants.WHITE){
            this.boardState[pieceCoords[0]][pieceCoords[1]] = Constants.BLACK;
        } else {
            this.boardState[pieceCoords[0]][pieceCoords[1]] = Constants.WHITE;
        }
    }
    public void makeMove(int[] pieceCoords_, int playerColor){
        int[] pieceCoords = new int[2];
        pieceCoords[0] = pieceCoords_[0];
        pieceCoords[1] = pieceCoords_[1];
        if (pieceCoords[0] != -1) { // make sure move is possible
            this.boardState[pieceCoords[0]][pieceCoords[1]] = playerColor;
            for (int direction = 0; direction < 8; direction++) {
                pieceCoords = new int[2];
                pieceCoords[0] = pieceCoords_[0];
                pieceCoords[1] = pieceCoords_[1];
                this.checkSandwich(pieceCoords, playerColor, direction, true);
            }
        }
    }

    public int[][] getBoardState() {
        return this.boardState;
    }
}
