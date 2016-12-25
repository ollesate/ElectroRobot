package olof.sjoholm.common;

import olof.sjoholm.GameWorld.Utils.Direction;


public class MoveModel extends CardModel {
    public final Direction direction;
    public final int steps;

    public MoveModel(String type, int priority, Direction direction, int steps) {
        super(type, priority);
        this.direction = direction;
        this.steps = steps;
    }

}
