package olof.sjoholm.Api;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveAction;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import olof.sjoholm.GameWorld.Actors.GameBoardActor;
import olof.sjoholm.GameWorld.Actors.PlayerToken;
import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.Utils.Logger;
import olof.sjoholm.Views.GameStage;

public class Missile extends Actor {
    private final TextureRegion missileTexture;
    private final float actualWidth;
    private final float actualHeight;
    private PlayerToken owner;

    public Missile(float scale, float speed, float rotation) {
        this(scale, speed, Direction.fromRotation(rotation));
    }

    public Missile(float scale, float speed, Direction direction) {
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

        float distance = 1000f;
        addAction(Actions.sequence(
                Actions.moveBy(direction.dirX * distance, direction.dirY * distance, distance / speed),
                Actions.removeActor()
        ));
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
            GameStage stage = (GameStage) getStage();
            List<GameBoardActor> actors = stage.getActors(getX(), getY(), actualWidth, actualHeight);
            for (GameBoardActor actor : actors) {
                if (!actor.equals(owner)) {
                    if (actor instanceof PlayerToken) {
                        ((PlayerToken) actor).damage(1);
                        addAction(new RemoveActorAction());
                    }
                }
            }
        }
    }

    public void setOwner(PlayerToken owner) {
        this.owner = owner;
    }
}
