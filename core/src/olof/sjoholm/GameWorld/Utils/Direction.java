package olof.sjoholm.GameWorld.Utils;

public enum Direction {

    UP(0, 1),

    LEFT(-1, 0),

    RIGHT(1, 0),

    DOWN(0, -1);

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x;

    public int y;

    public static Direction random() {
        int rand = (int)(Math.random() * Direction.values().length);
        return Direction.values()[rand];
    }
}
