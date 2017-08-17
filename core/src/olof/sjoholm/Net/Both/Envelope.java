package olof.sjoholm.Net.Both;

import com.badlogic.gdx.graphics.Color;

import java.io.Serializable;
import java.util.List;

import olof.sjoholm.Api.BoardAction;

public class Envelope implements Serializable {
    private Long responseId = -1L;

    private final Object contents;
    private int ownerId;

    public Envelope() {
        contents = null;
    }

    Envelope(final Object contents) {
        this.contents = contents;
    }

    public <T> T getContents(Class<T> clazz) {
        return clazz.cast(contents);
    }

    public Long getResponseId() {
        return responseId;
    }

    public void tagWithResponseId(Long responseId) {
        this.responseId = responseId;
    }

    @Override
    public String toString() {
        return "Envelope-> Type: " + getClass().getSimpleName() + ", Content: " + (contents != null ? contents.toString() : "null") + ", responseId " + responseId;
    }

    /**
     * Package protected so this can not be changed.
     */
    void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public static class Welcome extends Envelope {
        public final int id;

        public Welcome(int id) {
            super(id);
            this.id = id;
        }
    }

    public static class ClientConnection extends Envelope {

        public ClientConnection(NetClient netClient) {
            super(netClient);
        }
    }

    public static class ClientDisconnection extends Envelope {

        public ClientDisconnection(NetClient netClient) {
            super(netClient);
        }
    }

    public static class SendCards extends Envelope {
        public final List<BoardAction> cards;

        public SendCards(List<BoardAction> cards) {
            super(cards);
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
