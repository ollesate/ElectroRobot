package olof.sjoholm.utils.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;


public interface Drawable {

    void draw(Batch batch, float parentAlpha, float x, float y, float width, float height,
              float scaleX, float scaleY, float originX, float originY, float rotation, Color tint);
}
