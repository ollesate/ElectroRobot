package olof.sjoholm.GameWorld.Actors.Cards;

import olof.sjoholm.GameWorld.Utils.Direction;
import olof.sjoholm.Interfaces.Action;
import olof.sjoholm.Interfaces.Callback;
import olof.sjoholm.Interfaces.MovableToken;

public class MoveAction implements Action {
    private final Direction direction;
    private final int steps;

    public MoveAction(Direction direction, int steps) {
        this.direction = direction;
        this.steps = steps;
    }

    @Override
    public void apply(MovableToken movableToken, final Callback finishedCallback) {
        movableToken.move(steps, direction, new Callback() {
            @Override
            public void callback() {
                finishedCallback.callback();
            }
        });
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return super.toString() + ", steps " + steps + ", direction " + direction;
    }
}
