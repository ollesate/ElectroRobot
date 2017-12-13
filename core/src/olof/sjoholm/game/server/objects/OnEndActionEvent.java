package olof.sjoholm.game.server.objects;


import com.badlogic.gdx.scenes.scene2d.Event;

import olof.sjoholm.game.server.server_logic.Player;
import olof.sjoholm.game.shared.logic.cards.BoardAction;

public class OnEndActionEvent extends Event {
    public final BoardAction boardAction;
    public final Player player;

    public OnEndActionEvent(Player player, BoardAction boardAction) {
        this.player = player;
        this.boardAction = boardAction;
    }
}
