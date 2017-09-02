package olof.sjoholm.game.logic;

import olof.sjoholm.net.Player;

public class PlayerAction {
    public final Player player;
    public final BoardAction boardAction;

    public PlayerAction(Player player, BoardAction boardAction) {
        this.player = player;
        this.boardAction = boardAction;
    }
}
