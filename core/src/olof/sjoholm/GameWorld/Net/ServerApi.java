package olof.sjoholm.GameWorld.Net;

import olof.sjoholm.Net.Envelope;

public interface ServerApi {

    void broadcast(Envelope envelope);

    void sendMessage(Envelope envelope, Long clientId);

    void addOnMessageListener(OnMessageReceivedListener listener);

}
