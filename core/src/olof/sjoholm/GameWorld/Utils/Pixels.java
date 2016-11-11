package olof.sjoholm.GameWorld.Utils;

import com.badlogic.gdx.Gdx;

public class Pixels {

    public static float fromDp(float dp) {
        return Gdx.graphics.getDensity() * dp;
    }

    public static float fromHorizontalPercent(int pixels) {
        return Gdx.graphics.getWidth() * pixels;
    }

    public static float fromVerticalPercent(int pixels) {
        return Gdx.graphics.getHeight() * pixels;
    }

}
