package olof.sjoholm.utils.graphic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;


public class TextureDrawable implements Drawable {
    private Texture texture;

    public TextureDrawable(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void draw(Batch batch, float parentAlpha, float x, float y, float width, float height,
                     float scaleX, float scaleY, float originX, float originY, float rotation,
                     Color tint) {
        batch.setColor(tint.r, tint.g, tint.b, tint.a * parentAlpha);
        batch.draw(texture, x, y, width, height);
    }
}
