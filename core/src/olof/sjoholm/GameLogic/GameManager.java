package olof.sjoholm.GameLogic;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.GameWorld.Server.Player;
import olof.sjoholm.GameWorld.Server.PlayerManager;
import olof.sjoholm.GameWorld.Server.Screens.GameScreen;
import olof.sjoholm.GameWorld.Utils.CardUtil;
import olof.sjoholm.GameWorld.Utils.Logger;
import olof.sjoholm.Interfaces.Callback;
import olof.sjoholm.Interfaces.Action;
import olof.sjoholm.Interfaces.IGameBoard;

/**
 * @author sjoholm
 */
public class GameManager {
    private IGameBoard gameBoard;
    private PlayerManager playerManager;
    private GameScreen.CountDownManager countDownManager;

    private List<Player> players = new ArrayList<Player>();
    private final CardManager cardManager;
    private final Object fetchingCardsMutex = new Object();

    public GameManager(IGameBoard gameBoard, PlayerManager playerManager,
                       GameScreen.CountDownManager countDownManager) {
        this.gameBoard = gameBoard;
        this.playerManager = playerManager;
        this.countDownManager = countDownManager;
        cardManager = new CardManager(playerManager.getPlayers(), gameBoard);

        setupBoard();
        startGame();
    }

    private void setupBoard() {
        players.clear();
        for (Player player : playerManager.getPlayers()) {
            gameBoard.spawnToken(player);
            players.add(player);
        }
    }

    private void startGame() {
        playerManager.alertStartGame();

        Logger.d("startGame()");
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

            final List<Player> readyList = new ArrayList<Player>();
            for (final Player player : players) {
                player.getCards(new Player.OnCardsReceivedListener() {
                    @Override
                    public void onCardsReceived(List<Action> cards) {
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
        long countDownTime = 10000L;
        countDownManager.startCountDown(countDownTime);
        try {
            Thread.sleep(countDownTime);
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
        for (Player player : players) {
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
        private List<Player> players;
        private IGameBoard gameBoard;
        private List<PlayerCardPair> cardsToPlay;
        private Callback finishedCallback;

        public CardManager(List<Player> players, IGameBoard gameBoard) {
            this.players = players;
            this.gameBoard = gameBoard;
        }

        public void playRound(Callback finishedCallback) {
            this.finishedCallback = finishedCallback;
            Logger.d("playTurn()");
            playTurn();
        }

        private void playTurn() {
            cardsToPlay = popTopCards();
            if (cardsToPlay.size() > 0) {
                Logger.d("playSetOfCards() " + cardsToPlay.get(0).card.toString());
                playCards();
            } else {
                finishedCallback.callback();
            }
        }

        private void playCards() {
            if (cardsToPlay.size() > 0) {
                final PlayerCardPair pair = cardsToPlay.get(0);
                final Action card = pair.card;
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
            for (Player player : players) {
                if (player.hasCards()) {
                    cards.add(new PlayerCardPair(player, player.popTopCard()));
                }
            }
            return cards;
        }
    }

    private static class PlayerCardPair {
        public Player player;
        public Action card;

        public PlayerCardPair(Player player, Action card) {
            this.player = player;
            this.card = card;
        }
    }
}
