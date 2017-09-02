package olof.sjoholm.net;

import com.badlogic.gdx.graphics.Color;

import java.io.Serializable;
import java.util.List;

import olof.sjoholm.game.logic.BoardAction;

public class Envelope implements Serializable {

    @Override
    public String toString() {
        return "Envelope-> Type: " + getClass().getSimpleName() + ", Content: ";
    }

    public static class SendCards extends Envelope {
        public final List<BoardAction> cards;

        public SendCards(List<BoardAction> cards) {
            this.cards = cards;
        }
    }

    public static class StartCountdown extends Envelope {
        public final float time;

        public StartCountdown(float time) {
            this.time = time;
        }
    }

    public static class OnCardActivated extends Envelope {
        public final BoardAction boardAction;

        public OnCardActivated(BoardAction boardAction) {
            this.boardAction = boardAction;
        }
    }

    public static class OnCardDeactivated extends Envelope {
        public final BoardAction boardAction;

        public OnCardDeactivated(BoardAction boardAction) {
            this.boardAction = boardAction;
        }
    }

    public static class PlayerSelectColor extends Envelope {
        private final float r;
        private final float g;
        private final float b;
        private final float a;

        public PlayerSelectColor(Color color) {
            r = color.r;
            g = color.g;
            b = color.b;
            a = color.a;
        }

        public Color getColor() {
            return new Color(r, g, b, a);
        }
    }

    public static class PlayerSelectName extends Envelope {
        public final String name;

        public PlayerSelectName(String name) {
            this.name = name;
        }
    }

    public static class PlayerReady extends Envelope {
        public final boolean ready;

        public PlayerReady(boolean ready) {
            this.ready = ready;
        }
    }

    public static class StartGame extends Envelope {

    }

    public static class OnCardPhaseEnded extends Envelope {

    }

    public static class UnReadyMyCards extends Envelope {

    }

    public static class UpdateDamage extends Envelope {
        public final int damage;

        public UpdateDamage(int damage) {
            this.damage = damage;
        }
    }
}
