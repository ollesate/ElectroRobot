package olof.sjoholm.GameWorld.Assets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import olof.sjoholm.GameWorld.Actors.GameBoardActor;
import olof.sjoholm.Interfaces.Drawable;
import olof.sjoholm.Utils.Logger;
import sun.rmi.runtime.Log;

import static com.badlogic.gdx.scenes.scene2d.ui.Table.Debug.actor;


public class RegionDrawable implements Drawable {
    private TextureRegion textureRegion;

    public RegionDrawable(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    @Override
    public void draw(Batch batch, float parentAlpha, float x, float y, float width, float height,
                     float scaleX, float scaleY, float originX, float originY, float rotation,
                     Color tint) {
        batch.setColor(tint);
        batch.draw(textureRegion, x, y, width, height);
    }
}
