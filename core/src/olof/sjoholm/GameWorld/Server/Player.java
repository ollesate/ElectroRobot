package olof.sjoholm.GameWorld.Server;

import java.util.List;

import olof.sjoholm.Interfaces.Action;

public interface Player {

    void dealCards(List<Action> cards);

    void getCards(OnCardsReceivedListener onCardsReceivedListener);

    boolean hasCards();

    Action popTopCard();

}
