package olof.sjoholm.game.server.logic;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.awt.Point;

import olof.sjoholm.game.shared.logic.Movement;

public enum Direction {
    LEFT(-1, 0, 180f),
    UP(0, 1, 90f),
    RIGHT(1, 0, 0f),
    DOWN(0, -1, 270f);

    static {
        LEFT.rotateLeft = DOWN;
        LEFT.rotateRight = UP;
        LEFT.opposite = RIGHT;

        UP.rotateLeft = LEFT;
        UP.rotateRight = RIGHT;
        UP.opposite = DOWN;

        RIGHT.rotateLeft = UP;
        RIGHT.rotateRight = DOWN;
        RIGHT.opposite = LEFT;

        DOWN.rotateLeft = RIGHT;
        DOWN.rotateRight = LEFT;
        DOWN.opposite = UP;
    }

    public final int dirX;
    public final int dirY;
    public final float rotation;

    private Direction rotateRight;
    private Direction rotateLeft;
    private Direction opposite;

    Direction(int dirX, int dirY, float rotation) {
        this.dirX = dirX;
        this.dirY = dirY;
        this.rotation = rotation;
    }

    public Vector2 getVector() {
        return new Vector2(dirX, dirY);
    }

    public Point getPoint() {
        return new Point(dirX, dirY);
    }

    public Direction getRotateRight() {
        return rotateRight;
    }

    public Direction getRotateLeft() {
        return rotateLeft;
    }

    public Direction getOpposite() {
        return opposite;
    }

    public Direction translate(Movement movement) {
        float newRotation = rotation + movement.getRotation();
        return fromRotation(newRotation);
    }

    public static Direction fromRotation(float rotation) {
        float x = MathUtils.cosDeg(rotation);
        float y = MathUtils.sinDeg(rotation);
        Vector2 dir = new Vector2(Math.round(x), Math.round(y));

        for (Direction direction : Direction.values()) {
            if (dir.x == direction.dirX && dir.y == direction.dirY) {
                return direction;
            }
        }
        throw new IllegalStateException("No direction for rotation " + rotation);
    }
}
