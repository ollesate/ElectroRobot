package olof.sjoholm.GameWorld.Assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import olof.sjoholm.Utils.Constants;


public class SpriteSheetRegion extends TextureRegion {

    public SpriteSheetRegion(Texture region, int x, int y, int regionWidth, int regionHeight) {
        super(region, x * regionWidth, y * regionHeight, regionWidth, regionHeight);
    }
}
