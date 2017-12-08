package olof.sjoholm.game.server.logic;

import olof.sjoholm.game.shared.logic.cards.BoardAction;
import olof.sjoholm.game.server.server_logic.Player;

public class PlayerAction {
    public final Player player;
    public final BoardAction boardAction;

    public PlayerAction(Player player, BoardAction boardAction) {
        this.player = player;
        this.boardAction = boardAction;
    }
}
