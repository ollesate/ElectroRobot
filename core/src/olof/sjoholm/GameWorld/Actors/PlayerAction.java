package olof.sjoholm.GameWorld.Actors;

import olof.sjoholm.Api.BoardAction;
import olof.sjoholm.Net.Server.Player;

public class PlayerAction {
    public final Player player;
    public final BoardAction boardAction;

    public PlayerAction(Player player, BoardAction boardAction) {
        this.player = player;
        this.boardAction = boardAction;
    }
}
