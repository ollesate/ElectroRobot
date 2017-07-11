package olof.sjoholm.Api;

import java.util.concurrent.atomic.AtomicLong;

import olof.sjoholm.Net.Both.Envelope;

/**
 * Creates a request with a unique id from the sender (note not globally). Receiver can use this id
 * to give a response.
 */
public class Request {
    private static final AtomicLong idCounter = new AtomicLong();

    public final Envelope envelope;
    public final long id;

    public Request (Envelope envelope) {
        this.envelope = envelope;
        id = idCounter.incrementAndGet();
    }

    @Override
    public String toString() {
        return "id: " + id + ", " + envelope.toString();
    }
}