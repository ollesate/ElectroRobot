package olof.sjoholm.GameLogic;

import olof.sjoholm.Interfaces.*;
import olof.sjoholm.Interfaces.ActionCard;

public class ConcretePlayer {
    private olof.sjoholm.Interfaces.ICardHand cardHand;
    private MovableToken movableToken;

    public ConcretePlayer(MovableToken movableToken, olof.sjoholm.Interfaces.ICardHand cardHand) {
        this.cardHand = cardHand;
        this.movableToken = movableToken;
    }

    public void dealFiveCards() {
        cardHand.clearAllCards();
        for (int i = 0; i < 5; i++) {
            cardHand.createRandomCard(movableToken);
        }
    }

    public boolean hasCard() {
        return cardHand.size() > 0;
    }

    public ActionCard popTopCard() {
        return cardHand.popTopCard();
    }

}
