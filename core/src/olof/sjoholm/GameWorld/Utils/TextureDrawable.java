package olof.sjoholm.GameWorld.Utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import olof.sjoholm.Interfaces.Drawable;

/**
 * Created by sjoholm on 01/10/16.
 */

public class TextureDrawable implements Drawable {
    private Texture texture;
    private Actor actor;

    public TextureDrawable(Actor actor, Texture texture) {
        this.texture = texture;
        this.actor = actor;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (texture != null) {
            batch.setColor(actor.getColor());
            batch.draw(texture, actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
        }
    }
}
