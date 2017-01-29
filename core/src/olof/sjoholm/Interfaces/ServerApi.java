package olof.sjoholm.Interfaces;

import olof.sjoholm.Net.Envelope;

public interface ServerApi {

    void broadcast(Envelope envelope);

    void sendMessage(Envelope envelope, Long clientId);

    void addOnMessageListener(olof.sjoholm.Interfaces.OnMessageReceivedListener listener);

}
