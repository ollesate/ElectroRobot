package olof.sjoholm.Client;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.Interfaces.OnMessageReceivedListener;
import olof.sjoholm.Net.Both.Client;
import olof.sjoholm.Net.Both.OnMessageListener;
import olof.sjoholm.Net.Envelope;

/**
 * Created by sjoholm on 23/12/16.
 */
class ServerConnection {
    private final List<OnMessageReceivedListener> listeners = new ArrayList<OnMessageReceivedListener>();
    private Client serverClient;

    public ServerConnection(String host, int port) {
        serverClient = new Client(host, port);
        serverClient.startReading();
        serverClient.setOnMessageListener(new OnMessageListener() {
            @Override
            public void onMessage(Client client, Envelope envelope) {
                synchronized (listeners) {
                    for (OnMessageReceivedListener listener : listeners) {
                        listener.onMessage(envelope, client.getId());
                    }
                }
            }
        });
    }

    public void addOnMessageReceivedListener(OnMessageReceivedListener listener) {
        listeners.add(listener);
    }

    public void send(Envelope.SendCards sendCards) {
        serverClient.sendData(sendCards);
    }
}
