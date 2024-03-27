package othello;

import javafx.scene.input.MouseEvent;

public interface Player {
    public void handleMouseClick(MouseEvent e);
    public void setListeningForMove(boolean listeningForMove);
    public int getPlayerColor();
}
