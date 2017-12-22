package olof.sjoholm.game.server.logic;

import java.util.List;

import olof.sjoholm.game.shared.logic.cards.BoardAction;
import olof.sjoholm.game.shared.objects.PlayerToken;

public class PlaySet {
    private final PlayerToken token;
    private final List<BoardAction> cards;

    public PlaySet(PlayerToken token, List<BoardAction> cards) {
        this.token = token;
        this.cards = cards;
    }

    public boolean hasRound(int round) {
        return cards.size() > round;
    }

    public BoardAction getRound(int round) {
        return cards.get(round);
    }

    public PlayerToken getPlayerToken() {
        return token;
    }
}
