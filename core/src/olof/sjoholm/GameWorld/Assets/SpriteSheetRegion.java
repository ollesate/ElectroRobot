package olof.sjoholm.GameWorld.Assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import olof.sjoholm.GameWorld.Utils.Constants;


public class SpriteSheetRegion extends TextureRegion {

    public SpriteSheetRegion(Texture region, int x, int y) {
        super(region,
                (int)(x * Constants.STEP_SIZE),
                (int)(y * Constants.STEP_SIZE),
                (int)(Constants.STEP_SIZE),
                (int)(Constants.STEP_SIZE)
        );
    }
}
