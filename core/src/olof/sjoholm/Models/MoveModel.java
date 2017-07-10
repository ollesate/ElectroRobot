package olof.sjoholm.Models;

import olof.sjoholm.Utils.Direction;


public class MoveModel extends CardModel {
    public Direction direction;
    public int steps;

    public MoveModel() {

    }

    public MoveModel(Direction direction, int steps) {
        this.direction = direction;
        this.steps = steps;
    }
}