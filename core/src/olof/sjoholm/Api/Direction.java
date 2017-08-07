package olof.sjoholm.Api;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public enum Direction {
    LEFT(-1, 0, 180f),
    UP(0, 1, 90f),
    RIGHT(1, 0, 0f),
    DOWN(0, -1, 270f);

    public final float dirX;
    public final float dirY;
    public final float rotation;

    Direction(float dirX, float dirY, float rotation) {
        this.dirX = dirX;
        this.dirY = dirY;
        this.rotation = rotation;
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
