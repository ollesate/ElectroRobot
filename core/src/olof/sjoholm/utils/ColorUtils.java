package olof.sjoholm.utils;

import com.badlogic.gdx.graphics.Color;

public class ColorUtils {

    private ColorUtils() {
    }

    public static Color alpha(Color color, float alpha) {
        return new Color(color.r, color.g, color.b, alpha);
    }
}
