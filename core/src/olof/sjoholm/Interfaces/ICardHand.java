package olof.sjoholm.Interfaces;

public interface ICardHand {

    void createRandomCard(MovableToken movableToken);

    int size();

    ICard popTopCard();

    void clearAllCards();

}
