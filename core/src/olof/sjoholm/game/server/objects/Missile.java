package olof.sjoholm.game.server.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import olof.sjoholm.game.server.logic.Direction;
import olof.sjoholm.assets.Textures;
import olof.sjoholm.game.shared.objects.PlayerToken;
import olof.sjoholm.sfx.Explosion;

public class Missile extends Actor {
    private final TextureRegion missileTexture;
    private final float actualWidth;
    private final float actualHeight;
    private final float speed;
    private final Direction direction;
    private PlayerToken owner;
    private PlayerToken hitPlayer;

    public Missile(float scale, float speed, float rotation) {
        this(scale, speed, Direction.fromRotation(rotation));
    }

    public Missile(float scale, float speed, Direction direction) {
        this.speed = speed;
        this.direction = direction;

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
        if (hitPlayer != null) {
            return;
        }

        if (getStage() instanceof GameStage) {
            GameStage stage = (GameStage) getStage();
            List<GameBoardActor> actors = stage.getGameBoard().getActors(getX(), getY(), actualWidth, actualHeight);
            for (GameBoardActor actor : actors) {
                if (!actor.equals(owner)) {
                    if (actor instanceof PlayerToken) {
                        hitPlayer = (PlayerToken) actor;
                        moveToPlayerAndBlowUp(hitPlayer);
                    }
                }
            }
        }
    }

    private void moveToPlayerAndBlowUp(final PlayerToken playerToken) {
        clearActions();
        // Hack logic to offset that the player token is not the size it is.
        // TODO: remove this if tank ever becomes the perfect size

        Vector2 sizeOffset = new Vector2(
                direction.dirX * playerToken.getWidth() / 2 * 0.8f,
                direction.dirY * playerToken.getHeight() / 2 * 0.8f);
        Vector2 pos = new Vector2(playerToken.getX(Align.center), playerToken.getY(Align.center)).
                sub(sizeOffset);
        Vector2 selfPos = new Vector2(getX(Align.center), getY(Align.center));

        float distance = pos.sub(selfPos).len();

        addAction(Actions.sequence(
                Actions.moveBy(direction.dirX * distance, direction.dirY * distance, distance / speed),
                Explosion.explodeAt(this),
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        playerToken.damage(1);
                        return true;
                    }
                },
                Actions.removeActor()
        ));
    }

    public void setOwner(PlayerToken owner) {
        this.owner = owner;
    }
}
