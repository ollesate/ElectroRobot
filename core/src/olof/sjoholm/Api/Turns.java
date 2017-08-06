package olof.sjoholm.Api;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.GameWorld.Actors.PlayerAction;

public class Turns {
    private final List<List<PlayerAction>> playerActions = new ArrayList<List<PlayerAction>>();
    private final int turnSize;

    public Turns(int turnSize) {
        this.turnSize = turnSize;
        for (int i = 0; i < turnSize; i++) {
            playerActions.add(new ArrayList<PlayerAction>());
        }
    }

    public void addToTurn(int turn, PlayerAction playerAction) {
        List<PlayerAction> turnActions = this.playerActions.get(turn);
        turnActions.add(playerAction);
    }

    public List<PlayerAction> getTurn(int turn) {
        return playerActions.get(turn);
    }

    public int size() {
        return turnSize;
    }
}
