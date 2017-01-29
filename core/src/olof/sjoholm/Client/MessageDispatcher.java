package olof.sjoholm.Client;

import java.util.List;

import olof.sjoholm.Models.CardModel;


public class MessageDispatcher {
    private final PlayerGame playerGame;

    public MessageDispatcher(PlayerGame playerGame) {
        this.playerGame = playerGame;
    }

    public void startGame() {
        playerGame.startGame();
    }

    public void dealCards(List<CardModel> list) {
        playerGame.dealCards(list);
    }

    public List<CardModel> getCards() {
        return playerGame.getCards();
    }
}
