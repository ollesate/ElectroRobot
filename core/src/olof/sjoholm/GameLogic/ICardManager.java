package olof.sjoholm.GameLogic;

import olof.sjoholm.GameWorld.Actors.Direction;
import olof.sjoholm.GameWorld.Actors.ICardView;

/**
 * Created by sjoholm on 25/09/16.
 */

public interface ICardManager {

    void addCard(int value, Direction direction);

    void removeCard(Card card);

    void removeAllCards();

    void selectCard(Card card);

    void selectCard(int index);

    void addAllCards(Card[] cards);

    ICardView getCard(int index);
}
