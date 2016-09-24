package olof.sjoholm.GameLogic;

import olof.sjoholm.GameWorld.Actors.MovableToken;

/**
 * Created by sjoholm on 27/09/16.
 */
public class Player {
    private ICardHand cardHand;
    private MovableToken movableToken;

    public Player(MovableToken movableToken, ICardHand cardHand) {
        this.cardHand = cardHand;
        this.movableToken = movableToken;
    }

    public void dealFiveCards() {
        for (int i = 0; i < 5; i++) {
            cardHand.createRandomCard(movableToken);
        }
    }

    public boolean hasCard() {
        return cardHand.size() > 0;
    }

    public ICard popTopCard() {
        return cardHand.popTopCard();
    }

}
