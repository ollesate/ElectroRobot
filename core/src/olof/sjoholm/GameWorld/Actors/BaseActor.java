package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by sjoholm on 24/09/16.
 */

public class BaseActor extends Actor {
    private Texture texture;
    private Rectangle bounds;

    {
        bounds = new Rectangle();
    }

    public BaseActor() {

    }

    public BaseActor(Texture texture) {
        setTexture(texture);
    }

    public BaseActor(Texture texture, float x, float y) {
        setTexture(texture);
        setX(x);
        setY(y);
    }

    public void setPosition(Vector2 position) {
        setX(position.x);
        setY(position.y);
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

    public Rectangle getBounds() {
        return bounds.set(getX(), getY(), getWidth(), getHeight());
    }

}
