package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.Interfaces.ICard;
import olof.sjoholm.Interfaces.ICardHand;

/**
 * Created by sjoholm on 27/09/16.
 */

public class CardHand extends Group implements ICardHand {
    private final List<BaseCard> cards;

    public CardHand() {
        cards = new ArrayList<BaseCard>();
        setScale(0.5f);
    }

    @Override
    public void createRandomCard(olof.sjoholm.Interfaces.MovableToken movableToken) {
        BaseCard card;
        if (Math.random() > 0.75f) {
            card = new MoveCard(movableToken);
        } else {
            card = new RotateCard(movableToken);
        }
        addCard(card);
    }

    private void addCard(BaseCard card) {
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
