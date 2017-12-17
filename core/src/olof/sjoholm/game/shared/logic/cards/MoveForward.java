package olof.sjoholm.game.shared.logic.cards;


import com.badlogic.gdx.scenes.scene2d.Action;

import olof.sjoholm.game.shared.logic.Movement;
import olof.sjoholm.game.shared.objects.PlayerToken;

public class MoveForward extends BoardAction {
    private int steps;

    public MoveForward(int steps) {
        this.steps = steps;
    }

    @Override
    public Action getAction(PlayerToken playerToken) {
        return playerToken.getPushMoveAction(Movement.FORWARD, steps);
    }

    @Override
    public String getText() {
        return String.format("Move forward %s steps", steps);
    }
}
