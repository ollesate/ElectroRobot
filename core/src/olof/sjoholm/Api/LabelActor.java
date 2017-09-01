package olof.sjoholm.Api;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class LabelActor extends Label {

    public LabelActor(CharSequence text, BitmapFont bitmapFont) {
        super(text, new Label.LabelStyle(bitmapFont, Color.BLACK));
    }
}
