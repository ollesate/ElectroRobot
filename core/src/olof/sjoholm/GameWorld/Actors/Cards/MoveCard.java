package olof.sjoholm.GameWorld.Actors.Cards;

import olof.sjoholm.GameWorld.Utils.Direction;
import olof.sjoholm.Interfaces.Callback;
import olof.sjoholm.Interfaces.ICard;
import olof.sjoholm.Interfaces.MovableToken;

public class MoveCard implements ICard {
    private int steps;
    private Direction direction;

    public MoveCard() {
        steps = (int)(Math.random() * 4 + 1);
        direction = Direction.random();
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

    @Override
    public int getPriority() {
        return steps;
    }

    @Override
    public String getType() {
        return MoveCard.class.getSimpleName();
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return super.toString() + ", steps " + steps + ", direction " + direction;
    }
}
