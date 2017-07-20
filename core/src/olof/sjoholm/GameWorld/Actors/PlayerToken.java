package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import olof.sjoholm.Utils.Logger;
import olof.sjoholm.Utils.Rotation;
import olof.sjoholm.GameWorld.Assets.TankAnimation;
import olof.sjoholm.Interfaces.Callback;
import olof.sjoholm.Interfaces.IGameBoard;
import olof.sjoholm.Utils.Direction;
import olof.sjoholm.Interfaces.MovableToken;

public class PlayerToken extends GameBoardActor implements MovableToken {
    private float stepDelay = .25f;
    private float stepSpeed = .75f;
    private Callback finishedCallback;
    private TankAnimation tankAnimation;

    {
        setOrigin(getWidth() / 2, getHeight() / 2);
        tankAnimation = new TankAnimation(this);
        setDrawable(tankAnimation);
    }

    @Override
    protected void positionChanged() {
        setOrigin(getWidth() / 2, getHeight() / 2);
    }

    private final Action finishedAction = new Action() {
        @Override
        public boolean act(float delta) {
            setColor(Color.WHITE);
            updateToBoardPosition();

            if (finishedCallback != null) {
                finishedCallback.callback();
            }
            return true;
        }
    };

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

    @Override
    public void move(int steps, Direction direction, Callback finishedCallback) {
        this.finishedCallback = finishedCallback;

        Vector2 currentDir = new Vector2(
                MathUtils.cos(getRotation()),
                MathUtils.sin(getRotation())
        );
        Vector2 newDirection;
        switch (direction) {
            case UP:
                newDirection = new Vector2(currentDir);
                break;
            case LEFT:
                newDirection = new Vector2(currentDir).rotate90(1);
                break;
            case RIGHT:
                newDirection = new Vector2(currentDir).rotate90(-1);
                break;
            case DOWN:
                newDirection = new Vector2(currentDir).scl(-1);
                break;
            default:
                throw new IllegalArgumentException("Illegal direction " + direction);
        }

        Logger.d("Current direction " + currentDir + " -> " + direction + " -> " + newDirection);

        // TODO: rewrite. I guess this is a card action, so create card action. It should probably
        // be the gameboard that should move this token. Because it knows the map, the players etc.
        int possibleSteps = 100;

        int actualSteps = (possibleSteps > steps) ? steps : possibleSteps;

        int waitSteps = steps - actualSteps;
        
        setBoardX(getBoardX() + (int) newDirection.x * actualSteps);
        setBoardY(getBoardY() + (int) newDirection.y * actualSteps);

        SequenceAction moveSequence = Actions.sequence(
                getMovementSequence(actualSteps, newDirection),
                getWaitSequence(waitSteps),
                finishedAction
        );
        addAction(moveSequence);
    }

    @Override
    public void rotate(Rotation rotation, Callback finishedCallback) {
        this.finishedCallback = finishedCallback;

        SequenceAction sequence = Actions.sequence(
                Actions.rotateBy(
                        rotation.degrees,
                        stepSpeed * rotation.duration,
                        Interpolation.linear
                ),
                Actions.delay(stepDelay),
                finishedAction
        );
        addAction(sequence);
    }

    private Action getMovementSequence(int steps, Vector2 direction) {
        if (steps == 0) {
            return Actions.delay(0f);
        }

        Vector2 movement = new Vector2(direction.x, direction.y).scl(stepSize);

        SequenceAction actions = new SequenceAction();
        for (int i = 0; i < steps; i++) {
            actions.addAction(getSingleMoveSequence(movement));
        }
        return actions;
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

    private SequenceAction getSingleMoveSequence(Vector2 movement) {
        return Actions.sequence(
                animationResumeAction,
                Actions.moveBy(movement.x, movement.y, stepSpeed, Interpolation.linear),
                animationPauseAction,
                Actions.delay(stepDelay)
        );
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
