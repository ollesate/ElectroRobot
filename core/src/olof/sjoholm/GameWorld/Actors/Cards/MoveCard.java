package olof.sjoholm.GameWorld.Actors.Cards;

import olof.sjoholm.GameWorld.Utils.Direction;
import olof.sjoholm.Interfaces.Callback;
import olof.sjoholm.Interfaces.MovableToken;
import olof.sjoholm.common.Card;
import olof.sjoholm.common.CardModel;

public class MoveCard extends Card {
    private final Direction direction;
    private final int steps;

    public MoveCard(CardModel cardModel, Direction direction, int steps) {
        super(cardModel);
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

    public int getSteps() {
        return steps;
    }

    @Override
    public String toString() {
        return super.toString() + ", steps " + steps + ", direction " + direction;
    }
}
