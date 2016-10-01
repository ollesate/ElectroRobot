package olof.sjoholm.GameWorld.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.Interfaces.Drawable;

/**
 * Created by sjoholm on 01/10/16.
 */

public class TankAnimation implements Drawable {
    private static final float frameDuration = 0.1f;
    private Animation animation;
    private float duration;
    private boolean isPlaying;
    private Actor actor;

    public TankAnimation(Actor actor) {
        this.actor = actor;

        TextureRegion[] regions = new TextureRegion[9];
        for (int i = 0; i < regions.length; i++) {
            regions[i] = getFrame(i);
        }

        actor.setOrigin(Constants.STEP_SIZE / 2, Constants.STEP_SIZE / 2);

        animation = new Animation(frameDuration, regions);
    }

    private TextureRegion getFrame(int frame) {
        int size = (int) Constants.STEP_SIZE;
        int x = (int) (frame * Constants.STEP_SIZE);
        int y = (int) (9 * Constants.STEP_SIZE);
        return new TextureRegion(
                Textures.spritesheet,
                x,
                y,
                size,
                size
        );
    }

    public void resume() {
        isPlaying = true;
    }

    public void pause() {
        isPlaying = false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isPlaying) {
            duration += Gdx.graphics.getDeltaTime();
        }
        batch.draw(
                animation.getKeyFrame(duration, true),
                actor.getX(), actor.getY(),
                actor.getOriginX(), actor.getOriginY(),
                actor.getWidth(), actor.getHeight(),
                actor.getScaleX(), actor.getScaleY(),
                actor.getRotation()
                );
    }
}
