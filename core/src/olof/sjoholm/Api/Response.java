package olof.sjoholm.Api;

import olof.sjoholm.Net.Both.Envelope;

/**
 * A response to a request. The response will use the same identifier as the requst.
 */
public class Response {
    public Envelope envelope;
    public final long id;

    public Response(Envelope responseEnvelope, Request request) {
        this.envelope = responseEnvelope;
        this.id = request.id;
    }
}
