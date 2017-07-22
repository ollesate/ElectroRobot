package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.scenes.scene2d.Action;

import olof.sjoholm.Api.BoardAction;

class ActionWrapper extends Action {
    private final PlayerToken playerToken;
    private final BoardAction boardAction;
    private final Action perform;
    private boolean added;

    public ActionWrapper(PlayerToken playerToken, BoardAction boardAction) {
        // TODO: Rename target action or similiar, or can we use add action here? No, dont think so
        this.playerToken = playerToken;
        this.boardAction = boardAction;
        perform = boardAction.perform(playerToken);
    }

    @Override
    public boolean act(float delta) {
        if (!added) {
            playerToken.addAction(perform);
            added = true;
        }
        return perform.act(delta);
    }
}
