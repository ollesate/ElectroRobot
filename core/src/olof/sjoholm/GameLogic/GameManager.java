package olof.sjoholm.GameLogic;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.GameWorld.Game.PlayerController;
import olof.sjoholm.GameWorld.IGameStage;
import olof.sjoholm.GameWorld.Server.GameApi;
import olof.sjoholm.GameWorld.Server.OnCardsReceivedListener;
import olof.sjoholm.GameWorld.Server.Player;
import olof.sjoholm.GameWorld.Utils.CardUtil;
import olof.sjoholm.GameWorld.Utils.Logger;
import olof.sjoholm.Interfaces.Callback;
import olof.sjoholm.Interfaces.Action;
import olof.sjoholm.Interfaces.IGameBoard;
import olof.sjoholm.common.CardModel;

/**
 * @author sjoholm
 */
public class GameManager {
    private IGameBoard gameBoard;
    private GameApi gameApi;

    private IGameStage gameStage;
    private List<PlayerController> players;
    private final CardManager cardManager;
    private final Object fetchingCardsMutex = new Object();

    public GameManager(IGameStage gameStage, GameApi gameApi) {
        this.gameStage = gameStage;
        this.gameBoard = gameStage.getGameBoard();
        this.gameApi = gameApi;
        cardManager = new CardManager(gameBoard);
    }

    private void setupTokens() {
        for (PlayerController player : players) {
            gameBoard.spawnToken(player);
        }
    }

    public void startGame(List<PlayerController> players) {
        gameApi.startGame();
        this.players = players;
        cardManager.setPlayers(players);
        Logger.d("startGame() with " + players.size() + " players");
        setupTokens();
        new Thread(new Runnable() {
            @Override
            public void run() {
                startTurn();
            }
        }).start();
    }

    private void startTurn() {
        Logger.d("startTurn()");
        dealCards();
        givePlayersTime();
        fetchCardsFromPlayers();
        waitForPlayerResponses();
        playRound();
    }

    private void fetchCardsFromPlayers() {
        Logger.d("fetchCardsFromPlayers()");
        synchronized (fetchingCardsMutex) {

            final List<PlayerController> readyList = new ArrayList<PlayerController>();
            for (final PlayerController player : players) {
                player.getCards(new OnCardsReceivedListener() {
                    @Override
                    public void onCardsReceived(List<CardModel> cards) {
                        player.receivedCards(cards);
                        Logger.d("Received response!");
                        synchronized (readyList) {
                            readyList.add(player);
                        }
                        if (readyList.size() == players.size()) {
                            Logger.d("Everyone is ready, notify!");
                            synchronized (fetchingCardsMutex) {
                                fetchingCardsMutex.notify();
                            }
                        }
                    }
                });
            }
        }
    }

    private void givePlayersTime() {
        Logger.d("Lets wait for players to make their turn");
        float countDownTime = 10f;
        gameStage.startCountdown(countDownTime);
        try {
            Thread.sleep((long)(countDownTime * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void waitForPlayerResponses() {
        Logger.d("waitForPlayerResponses()");
        synchronized (fetchingCardsMutex) {
            try {
                Logger.d("Start waiting for players...");
                fetchingCardsMutex.wait(15000);
                Logger.d("Finished waiting lets go!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void dealCards() {
        for (PlayerController player : players) {
            player.dealCards(CardUtil.createRandomCards(5));
        }
    }

    private void playRound() {
        cardManager.playRound(new Callback() {
            @Override
            public void callback() {
                onRoundFinished();
            }
        });
    }

    private void onRoundFinished() {
        Logger.d("We are finished with one round");
    }

    private static class CardManager {
        private List<PlayerController> players;
        private IGameBoard gameBoard;
        private List<PlayerCardPair> cardsToPlay;
        private Callback finishedCallback;

        public CardManager(IGameBoard gameBoard) {
            this.gameBoard = gameBoard;
        }

        public void setPlayers(List<PlayerController> players) {
            this.players = players;
        }

        public void playRound(Callback finishedCallback) {
            this.finishedCallback = finishedCallback;
            Logger.d("playTurn()");
            playTurn();
        }

        private void playTurn() {
            cardsToPlay = popTopCards();
            if (cardsToPlay.size() > 0) {
                Logger.d("playSetOfCards() " + cardsToPlay.get(0).action.toString());
                playCards();
            } else {
                finishedCallback.callback();
            }
        }

        private void playCards() {
            if (cardsToPlay.size() > 0) {
                final PlayerCardPair pair = cardsToPlay.get(0);
                final Action card = pair.action;
                card.apply(gameBoard.getToken(pair.player), new Callback() {
                    @Override
                    public void callback() {
                        cardsToPlay.remove(pair);
                        playCards();
                    }
                });
            } else {
                playTurn();
            }
        }

        private List<PlayerCardPair> popTopCards() {
            List<PlayerCardPair> cards = new ArrayList<PlayerCardPair>();
            for (PlayerController player : players) {
                if (player.hasCards()) {
                    cards.add(new PlayerCardPair(player, player.drawAction()));
                }
            }
            return cards;
        }
    }

    private static class PlayerCardPair {
        public PlayerController player;
        public Action action;

        public PlayerCardPair(PlayerController player, Action action) {
            this.player = player;
            this.action = action;
        }
    }
}
