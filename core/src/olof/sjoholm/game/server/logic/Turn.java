package olof.sjoholm.game.server.logic;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.game.shared.logic.cards.BoardAction;

public class Turn {
    private final List<PlaySet> playSets;

    public Turn(List<PlaySet> playSets) {
        this.playSets = playSets;
    }

    public List<PlayerAction> getRound(int turn) {
        return null;
    }

    public int size() {
        return 0;
    }

    public boolean isLastOfRound(BoardAction boardAction) {
        /*
        for (List<PlayerAction> playerAction : playerActions) {
            for (int i = 0; i < playerAction.size(); i++) {
                BoardAction action = playerAction.get(i).boardAction;
                if (boardAction.equals(action) && i == playerAction.size() - 1) {
                    return true;
                }
            }
        }
        */
        return false;
    }

    public int getRoundOf(BoardAction boardAction) {
        /*
        for (int i = 0; i < playerActions.size(); i++) {
            List<PlayerAction> actions = playerActions.get(i);
            for (PlayerAction playerAction : actions) {
                BoardAction action = playerAction.boardAction;
                if (boardAction.equals(action)) {
                    return i + 1; // We do not count 0.
                }
            }
        }
        */
        return -1;
    }
}
