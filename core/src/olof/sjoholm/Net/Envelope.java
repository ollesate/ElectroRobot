package olof.sjoholm.Net;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import olof.sjoholm.Net.Both.Client;
import olof.sjoholm.common.CardModel;

public class Envelope implements Serializable {
    private static final AtomicLong idCounter = new AtomicLong(0);
    private Long responseId = -1L;

    private final Object contents;
    private final String type;

    public Envelope(final Object contents, String type) {
        this.contents = contents;
        this.type = type;
    }

    public <T> T getContents(Class<T> clazz) {
        return (T) contents;
    }

    public String getType() {
        return type;
    }

    public Long getResponseId() {
        return responseId;
    }

    /**
     * Tag an envelope with an responseId so we can listen for a response on the same responseId.
     */
    public void tagWithResponseId() {
        responseId = idCounter.addAndGet(1);
    }

    public void tagWithResponseId(Long responseId) {
        this.responseId = responseId;
    }

    @Override
    public String toString() {
        return "Envelope-> Type: " + type + ", Content: " + (contents != null ? contents.toString() : "null") + ", responseId " + responseId;
    }

    public static class Message extends Envelope {
        private static final String type = Message.class.getSimpleName();

        public Message(String contents) {
            super(contents, type);
        }
    }

    public static class Welcome extends Envelope {
        private static final String type = Welcome.class.getSimpleName();

        public Welcome(Long clientId) {
            super(clientId, type);
        }
    }

    public static class ClientConnection extends Envelope {
        private static final String type = ClientConnection.class.getSimpleName();

        public ClientConnection(Client client) {
            super(client, type);
        }
    }

    public static class ClientDisconnection extends Envelope {
        private static final String type = ClientDisconnection.class.getSimpleName();

        public ClientDisconnection(Client client) {
            super(client, type);
        }
    }

    public static class SendCards extends Envelope {
        private static final String type = SendCards.class.getSimpleName();

        public SendCards(List<CardModel> cards) {
            super(cards, type);
        }
    }

    public static class RequestCards extends Envelope {
        private static final String type = RequestCards.class.getSimpleName();

        public RequestCards() {
            super(null, type);
        }
    }
}
