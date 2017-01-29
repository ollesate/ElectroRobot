package olof.sjoholm.Interfaces;

import java.util.List;

public interface Player {

    void dealCards(List<Action> cards);

    void getCards(OnCardsReceivedListener onCardsReceivedListener);

    boolean hasCards();

    Action popTopCard();

}
