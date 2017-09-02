package olof.sjoholm.utils.graphic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class SpriteSheetRegion extends TextureRegion {

    public SpriteSheetRegion(Texture region, int x, int y, int regionWidth, int regionHeight) {
        super(region, x * regionWidth, y * regionHeight, regionWidth, regionHeight);
    }
}
