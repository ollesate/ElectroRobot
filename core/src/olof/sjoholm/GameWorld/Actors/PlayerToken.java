package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import olof.sjoholm.GameWorld.Assets.TankAnimation;
import olof.sjoholm.Utils.Constants;
import olof.sjoholm.Utils.Direction;
import olof.sjoholm.Utils.Logger;
import olof.sjoholm.Utils.Rotation;

public class PlayerToken extends GameBoardActor {
    private float stepDelay = .5f;
    private float stepSpeed = 2.0f;
    private TankAnimation tankAnimation;

    {
        setOrigin(getWidth() / 2, getHeight() / 2);
        // TODO: remove one if this?
        setOrigin(olof.sjoholm.Utils.Constants.STEP_SIZE / 2, olof.sjoholm.Utils.Constants.STEP_SIZE / 2);
        tankAnimation = new TankAnimation();
        setDrawable(tankAnimation);
    }

    @Override
    protected void positionChanged() {
        setOrigin(getWidth() / 2, getHeight() / 2);
    }

    private final Action animationResumeAction = new Action() {
        @Override
        public boolean act(float delta) {
            tankAnimation.resume();
            return true;
        }
    };

    private final Action animationPauseAction = new Action() {
        @Override
        public boolean act(float delta) {
            tankAnimation.pause();
            return true;
        }
    };

    public Action move(Direction direction, int steps) {
        if (steps == 0) {
            return Actions.delay(stepDelay);
        }
        SequenceAction sequence = new SequenceAction();
        for (int i = 0; i < steps; i++) {
            SequenceAction tileSequence = Actions.sequence(
                    animationResumeAction,
                    new MoveDirectionAction(this, direction, stepSpeed),
                    roundAction,
                    animationPauseAction,
                    Actions.delay(stepDelay));
            sequence.addAction(tileSequence);
        }
        return sequence;
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
        return Actions.sequence(
                Actions.rotateBy(
                        rotation.degrees,
                        stepSpeed * rotation.duration,
                        Interpolation.linear
                ),
                roundAction,
                Actions.delay(stepDelay)
        );
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

    public abstract static class RelativeAction extends Action {
        private boolean hasBegun;

        public abstract void begin();

        public abstract boolean update(float delta);

        @Override
        public boolean act(float delta) {
            if (!hasBegun) {
                begin();
                hasBegun = true;
            }
            return update(delta);
        }
    }

    public Vector2 getDirection() {
        // TODO: this is very dangerous rounding!
        float rotation = getRotation();
        float x = MathUtils.cosDeg(rotation);
        float y = MathUtils.sinDeg(rotation);
        return new Vector2(Math.round(x), Math.round(y));
    }

    private static class MoveDirectionAction extends RelativeAction {
        private final Actor actor;
        private final Direction direction;
        private final float speed;
        private MoveByAction moveByAction;

        public MoveDirectionAction(Actor actor, Direction direction, float speed) {
            this.actor = actor;
            this.direction = direction;
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
            switch (direction) {
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
                    throw new IllegalArgumentException("Illegal direction " + direction);
            }

            Logger.d("Current direction " + currentDir + " -> " + direction + " -> " + newDirection);

            Vector2 tileMovement = new Vector2(newDirection.x, newDirection.y).scl(Constants.STEP_SIZE);
            moveByAction = Actions.moveBy(tileMovement.x, tileMovement.y, speed, Interpolation.linear);
            moveByAction.setTarget(actor);
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
}
