package olof.sjoholm.Api;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import olof.sjoholm.GameWorld.Actors.GameBoardActor;
import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.Utils.Logger;
import olof.sjoholm.Views.GameStage;

public class Missile extends Actor {
    private final TextureRegion missileTexture;
    private final float actualWidth;
    private final float actualHeight;

    public Missile(float scale, Direction direction, float speed) {
        missileTexture = Textures.MISSILE;
        setWidth(missileTexture.getRegionWidth() * scale);
        setHeight(missileTexture.getRegionHeight() * scale);
        setRotation(direction.rotation);

        if (Direction.LEFT == direction || Direction.RIGHT == direction) {
            actualWidth = getWidth();
            actualHeight = getHeight();
        } else {
            actualWidth = getHeight();
            actualHeight = getWidth();
        }

        float distance = 4000f;
        addAction(Actions.moveBy(direction.dirX * distance, direction.dirY * distance, 1 / speed * 5));
    }

    @Override
    protected void sizeChanged() {
        setOrigin(Align.center);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor());
        batch.draw(missileTexture, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (getStage() instanceof GameStage) {
            // TODO: Not good logic, works for now until trigonometric collision code with rotated rectangles.
            // OR NOT! All shots will be horizontal or vertial. Either radius check.
            GameStage stage = (GameStage) getStage();
            List<GameBoardActor> actors = stage.getActors(getX(), getY(), getWidth(), getHeight());
            setColor(actors.size() > 0 ? Color.RED : Color.WHITE);
        }
    }
}