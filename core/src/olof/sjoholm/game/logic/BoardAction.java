package olof.sjoholm.game.logic;

import com.badlogic.gdx.scenes.scene2d.Action;

import java.io.Serializable;

import olof.sjoholm.game.objects.PlayerToken;

public abstract class BoardAction implements Serializable {
    private int id;

    public abstract Action getAction(PlayerToken playerToken);

    public abstract String getText();

    public void setId(int id) {
        // TODO: make package protected.
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
        public Action getAction(PlayerToken playerToken) {
            return playerToken.move(Movement.FORWARD, steps);
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
        public Action getAction(PlayerToken playerToken) {
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

    public static class Shoot extends BoardAction {

        @Override
        public Action getAction(PlayerToken playerToken) {
            return playerToken.getShootAction();
        }

        @Override
        public String getText() {
            return "Shoot";
        }
    }
}
