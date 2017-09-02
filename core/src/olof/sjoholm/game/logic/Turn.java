package olof.sjoholm.game.logic;

import java.util.ArrayList;
import java.util.List;

public class Turn {
    private OnTurnFinishedListener listener;

    public interface OnTurnFinishedListener {

        void onTurnFinished();
    }

    private final List<List<PlayerAction>> playerActions = new ArrayList<List<PlayerAction>>();
    private final int turnSize;

    public Turn(int turnSize) {
        this.turnSize = turnSize;
        for (int i = 0; i < turnSize; i++) {
            playerActions.add(new ArrayList<PlayerAction>());
        }
    }

    public void setFinishedListener(OnTurnFinishedListener listener) {
        this.listener = listener;
    }


    public void addToRound(int round, PlayerAction playerAction) {
        List<PlayerAction> turnActions = this.playerActions.get(round);
        turnActions.add(playerAction);
    }

    public List<PlayerAction> getRound(int turn) {
        return playerActions.get(turn);
    }

    public int size() {
        return turnSize;
    }

    public boolean isLastOfRound(olof.sjoholm.game.logic.BoardAction boardAction) {
        for (List<PlayerAction> playerAction : playerActions) {
            for (int i = 0; i < playerAction.size(); i++) {
                olof.sjoholm.game.logic.BoardAction action = playerAction.get(i).boardAction;
                if (boardAction.equals(action) && i == playerAction.size() - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getRoundOf(olof.sjoholm.game.logic.BoardAction boardAction) {
        for (int i = 0; i < playerActions.size(); i++) {
            List<PlayerAction> actions = playerActions.get(i);
            for (PlayerAction playerAction : actions) {
                olof.sjoholm.game.logic.BoardAction action = playerAction.boardAction;
                if (boardAction.equals(action)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void finished() {
        if (listener != null) {
            listener.onTurnFinished();
        }
    }
}
