package olof.sjoholm.Api;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class PopupText extends Label {
    private final float duration;

    public PopupText(CharSequence text, BitmapFont font, float duration) {
        super(text, new LabelStyle(font, Color.BLACK));
        this.duration = duration;
    }

    public void start() {
        addAction(Actions.sequence(
                Actions.delay(duration),
                Actions.fadeOut(0.5f),
                Actions.removeActor()
        ));
    }
}
