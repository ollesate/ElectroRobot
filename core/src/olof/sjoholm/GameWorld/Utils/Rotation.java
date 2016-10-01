package olof.sjoholm.GameWorld.Utils;

/**
 * Created by sjoholm on 02/10/16.
 */

public enum Rotation {
    LEFT(90f, 1),
    RIGHT(-90f, 1),
    UTURN(180f, 2);

    public float degrees;

    public int duration;

    Rotation(float degrees, int duration) {
        this.degrees = degrees;
        this.duration = duration;
    }
}
