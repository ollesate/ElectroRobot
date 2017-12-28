package olof.sjoholm.game.server.server_logic;

import java.util.HashSet;
import java.util.Set;

public class Checkpoints {
    private final int totalCheckpoints;
    private final Set<Integer> gatheredCheckpoints = new HashSet<Integer>();

    public Checkpoints(int totalCheckpoints) {
        this.totalCheckpoints = totalCheckpoints;
    }

    public void addCheckpoint(int checkPoint) {
        gatheredCheckpoints.add(checkPoint);
    }

    public int getLast() {
        int max = Integer.MIN_VALUE;
        for (Integer checkpoint : gatheredCheckpoints) {
            max = Math.max(checkpoint, max);
        }
        return max;
    }

    public boolean completedAllCheckpoints() {
        return gatheredCheckpoints.size() == totalCheckpoints;
    }
}
