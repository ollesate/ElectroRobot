package olof.sjoholm.Client;

import java.util.List;

import olof.sjoholm.GameWorld.Server.Player;
import olof.sjoholm.Interfaces.ActionCard;

/**
 * Created by sjoholm on 23/12/16.
 */

public class MessageDispatcher {
    private final PlayerGame playerGame;

    public MessageDispatcher(PlayerGame playerGame) {
        this.playerGame = playerGame;
    }

    public void startGame() {
        playerGame.startGame();
    }

    public void dealCards(List<ActionCard> list) {
        playerGame.dealCards(list);
    }

    public void getCards(Player.OnCardsReceivedListener onCardsReceivedListener) {
        playerGame.getCards(onCardsReceivedListener);
    }
}
