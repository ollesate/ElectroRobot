package olof.sjoholm.game.server.logic;

import java.util.HashSet;
import java.util.Set;

public enum TileType {
    FLOOR(0, 1),
    PIT(-1),
    OUT_OF_BOUNDS(-2);

    private final Set<Integer> ids = new HashSet<Integer>();

    TileType(int... ids) {
        for (int id : ids) {
            this.ids.add(id);
        }
    }

    public static TileType fromId(int id) {
        for (TileType tile : values()) {
            if (tile.ids.contains(id)) {
                return tile;
            }
        }
        // TODO: Fix this
        return FLOOR;
    }
}
