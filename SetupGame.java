package othello;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class SetupGame {
    private Pane root;
    private Board board;
    private boolean gameInProgress;
    public SetupGame(BorderPane pane){
        this.root = new Pane();
        pane.setCenter(this.root);
        pane.setFocusTraversable(false);
        this.board = new Board(this.root);

        this.root.setStyle("-fx-background-color: #000000;");
        this.root.setFocusTraversable(true);
        this.gameInProgress = false;
    }
    // called by Controls when apply settings button is clicked
    public void handleApplySettings(int whitePlayerMode, int blackPlayerMode,
                                    Label scoreLabel, Label turnLabel){
        if (!this.gameInProgress) {
            this.board.createReferee(whitePlayerMode, blackPlayerMode);
            this.board.setupLabels(scoreLabel, turnLabel);
            this.gameInProgress = true;
        } else {
            this.board.getReferee().createPlayers(whitePlayerMode,blackPlayerMode);
        }
    }
    // called by Controls when reset button is clicked
    public void resetGame(){
        this.board.getReferee().resetGame();
    }
}
