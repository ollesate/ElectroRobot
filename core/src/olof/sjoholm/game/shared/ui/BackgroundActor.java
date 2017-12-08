package olof.sjoholm.game.shared.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;

/**
 * An actor that draws a simple background from drawable. Rotation not supported.
 */
public class BackgroundActor extends Actor {
    private final BaseDrawable baseDrawable;

    public BackgroundActor(BaseDrawable baseDrawable) {
        this.baseDrawable = baseDrawable;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color c = batch.getColor();
        batch.setColor(c.r, c.g, c.b, parentAlpha);
        baseDrawable.draw(batch, getX(), getY(), getWidth(), getHeight());
        batch.setColor(c);
    }
}
