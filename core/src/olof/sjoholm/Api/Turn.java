package olof.sjoholm.Api;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.GameWorld.Actors.PlayerAction;

public class Turn {
    private final List<List<PlayerAction>> playerActions = new ArrayList<List<PlayerAction>>();
    private final int turnSize;

    public Turn(int turnSize) {
        this.turnSize = turnSize;
        for (int i = 0; i < turnSize; i++) {
            playerActions.add(new ArrayList<PlayerAction>());
        }
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

    public boolean isLastOfRound(BoardAction boardAction) {
        for (List<PlayerAction> playerAction : playerActions) {
            for (int i = 0; i < playerAction.size(); i++) {
                BoardAction action = playerAction.get(i).boardAction;
                if (boardAction.equals(action) && i == playerAction.size() - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getRoundOf(BoardAction boardAction) {
        for (int i = 0; i < playerActions.size(); i++) {
            List<PlayerAction> actions = playerActions.get(i);
            for (PlayerAction playerAction : actions) {
                BoardAction action = playerAction.boardAction;
                if (boardAction.equals(action)) {
                    return i;
                }
            }
        }
        return -1;
    }
}