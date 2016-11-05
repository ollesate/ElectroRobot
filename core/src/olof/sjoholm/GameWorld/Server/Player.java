package olof.sjoholm.GameWorld.Server;

import java.util.List;

import olof.sjoholm.Interfaces.ICard;

public interface Player {

    void dealCards(List<ICard> cards);

    void getCards(OnCardsReceivedListener onCardsReceivedListener);

    boolean hasCards();

    ICard popTopCard();

    interface OnCardsReceivedListener {

        void onCardsReceived(List<ICard> cards);
    }
}
