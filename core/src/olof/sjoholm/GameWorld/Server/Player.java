package olof.sjoholm.GameWorld.Server;

import java.util.List;

import olof.sjoholm.Interfaces.ActionCard;

public interface Player {

    void dealCards(List<ActionCard> cards);

    void getCards(OnCardsReceivedListener onCardsReceivedListener);

    boolean hasCards();

    ActionCard popTopCard();

    interface OnCardsReceivedListener {

        void onCardsReceived(List<ActionCard> cards);
    }
}
