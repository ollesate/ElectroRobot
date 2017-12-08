package olof.sjoholm.game.shared.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;

import olof.sjoholm.assets.Textures;

public class ColorDrawable extends BaseDrawable {
    private static final Texture texture = Textures.BACKGROUND;
    private Color color;

    public ColorDrawable() {

    }

    public ColorDrawable(Color color) {
        this.color = color;
    }

    public void setColor(Color color) {
        this.color = color;
    }


    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        batch.setColor(color.r, color.g, color.b, batch.getColor().a);
        batch.draw(texture, x, y, width, height);
    }
}
