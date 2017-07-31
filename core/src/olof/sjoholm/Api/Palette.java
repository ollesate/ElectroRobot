package olof.sjoholm.Api;


import com.badlogic.gdx.graphics.Color;

public class Palette {
    public static final Color PRIMARY = new Color(0.012f, 0.663f, 0.957f, 1f);
    public static final Color PRIMARY_DESELECTED = new Color(PRIMARY.r, PRIMARY.g, PRIMARY.b, 0.4f);
    public static final Color ACCENT = new Color(0f, .588f, 0.533f, 1f);
    public static final Color TEXT_COLOR = new Color(Color.BLACK);

    private Palette() {
    }
}
