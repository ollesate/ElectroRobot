package olof.sjoholm.Api;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Json;

import java.io.Serializable;

import olof.sjoholm.GameWorld.Actors.PlayerToken;
import olof.sjoholm.Net.Both.Envelope;
import olof.sjoholm.Utils.Direction;
import olof.sjoholm.Utils.Rotation;

public abstract class BoardAction implements Serializable {
    private int id;

    public abstract Action perform(PlayerToken playerToken);

    public abstract String getText();

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static class MoveForward extends BoardAction {
        private int steps;

        public MoveForward(int steps) {
            this.steps = steps;
        }

        @Override
        public Action perform(PlayerToken playerToken) {
            return playerToken.move(Direction.FORWARD, steps);
        }

        @Override
        public String getText() {
            return String.format("Move forward %s steps", steps);
        }
    }

    public static class Rotate extends BoardAction {
        private Rotation rotation;

        public Rotate(Rotation rotation) {
            this.rotation = rotation;
        }

        @Override
        public Action perform(PlayerToken playerToken) {
            return playerToken.rotate(rotation);
        }

        @Override
        public String getText() {
            if (Rotation.UTURN.equals(rotation)) {
                return "Perform U-TURN";
            }
            return "Rotate " + rotation;
        }
    }
}
