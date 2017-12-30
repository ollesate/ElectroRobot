package olof.sjoholm.game.shared.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;

import olof.sjoholm.assets.Textures;

public class BackgroundDrawable extends BaseDrawable {
    private final Texture texture;
    private final Color color;

    public BackgroundDrawable(Color color) {
        texture = Textures.BACKGROUND;
        this.color = color;
        setMinWidth(texture.getWidth());
        setMinHeight(texture.getHeight());
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        batch.setColor(color);
        batch.draw(texture, x, y, width, height);
    }
}
