package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;


public class BaseActor extends Actor {
    private Texture texture;

    public BaseActor(Texture texture) {
        setTexture(texture);
    }

    protected void setTexture(Texture texture) {
        this.texture = texture;
        setWidth(texture.getWidth());
        setHeight(texture.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (texture != null) {
            batch.setColor(getColor());
            batch.draw(texture, getX(), getY(), getWidth(), getHeight());
        }
    }

}
