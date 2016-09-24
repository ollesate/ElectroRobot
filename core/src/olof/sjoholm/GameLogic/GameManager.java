package olof.sjoholm.GameLogic;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.GameWorld.Utils.CardSequence;
import olof.sjoholm.GameWorld.Utils.CardUtil;

/**
 * @author sjoholm
 */
public class GameManager {
    private IGameBoard gameBoard;

    private List<Player> players = new ArrayList<Player>();

    public GameManager(IGameBoard gameBoard) {
        this.gameBoard = gameBoard;
        setupBoard();
        startGame();
    }

    private void setupBoard() {
        players.clear();
        players.add(new Player(
                gameBoard.spawnToken(0),
                gameBoard.spawnCardHand(0)
        ));
        players.add(new Player(
                gameBoard.spawnToken(1),
                gameBoard.spawnCardHand(1)
        ));
    }

    private void startGame() {
        startTurn();
    }

    private void dealCards() {
        for (Player player : players) {
            player.dealFiveCards();
        }
    }

    private void startTurn() {
        dealCards();
        playRoundsUntilFinished(new Callback() {
            @Override
            public void callback() {
                onTurnFinished();
            }
        });
    }

    private void onTurnFinished() {
        // Restart everything
        startTurn();
    }

    private void playRoundsUntilFinished(final Callback onFinished) {
        List<ICard> cards = CardUtil.popTopCards(players);

        if (cards.size() > 0) {
            playRound(cards, new Callback() {
                @Override
                public void callback() {
                    playRoundsUntilFinished(onFinished);
                }
            });
        } else {
            onFinished.callback();
        }
    }

    private void playRound(final List<ICard> cards, final Callback onFinished) {
        CardSequence.playCards(cards, new Callback() {
            @Override
            public void callback() {
                Gdx.app.log("Tag", "Round finished");
                onFinished.callback();
            }
        });
    }

}
