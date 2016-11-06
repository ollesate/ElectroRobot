package olof.sjoholm.GameWorld.Client;

import olof.sjoholm.GameWorld.Net.OnMessageReceivedListener;
import olof.sjoholm.Net.Envelope;

public interface ClientApi {

    void sendMessage(Envelope envelope, Long clientId);

    void addOnMessageListener(OnMessageReceivedListener listener);

}
