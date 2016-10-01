package olof.sjoholm.GameWorld.Utils;

import java.util.List;

import olof.sjoholm.Interfaces.Callback;
import olof.sjoholm.Interfaces.ICard;

/**
 * Created by sjoholm on 27/09/16.
 */

public class CardSequence {

    public static void playCards(final List<ICard> cards, final Callback onFinished) {
        if (cards.size() > 0) {
            final ICard card = cards.get(0);
            card.playCard(new Callback() {
                @Override
                public void callback() {
                    cards.remove(card);
                    playCards(cards, onFinished);
                }
            });
        } else {
            onFinished.callback();
        }
    }

}
