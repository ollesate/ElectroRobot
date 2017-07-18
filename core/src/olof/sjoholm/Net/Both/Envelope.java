package olof.sjoholm.Net.Both;

import java.io.Serializable;
import java.util.List;

import olof.sjoholm.Models.CardModel;

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

        public SendCards(List<CardModel> cards) {
            super(cards);
        }
    }

    public static class RequestCards extends Envelope {

    }

    public static class StartGame extends Envelope {

    }
}
