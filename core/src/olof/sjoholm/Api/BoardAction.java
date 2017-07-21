package olof.sjoholm.Api;

import olof.sjoholm.GameWorld.Actors.PlayerToken;
import olof.sjoholm.Net.Both.Envelope;
import olof.sjoholm.Utils.Direction;

public interface BoardAction {

    void perform(PlayerToken playerToken);

    class MoveForward implements BoardAction {
        private int steps;

        public MoveForward(int steps) {
            this.steps = steps;
        }

        @Override
        public void perform(PlayerToken playerToken) {
            playerToken.move(Direction.FORWARD, 2);
        }
    }
}