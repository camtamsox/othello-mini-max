package othello;

import javafx.scene.layout.BorderPane;

public class PaneOrganizer {
    private BorderPane root;
    public PaneOrganizer(){
        this.root = new BorderPane();
        SetupGame game = new SetupGame(this.root);
        Controls controls = new Controls(game);
        this.root.setRight(controls.getPane());
    }
    public BorderPane getRoot(){return this.root;}
}
