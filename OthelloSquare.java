package othello;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class OthelloSquare {
    private Circle piece;
    private double[] center;
    private Rectangle rectangle;
    private Pane pane;
    private int pieceColor;
    private Color defaultColor;
    public OthelloSquare(double x, double y, Pane pane, Color color){
        this.rectangle = new Rectangle(x, y, Constants.SQUARE_WIDTH-1,Constants.SQUARE_WIDTH-1);
        this.rectangle.setFill(color);
        this.defaultColor = color;
        this.pane = pane;
        this.pane.getChildren().add(this.rectangle);

        this.center = new double[2]; // center of square
        this.center[0] = x + Constants.SQUARE_WIDTH/2.;
        this.center[1] = y + Constants.SQUARE_WIDTH/2.;

        this.pieceColor = -1; // no piece here yet
    }
    public void addPiece(int player){
        if (this.piece == null) {
            this.pieceColor = player;
            this.piece = new Circle(this.center[0], this.center[1], Constants.PIECE_RADIUS);
            if (player == Constants.WHITE) {
                this.piece.setFill(Color.WHITE);
            } else {
                this.piece.setFill(Color.BLACK);
            }
            this.pane.getChildren().add(this.piece);
        }
    }
    public void flipPiece(){
        if (this.piece != null) {
            if (this.piece.getFill() == Color.WHITE) {
                this.piece.setFill(Color.BLACK);
                this.pieceColor = Constants.BLACK;
            } else {
                this.piece.setFill(Color.WHITE);
                this.pieceColor = Constants.WHITE;
            }
        }
    }
    public void resetSquare(){
        if (this.piece != null){
            this.pane.getChildren().remove(this.piece);
            this.piece = null;
            this.pieceColor = -1;
        }
    }
    public double getX(){return this.rectangle.getX();}
    public double getY(){return this.rectangle.getY();}
    public void highlightValidMove(){
        this.rectangle.setFill(Color.GRAY);
    }
    public void unHighlightMove(){
        this.rectangle.setFill(this.defaultColor);
    }
    public int getPieceColor(){return this.pieceColor;}
}
