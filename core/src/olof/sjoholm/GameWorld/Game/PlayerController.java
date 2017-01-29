package olof.sjoholm.GameWorld.Game;

import java.util.List;

import olof.sjoholm.Client.CardHandModel;
import olof.sjoholm.GameWorld.Actors.Actions;
import olof.sjoholm.GameWorld.Server.IPlayerApi;
import olof.sjoholm.GameWorld.Server.OnCardsReceivedListener;
import olof.sjoholm.GameWorld.Server.PlayerApi;
import olof.sjoholm.Interfaces.Action;
import olof.sjoholm.common.CardModel;

public class PlayerController {
    private int playerId;
    private final IPlayerApi playerApi;
    private final CardHandModel cardHandModel;

    public PlayerController(int playerId, IPlayerApi playerApi, CardHandModel cardHandModel) {
        this.playerId = playerId;
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

    public void receivedCards(List<CardModel> cards) {
        cardHandModel.clear();
        cardHandModel.addCardModels(cards);
    }

    public int getPlayerId() {
        return playerId;
    }
}
