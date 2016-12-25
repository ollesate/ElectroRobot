package olof.sjoholm.Client.stages;

import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.List;

import olof.sjoholm.Client.CardHandModel;
import olof.sjoholm.common.CardModel;


public class HandStage extends Stage {
    private final CardHandTable cardHandTable;
    private final CardHandModel cardHandModel;

    public HandStage() {
        cardHandModel = new CardHandModel();
        cardHandTable = new CardHandTable(cardHandModel);
        addActor(cardHandTable);
    }

    public void dealCards(List<CardModel> list) {
        cardHandModel.clear();
        cardHandModel.addCardModels(list);
    }

    public List<CardModel> getCards() {
        return cardHandModel.getCardModels();
    }

    private static class CardHandController {

        public CardHandController() {

        }

        public void addCard(CardModel cardModel) {

        }

    }

}
