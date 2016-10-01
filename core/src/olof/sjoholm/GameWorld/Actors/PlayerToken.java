package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import olof.sjoholm.GameWorld.Utils.TextureDrawable;
import olof.sjoholm.Interfaces.Callback;
import olof.sjoholm.Interfaces.IGameBoard;
import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.GameWorld.Utils.Constants;
import olof.sjoholm.GameWorld.Utils.Direction;
import olof.sjoholm.Interfaces.MovableToken;

/**
 * Created by sjoholm on 25/09/16.
 */
public class PlayerToken extends GameBoardActor implements MovableToken {
    private float stepSize = Constants.STEP_SIZE;
    private float stepDelay = .75f;
    private float stepSpeed = .25f;
    private Callback finishedCallback;
    private Vector2 currentMovement;
    private IGameBoard gameBoard;

    {
        currentMovement = new Vector2();
        setDrawable(new TextureDrawable(this, Textures.origTexture));
        setSize(Constants.STEP_SIZE, Constants.STEP_SIZE);
    }

    private final Action finishedAction = new Action() {
        @Override
        public boolean act(float delta) {
            finishedCallback.callback();
            currentMovement.setZero();
            setColor(Color.WHITE);
            setAnimating(false);
            return true;
        }
    };

    public PlayerToken(IGameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    @Override
    public void move(int steps, Direction direction, Callback finishedCallback) {
        this.finishedCallback = finishedCallback;
        setAnimating(true);

        int possibleSteps = gameBoard.getPossibleSteps(direction, getBoardX(), getBoardY());

        int actualSteps = (possibleSteps > steps) ? steps : possibleSteps;

        int waitSteps = steps - actualSteps;

//        Logger.d("--");
//        Logger.d("Position " + getBoardX() + ", " + getBoardY());
//        Logger.d("Steps " + steps + ", actual " + actualSteps + ", wait " + waitSteps);

        setBoardX(getBoardX() + direction.x * actualSteps);
        setBoardY(getBoardY() + direction.y * actualSteps);

        setColor(Color.BLUE);

        SequenceAction moveSequence = Actions.sequence(
                getMovementSequence(actualSteps, direction),
                getWaitSequence(waitSteps),
                finishedAction
        );
        addAction(moveSequence);
    }

    private Action getMovementSequence(int steps, Direction direction) {
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
                Actions.moveBy(movement.x, movement.y, stepSpeed, Interpolation.linear),
                Actions.delay(stepDelay)
        );
    }

    private Action getSingleWaitSequence() {
        return Actions.delay(stepDelay + stepSpeed);
    }
}
