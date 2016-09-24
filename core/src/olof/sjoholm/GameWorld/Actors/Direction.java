package olof.sjoholm.GameWorld.Actors;

/**
 * Created by sjoholm on 26/09/16.
 */
public enum Direction {
    UP,
    LEFT,
    RIGHT,
    DOWN;

    public static Direction random() {
        int rand = (int)(Math.random() * Direction.values().length);
        return Direction.values()[rand];
    }
}
