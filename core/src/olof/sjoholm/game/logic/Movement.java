package olof.sjoholm.game.logic;

public enum Movement {

    FORWARD(0, 1),

    CRAB_LEFT(-1, 0),

    CRAB_RIGHT(1, 0),

    BACKWARDS(0, -1);

    Movement(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x;

    public int y;

    public static Movement random() {
        int rand = (int)(Math.random() * Movement.values().length);
        return Movement.values()[rand];
    }
}
