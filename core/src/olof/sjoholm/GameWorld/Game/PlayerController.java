package olof.sjoholm.GameWorld.Game;

import java.util.List;

import olof.sjoholm.Client.CardHandModel;
import olof.sjoholm.GameWorld.Actors.Actions;
import olof.sjoholm.GameWorld.Server.OnCardsReceivedListener;
import olof.sjoholm.GameWorld.Server.PlayerApi;
import olof.sjoholm.Interfaces.Action;
import olof.sjoholm.common.CardModel;

public class PlayerController {
    private final PlayerApi playerApi;
    private final CardHandModel cardHandModel;

    public PlayerController(PlayerApi playerApi, CardHandModel cardHandModel) {
        this.playerApi = playerApi;
        this.cardHandModel = cardHandModel;
    }

    public void dealCards(List<CardModel> cards) {
        playerApi.sendCards(cards);
        cardHandModel.clear();
        cardHandModel.addCardModels(cards);
    }

    public void getCards(OnCardsReceivedListener onCardsReceivedListener) {
        playerApi.getCards(onCardsReceivedListener);
    }

    public Action drawAction() {
        CardModel cardModel = cardHandModel.popTop();
        return Actions.fromModel(cardModel);
    }

    public boolean hasCards() {
        return cardHandModel.getCardModels().size() > 0;
    }
}
