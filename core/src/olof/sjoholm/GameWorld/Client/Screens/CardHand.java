package olof.sjoholm.GameWorld.Client.Screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.GameWorld.Actors.Cards.BaseCard;
import olof.sjoholm.GameWorld.Actors.Cards.MoveCard;
import olof.sjoholm.GameWorld.Actors.Cards.RotateCard;
import olof.sjoholm.GameWorld.Utils.Logger;
import olof.sjoholm.Interfaces.ICard;

public class CardHand extends Group {
    private final List<BaseCard> cardActors;
    private List<ICard> cards;
    private BaseCard selectedCard;

    public CardHand() {
        cardActors = new ArrayList<BaseCard>();
        setDebug(true);
    }

    public void showCards(List<ICard> cards) {
        Logger.d("showCards()");
        this.cards = cards;
        for (BaseCard cardActor : cardActors) {
            removeActor(cardActor);
        }
        cardActors.clear();

        for (int i = 0; i < cards.size(); i++) {
            ICard card = cards.get(i);
            BaseCard cardActor = null;

            if (card instanceof MoveCard) {
                cardActor = new MoveCardActor(card);
            } else if (card instanceof RotateCard) {
                cardActor = new RotateCardActor(card);
            } else {
                Logger.d("Warning: card was not handled " + card.getClass().getSimpleName());
            }
            cardActors.add(cardActor);
        }

        invalidatePlacement();
    }

    private void invalidatePlacement() {
        for (int i = 0; i < cardActors.size(); i++) {
            BaseCard cardActor = cardActors.get(i);
            if (cardActor != null) {
                cardActor.setX(i * 75f);
                addActor(cardActor);
            }
        }

        if (cardActors.size() > 0) {
            setWidth((cardActors.size() - 1) * 75f + cardActors.get(0).getWidth());
            setHeight(cardActors.get(0).getHeight());
        }
    }

    private void swap(BaseCard card1, BaseCard card2) {
        swapPosition(card1, card2);

        int swap1 = cardActors.indexOf(card1);
        int swap2 = cardActors.indexOf(card2);

        BaseCard tempActor = cardActors.get(swap1);
        cardActors.set(swap1, cardActors.get(swap2));
        cardActors.set(swap2, tempActor);

        ICard tempCard = cards.get(swap1);
        cards.set(swap1, cards.get(swap2));
        cards.set(swap2, tempCard);
    }

    private void swapPosition(BaseCard card1, BaseCard card2) {
        float x = card1.getX();
        float y = card1.getY();

        card1.setPosition(card2.getX(), card2.getY());
        card2.setPosition(x, y);
    }

    public void press(BaseCard card) {
        if (selectedCard != null) {
            swap(card, selectedCard);
            deselect();
        } else {
            select(card);
        }
    }

    public void deselect() {
        if (selectedCard != null) {
            selectedCard.unselect();
            selectedCard = null;
        }
    }

    private void select(BaseCard card) {
        selectedCard = card;
        selectedCard.select();
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        return super.hit(x, y, touchable);
    }

    public ICard selectNextCard() {
        return null;
    }

    public List<ICard> getCards() {
        return cards;
    }

    public void addCard(BaseCard card) {
        cardActors.add(card);
        invalidatePlacement();
    }
}
