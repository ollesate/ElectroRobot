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
}
