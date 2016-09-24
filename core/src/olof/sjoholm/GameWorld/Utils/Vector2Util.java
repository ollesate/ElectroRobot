package olof.sjoholm.GameWorld.Utils;

import com.badlogic.gdx.math.Vector2;

import olof.sjoholm.GameWorld.Actors.Direction;

/**
 * Created by sjoholm on 25/09/16.
 */

public class Vector2Util {

    public static Vector2 vectorFromDirection(Direction direction) {
        switch (direction) {
            case DOWN:
                return new Vector2(0, -1);
            case LEFT:
                return new Vector2(-1, 0);
            case RIGHT:
                return new Vector2(1, 0);
            case UP:
                return new Vector2(0, 1);
        }
        return null;
    }

}
