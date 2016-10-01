package olof.sjoholm.GameLogic;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.GameWorld.Utils.CardSequence;
import olof.sjoholm.GameWorld.Utils.CardUtil;

/**
 * @author sjoholm
 */
public class GameManager {
    private olof.sjoholm.Interfaces.IGameBoard gameBoard;

    private List<Player> players = new ArrayList<Player>();

    public GameManager(olof.sjoholm.Interfaces.IGameBoard gameBoard) {
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
        playRoundsUntilFinished(new olof.sjoholm.Interfaces.Callback() {
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

    private void playRoundsUntilFinished(final olof.sjoholm.Interfaces.Callback onFinished) {
        List<olof.sjoholm.Interfaces.ICard> cards = CardUtil.popTopCards(players);

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

    private void playRound(final List<olof.sjoholm.Interfaces.ICard> cards, final olof.sjoholm.Interfaces.Callback onFinished) {
        CardSequence.playCards(cards, new olof.sjoholm.Interfaces.Callback() {
            @Override
            public void callback() {
                Gdx.app.log("Tag", "Round finished");
                onFinished.callback();
            }
        });
    }

}
