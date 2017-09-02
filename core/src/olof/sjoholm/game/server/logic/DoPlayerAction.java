package olof.sjoholm.game.server.logic;

import com.badlogic.gdx.scenes.scene2d.Action;

import olof.sjoholm.game.server.objects.GameBoard;
import olof.sjoholm.game.shared.objects.PlayerToken;

public class DoPlayerAction extends Action {
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
