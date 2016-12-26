package olof.sjoholm.Interfaces;

public interface ICardHand {

    void createRandomCard(MovableToken movableToken);

    int size();

    Action popTopCard();

    void clearAllCards();

}
