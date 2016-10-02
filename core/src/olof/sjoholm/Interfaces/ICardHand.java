package olof.sjoholm.Interfaces;

/**
 * Created by sjoholm on 27/09/16.
 */
public interface ICardHand {

    void createRandomCard(MovableToken movableToken);

    int size();

    ICard popTopCard();

    void clearAllCards();

}
