package olof.sjoholm.game.shared;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import olof.sjoholm.utils.ui.Drawable;

public class DrawUtils {

    public static void drawOnActor(Actor actor, Batch batch, float parentAlpha, Drawable drawable) {
        drawable.draw(batch, parentAlpha, actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight(),
                actor.getScaleX(), actor.getScaleY(), actor.getOriginX(), actor.getOriginY(), actor.getRotation(), actor.getColor());
    }

}
