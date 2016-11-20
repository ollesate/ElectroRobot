package olof.sjoholm.GameWorld.Game;

import java.util.List;

import olof.sjoholm.Interfaces.ICard;

public class MyCardHand extends HandView {

    public MyCardHand() {

    }

    public void addCard(SelectableCard card) {
        super.addCard(card.getView());
    }

    public List<ICard> getCards() {
        return null;
    }
}
