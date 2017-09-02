package olof.sjoholm.game.objects;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import olof.sjoholm.utils.actions.RelativeAction;
import olof.sjoholm.configuration.Config;
import olof.sjoholm.game.logic.TileType;
import olof.sjoholm.net.Player;
import olof.sjoholm.configuration.Constants;
import olof.sjoholm.utils.Logger;
import olof.sjoholm.game.logic.Movement;
import olof.sjoholm.game.logic.Rotation;
import olof.sjoholm.sfx.Muzzle;

public class PlayerToken extends olof.sjoholm.game.objects.GameBoardActor {
    private float stepDelay = .5f;
    private float stepSpeed = 1.0f;
    private final TankAnimation tankAnimation = new TankAnimation();;
    private SpawnPoint spawnPoint;
    private final int maxHealth = Constants.MAX_HEALTH;
    private int currentHealth = maxHealth;
    private Player player;
    private olof.sjoholm.game.objects.Badge badge;

    public PlayerToken() {
        setDrawable(tankAnimation);
        setTransform(false);
    }

    @Override
    protected void positionChanged() {
        setOrigin(getWidth() / 2, getHeight() / 2);
    }

    public final Action animationResumeAction = new Action() {
        @Override
        public boolean act(float delta) {
            tankAnimation.resume();
            return true;
        }
    };

    public final Action animationPauseAction = new Action() {
        @Override
        public boolean act(float delta) {
            tankAnimation.pause();
            return true;
        }
    };

    public Action move(Movement movement, int steps) {
        float playSpeed = Config.get(Config.PLAY_SPEED);
        if (steps == 0) {
            return Actions.delay(Constants.MOVE_STEP_DELAY / playSpeed);
        }
        MoveAction moveAction = new MoveAction(movement, steps, playSpeed);
        moveAction.setPlayerToken(this);
        return moveAction;
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

    private Action getWaitSequence(int steps) {
        if (steps == 0) {
            return Actions.delay(0f);
        }

        SequenceAction actions = new SequenceAction();
        for (int i = 0; i < steps; i++) {
            actions.addAction(getSingleWaitSequence());
        }
        return actions;
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
            badge = new olof.sjoholm.game.objects.Badge();
            addActor(badge);
        }
        badge.setText(name);
        badge.setSize(badge.getPrefWidth(), badge.getPrefHeight());
        badge.setPosition((getWidth()) / 2, (getHeight()) / 2, Align.center);
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

    public static class MoveTileAction extends RelativeAction {
        private final PlayerToken token;
        private final Vector2 direction;
        private boolean animate;
        private Action moveByAction;
        private final float duration;
        private final Action fallAnimation;

        public MoveTileAction(PlayerToken actor, Vector2 direction, boolean animate, float duration) {
            this.duration = duration;
            this.token = actor;
            this.direction = direction;
            this.animate = animate;

            fallAnimation = Actions.sequence(Actions.scaleTo(0, 0, 1f), Actions.removeActor());
            fallAnimation.setActor(actor);
        }

        @Override
        public void begin() {
            SequenceAction sequenceAction = new SequenceAction();
            sequenceAction.setActor(token);
            MoveByAction moveAction = Actions.moveBy(
                    direction.x * Constants.STEP_SIZE, direction.y * Constants.STEP_SIZE, duration);
            moveAction.setActor(token);
            if (animate) {
                sequenceAction.addAction(token.animationResumeAction);
                sequenceAction.addAction(moveAction);
                sequenceAction.addAction(token.animationPauseAction);
            } else {
                sequenceAction.addAction(moveAction);
            }
            moveByAction = sequenceAction;
        }

        boolean fallToDeath;

        @Override
        public boolean update(float delta) {
            if (token.getParent() == null) {
                return true;
            }

            if (fallToDeath) {
                return fallAnimation.act(delta);
            }

            boolean finished = moveByAction.act(delta);
            if (finished) {
                token.setX(Math.round(token.getX()));
                token.setY(Math.round(token.getY()));

                Vector2 pos = token.getGameBoardPosition();
                TileType tile = token.getStage().getTile((int) pos.x, (int) pos.y);
                if (TileType.PIT == tile) {
                    fallToDeath = true;
                    return false;
                }
                return true;
            }
            return false;
        }
    }

    public static class MoveAction extends Action {
        private static final float MOVE_DURATION = Constants.MOVE_STEP_DURATION;

        private final Movement movement;
        private final int steps;
        private final Action waitAction;
        private PlayerToken playerToken;
        private int currentStep;
        private Action movementAction;
        private GameStage gameStage;
        private float stepDuration;

        public MoveAction(Movement movement, int steps, float speedModifier) {
            stepDuration = MOVE_DURATION / speedModifier;
            this.movement = movement;
            this.steps = steps;
            currentStep = 0;
            waitAction = Actions.delay(Constants.MOVE_STEP_DELAY / speedModifier);
        }

        public void setPlayerToken(PlayerToken playerToken) {
            this.playerToken = playerToken;
        }

        @Override
        public boolean act(float delta) {
            if (gameStage == null) {
                gameStage = playerToken.getStage();
            }

            if (movementAction == null) {
                movementAction = getNextMovement();
            }
            boolean isMovementDone = movementAction.act(delta);
            if (isMovementDone) {
                boolean doneWaiting = waitAction.act(delta);
                if (doneWaiting) {
                    waitAction.restart();
                    currentStep++;
                    if (steps == currentStep) {
                        return true;
                    }
                    movementAction = getNextMovement();
                    movementAction.setActor(actor);
                }
            }
            return false;
        }

        private Action getNextMovement() {
            Vector2 movementDir = playerToken.getMovementVector(movement);
            Vector2 pos = olof.sjoholm.game.objects.GameBoard.getBoardPosition(playerToken.getX(), playerToken.getY());
            List<olof.sjoholm.game.objects.GameBoardActor> actorsAt = gameStage.getActorsAt((int) (pos.x + movementDir.x),
                    (int) (pos.y + movementDir.y));
            PlayerToken blockingPlayer = null;
            if (actorsAt.size() > 0) {
                blockingPlayer = ((PlayerToken) actorsAt.get(0));
            }
            if (blockingPlayer != null) {
                int x = (int)(pos.x + movementDir.x * 2);
                int y = (int)(pos.y + movementDir.y * 2);
                boolean freeSpace = gameStage.isWithinBounds(x, y);
                boolean emptySpot = gameStage.getActorsAt(x, y).isEmpty();
                if (freeSpace && emptySpot) {
                    MoveTileAction moveSelf = new MoveTileAction(playerToken, movementDir, true, stepDuration);
                    MoveTileAction moveOther = new MoveTileAction(blockingPlayer, movementDir, false, stepDuration);
                    return Actions.parallel(moveSelf, moveOther);
                } else {
                    return new Animate(playerToken, 1f);
                }
            }

            int x = (int)(pos.x + movementDir.x);
            int y = (int)(pos.y + movementDir.y);
            boolean canMoveNextStep = gameStage.isWithinBounds(x, y);
            if (canMoveNextStep) {
                return new MoveTileAction(playerToken, movementDir, true, stepDuration);
            } else {
                return new Animate(playerToken, 1f);
            }
        }
    }

    private static class Animate extends Action {
        private PlayerToken playerToken;
        private final float duration;
        float currentDuration;

        public Animate(PlayerToken playerToken, float duration) {
            this.playerToken = playerToken;
            this.duration = duration;
        }

        private void start() {
            playerToken.tankAnimation.resume();
        }

        private void end() {
            playerToken.tankAnimation.pause();
        }

        @Override
        public boolean act(float delta) {
            if (currentDuration == 0) {
                start();
            }
            currentDuration += delta;
            if (currentDuration > duration) {
                end();
                return true;
            }
            return false;
        }
    }

    private Vector2 getMovementVector(Movement movement) {
        Vector2 currentDirection = getDirection();
        Vector2 movementVector;
        switch (movement) {
            case FORWARD:
                movementVector = new Vector2(currentDirection);
                break;
            case BACKWARDS:
                movementVector = new Vector2(currentDirection).scl(-1);
                break;
            case CRAB_LEFT:
                movementVector = new Vector2(currentDirection).rotate90(1);
                break;
            case CRAB_RIGHT:
                movementVector = new Vector2(currentDirection).rotate90(-1);
                break;
            default:
                throw new IllegalArgumentException("Illegal direction " + movement);
        }
        movementVector.x = Math.round(movementVector.x);
        movementVector.y = Math.round(movementVector.y);
        return movementVector;
    }

    public void startAnimation() {
        tankAnimation.resume();
    }

    public void stopAnimation() {
        tankAnimation.pause();
    }

    private static class MoveDirectionAction extends RelativeAction {
        private final olof.sjoholm.game.objects.GameBoardActor actor;
        private final Movement movement;
        private final float speed;
        private Action moveByAction;

        public MoveDirectionAction(olof.sjoholm.game.objects.GameBoardActor actor, Movement movement, float speed) {
            this.actor = actor;
            this.movement = movement;
            this.speed = speed;
        }

        @Override
        public void begin() {
            // TODO: this should not be up to math to decide
            Logger.d("Rotation " + actor.getRotation());
            Vector2 currentDir = new Vector2(
                    MathUtils.cosDeg(actor.getRotation()),
                    MathUtils.sinDeg(actor.getRotation())
            );
            Vector2 newDirection;
            switch (movement) {
                case FORWARD:
                    newDirection = new Vector2(currentDir);
                    break;
                case BACKWARDS:
                    newDirection = new Vector2(currentDir).scl(-1);
                    break;
                case CRAB_LEFT:
                    newDirection = new Vector2(currentDir).rotate90(1);
                    break;
                case CRAB_RIGHT:
                    newDirection = new Vector2(currentDir).rotate90(-1);
                    break;
                default:
                    throw new IllegalArgumentException("Illegal direction " + movement);
            }

            Logger.d("Current direction " + currentDir + " -> " + movement + " -> " + newDirection);
            Vector2 tileMovement = new Vector2(newDirection.x, newDirection.y).scl(Constants.STEP_SIZE);
            // Rounding
            newDirection.x = Math.round(newDirection.x);
            newDirection.y = Math.round(newDirection.y);

            int tileX = (int) (actor.getX() / Constants.STEP_SIZE + newDirection.x);
            int tileY = (int) (actor.getY() / Constants.STEP_SIZE + newDirection.y);
            Logger.d("is x correct ? " + String.format("%s %s %s", actor.getX(), (int) (actor.getX() / Constants.STEP_SIZE + newDirection.x), newDirection.x));
            GameStage gameStage = actor.getStage();
            boolean canMoveThere = false;
            List<GameBoardActor> actorsAt = gameStage.getActorsAt(tileX, tileY);
            PlayerToken playerInTheWay = actorsAt.size() > 0 ? ((PlayerToken) actorsAt.get(0)) : null;
            if (playerInTheWay != null) {
                Logger.d("Player in the way!");
                int pushTileX = (int) (tileX + newDirection.x);
                int pushTileY = (int) (tileX + newDirection.y);
                if (gameStage.isWithinBounds(pushTileX, pushTileY)) {
                    Logger.d("Push him!");
                    canMoveThere = true;
                    MoveByAction moveByAction = new MoveByAction();
                    moveByAction.setAmount(tileMovement.x, tileMovement.y);
                    moveByAction.setDuration(speed);
                    moveByAction.setInterpolation(Interpolation.linear);

                    playerInTheWay.addAction(moveByAction);
                }
            } else if (gameStage.isWithinBounds(tileX, tileY)){
                canMoveThere = true;
            }

            Logger.d("Can move there ? " + tileX + " " + tileY + " " + canMoveThere);
            if (canMoveThere) {
                MoveByAction moveByAction = Actions.moveBy(tileMovement.x, tileMovement.y, speed, Interpolation.linear);
                moveByAction.setActor(actor);
                DelayAction delay = Actions.delay(0);
                delay.setActor(actor);
                this.moveByAction = Actions.sequence(moveByAction, delay);
                this.moveByAction.setActor(actor);
            } else {
                Logger.d("Lets just sit here");
                moveByAction = Actions.delay(speed);
            }
        }

        @Override
        public boolean update(float delta) {
            return moveByAction.act(delta);
        }
    }

    private Action getSingleWaitSequence() {
        return Actions.sequence(
                animationResumeAction,
                Actions.delay(stepSpeed),
                animationPauseAction,
                Actions.delay(stepDelay)
        );
    }

    public void damage(int damage) {
        currentHealth -= damage;
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

    public Vector2 getGameBoardPosition() {
        return GameBoard.getBoardPosition(getX(), getY());
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
}