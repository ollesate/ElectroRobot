package olof.sjoholm.GameWorld;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import olof.sjoholm.GameLogic.Callback;
import olof.sjoholm.GameWorld.Actors.BaseActor;
import olof.sjoholm.GameWorld.Actors.Direction;
import olof.sjoholm.GameWorld.Actors.MovableToken;
import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.GameWorld.Utils.Constants;
import olof.sjoholm.GameWorld.Utils.Vector2Util;

/**
 * Created by sjoholm on 25/09/16.
 */
public class PlayerToken extends BaseActor implements MovableToken {
    private int stepSize = Constants.StepSize;
    private float stepDelay = 0.25f;
    private float stepSpeed = 0.5f;
    private Callback finishedCallback;

    {
        setTexture(Textures.origTexture);
        setSize(Constants.StepSize, Constants.StepSize);
    }

    private final Action finishedAction = new Action() {
        @Override
        public boolean act(float delta) {
            finishedCallback.callback();
            return true;
        }
    };

    @Override
    public void move(int steps, Direction direction, Callback finishedCallback) {
        this.finishedCallback = finishedCallback;
        addAction(getMovementSequence(steps, direction));
    }

    private SequenceAction getMovementSequence(int steps, Direction direction) {
        Action[] actions = new Action[steps];
        Vector2 movement = Vector2Util.vectorFromDirection(direction).scl(stepSize);
        for (int i = 0; i < steps; i++) {
            actions[i] = getStepSequence(movement);
        }
        return Actions.sequence(
                Actions.sequence(actions),
                finishedAction
        );
    }

    private SequenceAction getStepSequence(Vector2 movement) {
        return Actions.sequence(
                Actions.moveBy(movement.x, movement.y, stepSpeed, Interpolation.linear),
                Actions.delay(stepDelay)
        );
    }
}
