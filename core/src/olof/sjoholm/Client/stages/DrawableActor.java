package olof.sjoholm.Client.stages;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import olof.sjoholm.Interfaces.Drawable;

public class DrawableActor extends Actor {
    private Drawable drawable;

    public DrawableActor(Drawable drawable) {
        this.drawable = drawable;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawable.draw(batch, parentAlpha, getX(), getY(), getWidth(), getHeight(), getScaleX(),
                getScaleY(), getOriginX(), getOriginY(), getRotation(), getColor());
    }
}
