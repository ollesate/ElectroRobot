package olof.sjoholm.Utils;

public enum Direction {

    FORWARD(0, 1),

    CRAB_LEFT(-1, 0),

    CRAB_RIGHT(1, 0),

    BACKWARDS(0, -1);

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
