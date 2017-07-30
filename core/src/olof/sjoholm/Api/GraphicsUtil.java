package olof.sjoholm.Api;

import com.badlogic.gdx.Gdx;

public class GraphicsUtil {

    public static float dpToPixels(float dp) {
        return Gdx.graphics.getDensity() * dp;
    }
}
