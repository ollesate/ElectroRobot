package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.GameLogic.Card;
import olof.sjoholm.GameLogic.ICard;
import olof.sjoholm.GameLogic.ICardHand;

/**
 * Created by sjoholm on 27/09/16.
 */

public class CardHand extends Group implements ICardHand {
    private final List<Card> cards;

    public CardHand() {
        cards = new ArrayList<Card>();
    }

    @Override
    public void createRandomCard(MovableToken movableToken) {
        addCard(new Card(movableToken));
    }

    private void addCard(Card card) {
        cards.add(card);

        card.setX(cards.indexOf(card) * 45);

        addActor(card);
    }

    @Override
    public int size() {
        return cards.size();
    }

    @Override
    public ICard popTopCard() {
        if (cards.size() > 0) {
            ICard card = cards.get(0);
            cards.remove(card);
            return card;
        }
        return null;
    }
}
