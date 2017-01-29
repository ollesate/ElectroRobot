package olof.sjoholm.Utils;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.Models.CardModel;
import olof.sjoholm.Utils.CardModelFactory;


public class CardUtil {

    public static List<CardModel> createRandomCards(int count) {
        List<CardModel> cards = new ArrayList<CardModel>();
        for (int i = 0; i < count; i++) {
            cards.add(createRandomCard());
        }
        return cards;
    }

    public static CardModel createRandomCard() {
        return CardModelFactory.getInstance().createRandom();
    }

}
