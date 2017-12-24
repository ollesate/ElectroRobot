package olof.sjoholm.game.shared.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.utils.Align;

import java.awt.Point;

import olof.sjoholm.assets.Textures;
import olof.sjoholm.configuration.Config;
import olof.sjoholm.configuration.Constants;
import olof.sjoholm.game.server.logic.Direction;
import olof.sjoholm.game.server.objects.GameBoard;
import olof.sjoholm.game.server.objects.GameBoardActor;
import olof.sjoholm.game.server.objects.HealthBarActor;
import olof.sjoholm.game.server.objects.Missile;
import olof.sjoholm.game.server.objects.SpawnPoint;
import olof.sjoholm.game.server.server_logic.Player;
import olof.sjoholm.game.shared.Damageable;
import olof.sjoholm.game.shared.logic.Movement;
import olof.sjoholm.game.shared.logic.Rotation;
import olof.sjoholm.sfx.Muzzle;
import olof.sjoholm.utils.actions.RelativeAction;
import olof.sjoholm.utils.ui.Drawable;

public class PlayerToken extends GameBoardActor implements Damageable {
    private float stepDelay = .5f;
    private float stepSpeed = 1.0f;
    private final TankAnimation tankAnimation = new TankAnimation();
    private SpawnPoint spawnPoint;
    private final int maxHealth = Constants.MAX_HEALTH;
    private int currentHealth = maxHealth;
    private Player player;
    private olof.sjoholm.game.server.objects.Badge badge;

    public PlayerToken() {
        setDrawable(tankAnimation);
        setTransform(false);
        addActor(new HealthBarActor(true));
    }

    @Override
    protected void sizeChanged() {
        HealthBarActor healthBar = findChild(HealthBarActor.class);
        healthBar.setSize(getWidth() * 0.75f, getHeight() * 0.1f);
        healthBar.setPosition(getWidth() / 2, getHeight() * 0.8f, Align.center);
    }

    @Override
    protected void positionChanged() {
        setOrigin(getWidth() / 2, getHeight() / 2);
    }

    private final Action roundAction = new Action() {

        @Override
        public boolean act(float delta) {
            setRotation(Math.round(getRotation()));
            setX(Math.round(getX()));
            setY(Math.round(getY()));
            return true;
        }
    };

    public Action rotate(Rotation rotation) {
        float playSpeed = Config.get(Config.PLAY_SPEED);

        SequenceAction sequence = Actions.sequence(
                Actions.rotateBy(
                        rotation.degrees,
                        stepSpeed / playSpeed * rotation.duration,
                        Interpolation.linear
                ),
                roundAction,
                Actions.delay(Constants.MOVE_STEP_DELAY / playSpeed)
        );
        sequence.setActor(this);
        return sequence;
    }

    public SpawnPoint getSpawnPoint() {
        return spawnPoint;
    }

    public void setSpawnPoint(SpawnPoint spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    public Action getShootAction() {
        return new ShootAction(this);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayerName(String name) {
        if (badge == null) {
            badge = new olof.sjoholm.game.server.objects.Badge();
            addActor(badge);
        }
        badge.setText(name);
        badge.setSize(badge.getPrefWidth(), badge.getPrefHeight());
        badge.setPosition((getWidth()) / 2, (getHeight()) / 2, Align.center);
    }

    public Action getFakeMoveAction(float duration) {
        TemporalAction action = new TemporalAction() {
            @Override
            protected void begin() {
                startAnimation();
            }

            @Override
            protected void end() {
                stopAnimation();
            }

            @Override
            protected void update(float percent) {

            }
        };
        action.setActor(this);
        action.setDuration(duration);
        return Actions.sequence(action, Actions.delay(1f));
    }

    private static class ShootAction extends RelativeAction {
        private final PlayerToken playerToken;
        private Missile missile;

        public ShootAction(PlayerToken playerToken) {
            this.playerToken = playerToken;
        }

        @Override
        public void begin() {
            Vector2 muzzleOffset = new Vector2(playerToken.getDirection())
                    .scl(playerToken.getWidth(), playerToken.getHeight())
                    .scl(0.5f);

            float muzzleX = playerToken.getX(Align.center) + muzzleOffset.x;
            float muzzleY = playerToken.getY(Align.center) + muzzleOffset.y;

            missile = new Missile(4f, Constants.MISSILE_SPEED, playerToken.getRotation());
            missile.setOwner(playerToken);
            missile.setPosition(muzzleX, muzzleY, Align.center);
            playerToken.getParent().addActorBefore(playerToken, missile);
            playerToken.getParent().addActor(Muzzle.create(muzzleX, muzzleY));
        }

        @Override
        public boolean update(float delta) {
            return missile.getParent() == null;
        }
    }

    public Vector2 getDirection() {
        // TODO: this is very dangerous rounding!
        float rotation = getRotation();
        float x = MathUtils.cosDeg(rotation);
        float y = MathUtils.sinDeg(rotation);
        return new Vector2(Math.round(x), Math.round(y));
    }

    public Action getMoveTileAction(Direction direction, float duration, boolean animate) {
        return new MoveTileAction(this, direction, animate, duration);
    }

    public Action getPushMoveAction(Movement movement, int steps) {
        return Actions.repeat(steps, new PushMoveAction(this, movement, getMoveDuration()));
    }

    private float getMoveDuration() {
        return Constants.MOVE_STEP_DURATION;
    }

    public void startAnimation() {
        tankAnimation.resume();
    }

    public void stopAnimation() {
        tankAnimation.pause();
    }

    @Override
    public int getCurrentHealth() {
        return currentHealth;
    }

    @Override
    public int getMaxHealth() {
        return maxHealth;
    }

    @Override
    public void damage(int damagePoints) {
        currentHealth -= damagePoints;
        if (currentHealth <= 0) {
            addAction(new RemoveActorAction());
        }
        fire(new DamagedEvent(maxHealth, currentHealth));
    }

    public static class DamagedEvent extends Event {
        public final int maxHealth;
        public final int healthLeft;

        public DamagedEvent(int maxHealth, int healthLeft) {
            this.maxHealth = maxHealth;
            this.healthLeft = healthLeft;
        }
    }

    public Point getGameBoardPosition() {
        return getGameBoardPosition(new Point());
    }

    public Point getGameBoardPosition(Point offset) {
        return GameBoard.getBoardPosition(getX() + offset.x * Constants.STEP_SIZE,
                getY() + offset.y * Constants.STEP_SIZE);
    }

    @Override
    public boolean remove() {
        clear();
        fire(new Destroyed(this));
        return super.remove();
    }

    public static class Destroyed extends Event {
        public final PlayerToken playerToken;

        public Destroyed(PlayerToken playerToken) {
            this.playerToken = playerToken;
        }
    }

    public class TankAnimation implements Drawable {
        private static final float frameDuration = 0.1f;
        private Animation<TextureRegion> animation;
        private float duration;
        private boolean isPlaying;

        public TankAnimation() {

            TextureRegion[] regions = new TextureRegion[9];
            for (int i = 0; i < regions.length; i++) {
                regions[i] = getFrame(i);
            }

            animation = new Animation<TextureRegion>(frameDuration, regions);
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

    public static float getWaitDuration() {
        // TODO: Find better way
        return 1f;
    }
}
