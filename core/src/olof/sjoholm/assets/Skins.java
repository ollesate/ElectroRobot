package olof.sjoholm.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Skins {
    public static Skin DEFAULT;

    private Skins() {
    }

    public static void initialize() {
        DEFAULT = new Skin(Gdx.files.internal("skin/uiskin.json"));
    }

    public static void dispose() {
        DEFAULT.dispose();
    }
}
