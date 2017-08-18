package olof.sjoholm.GameWorld;

public enum TileType {
    FLOOR(0),
    PIT(-1);

    private final int id;

    TileType(int id) {
        this.id = id;
    }

    public static TileType fromId(int id) {
        for (TileType tile : values()) {
            if (id == tile.id) {
                return tile;
            }
        }
        throw new IllegalStateException("No tile for id " + id);
    }
}
