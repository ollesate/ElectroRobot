package olof.sjoholm.game.server.objects;


import com.badlogic.gdx.scenes.scene2d.Event;

import olof.sjoholm.game.server.server_logic.Player;
import olof.sjoholm.game.shared.logic.cards.BoardAction;
import olof.sjoholm.game.shared.objects.PlayerToken;

public class OnStartActionEvent extends Event {
    public final BoardAction boardAction;
    public final PlayerToken playerToken;

    public OnStartActionEvent(PlayerToken token, BoardAction boardAction) {
        this.playerToken = token;
        this.boardAction = boardAction;
    }
}
