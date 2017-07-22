package olof.sjoholm.GameWorld.Actors;

import olof.sjoholm.Api.BoardAction;

public class PlayerAction {
    public final PlayerToken playerToken;
    public final BoardAction boardAction;

    public PlayerAction(PlayerToken playerToken, BoardAction boardAction) {
        this.playerToken = playerToken;
        this.boardAction = boardAction;
    }
}
