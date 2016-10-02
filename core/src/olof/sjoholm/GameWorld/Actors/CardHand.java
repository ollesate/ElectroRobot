package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Queue;

import olof.sjoholm.GameWorld.Actors.Cards.BaseCard;
import olof.sjoholm.GameWorld.Actors.Cards.MoveCard;
import olof.sjoholm.GameWorld.Actors.Cards.RotateCard;
import olof.sjoholm.GameWorld.Utils.Logger;
import olof.sjoholm.Interfaces.ICard;
import olof.sjoholm.Interfaces.ICardHand;
import olof.sjoholm.Interfaces.MovableToken;

/**
 * Created by sjoholm on 27/09/16.
 */

class CardHand extends Group implements ICardHand {
    private final Queue<BaseCard> playableCards;

    CardHand() {
        playableCards = new Queue<BaseCard>();
        setScale(0.5f);
    }

    @Override
    public void createRandomCard(MovableToken movableToken) {
        BaseCard card;
        if (Math.random() > 0.25f) {
            card = new MoveCard(movableToken);
        } else {
            card = new RotateCard(movableToken);
        }
        addCard(card);
    }

    private void addCard(BaseCard card) {
        playableCards.addLast(card);

        int index = playableCards.size - 1;

        card.setX(index * 45);

        addActor(card);
    }

    @Override
    public int size() {
        return playableCards.size;
    }

    @Override
    public ICard popTopCard() {
        if (playableCards.size > 0) {
            ICard card = playableCards.removeFirst();;
            return card;
        }
        return null;
    }

    @Override
    public void clearAllCards() {
        playableCards.clear();
        clearChildren();
    }
}
