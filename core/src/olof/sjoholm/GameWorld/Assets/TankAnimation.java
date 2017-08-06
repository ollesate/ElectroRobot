package olof.sjoholm.GameWorld.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import olof.sjoholm.Interfaces.Drawable;


public class TankAnimation implements Drawable {
    private static final float frameDuration = 0.1f;
    private Animation animation;
    private float duration;
    private boolean isPlaying;

    public TankAnimation() {

        TextureRegion[] regions = new TextureRegion[9];
        for (int i = 0; i < regions.length; i++) {
            regions[i] = getFrame(i);
        }

        animation = new Animation(frameDuration, regions);
    }

    private TextureRegion getFrame(int frame) {
        int regionSize = 32;
        int size = regionSize;
        int x = (int) (frame * regionSize);
        int y = (int) (9 * regionSize);
        return new TextureRegion(
                Textures.SPRITE_SHEET,
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
    public void draw(Batch batch, float parentAlpha, float x, float y, float width, float height,
                     float scaleX, float scaleY, float originX, float originY, float rotation, Color tint) {
        if (isPlaying) {
            duration += Gdx.graphics.getDeltaTime();
        }
        batch.setColor(tint);
        batch.draw(
                animation.getKeyFrame(duration, true),
                x, y,
                originX, originY,
                width, height,
                scaleX, scaleY,
                rotation
        );
    }
}
