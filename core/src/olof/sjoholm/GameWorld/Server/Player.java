package olof.sjoholm.GameWorld.Server;

import java.util.List;

import olof.sjoholm.Interfaces.ICard;

/**
 * Created by sjoholm on 05/11/16.
 */
public interface Player {

    void dealCards(List<ICard> cards);

    void getCards(OnCardsReceivedListener onCardsReceivedListener);

    interface OnCardsReceivedListener {

        void onCardsReceived(List<ICard> cards);
    }
}
