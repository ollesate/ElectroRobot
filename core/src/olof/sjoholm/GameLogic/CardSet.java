package olof.sjoholm.GameLogic;

import java.util.Collection;

/**
 * Created by sjoholm on 24/09/16.
 */

public class CardSet extends ListAdapter<Card> {

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean add(Card card) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public Card get(int index) {
        return null;
    }

    public Card[] getCards() {
        return null;
    }
}
