package olof.sjoholm.GameLogic;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.GameWorld.Server.Player;
import olof.sjoholm.GameWorld.Server.PlayerManager;
import olof.sjoholm.GameWorld.Utils.CardSequence;
import olof.sjoholm.GameWorld.Utils.CardUtil;
import olof.sjoholm.Interfaces.Callback;
import olof.sjoholm.Interfaces.ICard;
import olof.sjoholm.Interfaces.IGameBoard;

/**
 * @author sjoholm
 */
public class GameManager {
    private IGameBoard gameBoard;
    private PlayerManager playerManager;

    private List<Player> players = new ArrayList<Player>();

    public GameManager(IGameBoard gameBoard, PlayerManager playerManager) {
        this.gameBoard = gameBoard;
        this.playerManager = playerManager;
        setupBoard();
        startGame();
    }

    private void setupBoard() {
        players.clear();
        for (Player player : playerManager.getPlayers()) {
            players.add(player);
        }
    }

    private void startGame() {
        startTurn();
    }

    private void startTurn() {
        dealCards();
        playRoundsUntilFinished(new olof.sjoholm.Interfaces.Callback() {
            @Override
            public void callback() {
                onTurnFinished();
            }
        });
    }

    private void dealCards() {
        for (Player player : players) {
            player.dealCards(CardUtil.createRandomCards(5));
        }
    }

    private void onTurnFinished() {
        // Restart everything
        startTurn();
    }

    private void playRoundsUntilFinished(final Callback onFinished) {
        List<ICard> cards = CardUtil.popTopCards(players);

        if (cards.size() > 0) {
            playRound(cards, new olof.sjoholm.Interfaces.Callback() {
                @Override
                public void callback() {
                    playRoundsUntilFinished(onFinished);
                }
            });
        } else {
            onFinished.callback();
        }
    }

    private List<ICard> popTopCards()

    private void playRound(final List<ICard> cards, final Callback onFinished) {
        CardSequence.playCards(cards, new olof.sjoholm.Interfaces.Callback() {
            @Override
            public void callback() {
                Gdx.app.log("Tag", "Round finished");
                onFinished.callback();
            }
        });
    }
}
