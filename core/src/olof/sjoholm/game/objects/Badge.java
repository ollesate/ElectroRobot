package olof.sjoholm.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import olof.sjoholm.assets.Fonts;

public class Badge extends Label {

    public Badge() {
        super("", new LabelStyle(Fonts.get(Fonts.FONT_34), Color.WHITE));
    }
}
