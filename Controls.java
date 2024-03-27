package othello;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/*
 * Controls sets up the GUI for the game menu, allowing the user to pick the
 * game modes and to start and track games. Controls holds a one-way reference
 * to the Game, so it can control the Game's player settings.
 */
public class Controls {

  private SetupGame game;

  private VBox controlsPane;

  // Arrays for player buttons. Each button is checked to see if it is
  // selected when the user starts each game.
  private RadioButton[][] playerButtons;
  private Label scoreLabel;
  private Label turnLabel;
  public Controls(SetupGame othello) {
    this.game = othello;

    this.controlsPane = new VBox();
    this.controlsPane.setPadding(new Insets(10));
    this.controlsPane.setSpacing(30);
    this.controlsPane.setAlignment(Pos.CENTER);

    this.setupInstructions();
    this.setupScore();
    this.setupTurns();

    this.setupMenu();
    this.setupGameButtons();
  }

  public Pane getPane() {
    return this.controlsPane;
  }

  private void setupInstructions() {
    Label instructionsLabel = new Label(
        "Select options, then press Apply Settings");
    this.controlsPane.getChildren().add(instructionsLabel);
    this.scoreLabel = instructionsLabel;
  }
  private void setupScore() {
    Label scoreLabel = new Label(
            "White:  Black:");
    this.controlsPane.getChildren().add(scoreLabel);
    this.scoreLabel = scoreLabel;
  }
  private void setupTurns() {
    Label turnLabel = new Label(
            "Black to move");
    this.controlsPane.getChildren().add(turnLabel);
    this.turnLabel = turnLabel;
  }
  /*
   * Sets up the two halves of the player mode menu.
   */
  private void setupMenu() {
    this.playerButtons = new RadioButton[2][4];

    HBox playersMenu = new HBox();
    playersMenu.setSpacing(10);
    playersMenu.setAlignment(Pos.CENTER);
    playersMenu.getChildren().addAll(this.playerMenu(Constants.WHITE),
        this.playerMenu(Constants.BLACK));

    this.controlsPane.getChildren().add(playersMenu);
  }

  /*
   * Provides the menu for each player mode.
   */
  private VBox playerMenu(int player) {
    VBox playerMenu = new VBox();
    playerMenu.setPrefWidth(Constants.CONTROLS_PANE_WIDTH / 2);
    playerMenu.setSpacing(10);
    playerMenu.setAlignment(Pos.CENTER);

    // Player color.
    String playerColor = "White";
    if (player == Constants.BLACK) {
      playerColor = "Black";
    }
    Label playerName = new Label(playerColor);

    // Radio button group for player mode.
    ToggleGroup toggleGroup = new ToggleGroup();

    // Human player.
    RadioButton humanButton = new RadioButton("Human         ");
    humanButton.setToggleGroup(toggleGroup);
    humanButton.setSelected(true);
    this.playerButtons[player][0] = humanButton;

    // Computer Players.
    for (int i = 1; i < 4; i++) {
      RadioButton computerButton = new RadioButton("Computer " + i + "  ");
      computerButton.setToggleGroup(toggleGroup);
      this.playerButtons[player][i] = computerButton;

      // Enables deterministic button when Computer player selected.

    }

    // Checkbox for deterministic play. Only enabled when computer player
    // selected. This is ONLY for Bells&Whistles

    // Visually add the player mode menu.
    playerMenu.getChildren().add(playerName);
    for (RadioButton rb : this.playerButtons[player]) {
      playerMenu.getChildren().add(rb);
    }

    return playerMenu;
  }

  private void setupGameButtons() {
    Button applySettingsButton = new Button("Apply Settings");
    applySettingsButton.setOnAction((ActionEvent e)->this.applySettings(e));
    applySettingsButton.setFocusTraversable(false);

    Button resetButton = new Button("Reset");
    resetButton.setOnAction((ActionEvent e)-> this.resetHandler(e));
    resetButton.setFocusTraversable(false);

    Button quitButton = new Button("Quit");
    quitButton.setOnAction((ActionEvent e)->Platform.exit());
    quitButton.setFocusTraversable(false);

    this.controlsPane.getChildren().addAll(applySettingsButton, resetButton,
        quitButton);
  }

  /*
   * Handler for Apply Settings button.
   */

    public void applySettings(ActionEvent e) {

      // Determine game play mode for each player.
      int whitePlayerMode = 0;
      int blackPlayerMode = 0;
      for (int player = 0; player < 2; player++) {
        for (int mode = 0; mode < 4; mode++) {
          if (this.playerButtons[player][mode].isSelected()) {
            if (player == Constants.WHITE) {
              whitePlayerMode = mode;
            } else {
              blackPlayerMode = mode;
            }
          }
        }
      }
      this.game.handleApplySettings(whitePlayerMode, blackPlayerMode, this.scoreLabel,this.turnLabel);

    }
    public void resetHandler(ActionEvent e){
      this.game.resetGame();
    }



}
