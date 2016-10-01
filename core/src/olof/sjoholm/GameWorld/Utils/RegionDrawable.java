package olof.sjoholm.GameWorld.Utils;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import olof.sjoholm.Interfaces.Drawable;

/**
 * Created by sjoholm on 01/10/16.
 */

public class RegionDrawable implements Drawable {
    private Actor actor;
    private TextureRegion textureRegion;

    public RegionDrawable(Actor actor, TextureRegion textureRegion) {
        this.actor = actor;
        this.textureRegion = textureRegion;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (textureRegion != null) {
            batch.setColor(actor.getColor());
            batch.draw(textureRegion, actor.getX(), actor.getY());
        }
    }
}
