package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.scenes.scene2d.Action;

class DoPlayerAction extends Action {
    private final GameBoard gameBoard;
    private final PlayerAction playerAction;
    private Action action;

    public DoPlayerAction(GameBoard gameBoard, PlayerAction playerAction) {
        this.gameBoard = gameBoard;
        this.playerAction = playerAction;
    }

    @Override
    public boolean act(float delta) {
        if (action == null) {
            PlayerToken playerToken = gameBoard.getPlayerToken(playerAction.player);
            if (playerToken != null) {
                action = playerAction.boardAction.getAction(playerToken);
            } else {
                return true;
            }
        }
        return action.act(delta);
    }
}
