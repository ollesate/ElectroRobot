package olof.sjoholm.Net.Server;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.GameWorld.Utils.Logger;
import olof.sjoholm.Net.Both.Client;
import olof.sjoholm.Net.Both.OnMessageListener;
import olof.sjoholm.Net.Envelope;

/**
 * Created by sjoholm on 02/10/16.
 */

class ClientManager {
    private final List<olof.sjoholm.Net.Both.Client> clients;
    private OnMessageListener listener;
    private Client.OnDisconnectedListener onDisconnectedListener;

    ClientManager(OnMessageListener listener, Client.OnDisconnectedListener onDisconnectedListener) {
        this.listener = listener;
        this.onDisconnectedListener = onDisconnectedListener;
        clients = new ArrayList<olof.sjoholm.Net.Both.Client>();
    }

    void addClient(olof.sjoholm.Net.Both.Client client) {
        synchronized (clients) {
            clients.add(client);
            client.setOnMessageListener(new OnMessageListener() {
                @Override
                public void onMessage(olof.sjoholm.Net.Both.Client client, Envelope envelope) {
                    listener.onMessage(client, envelope);
                }
            });
            client.setOnDisconnectedListener(new Client.OnDisconnectedListener() {
                @Override
                public void onDisconnected(Client client) {
                    clients.remove(client);
                    onDisconnectedListener.onDisconnected(client);
                }
            });
            client.startReading();
        }
    }

    int getClientsSize() {
        return clients.size();
    }

    void broadcast(Envelope envelope) {
        Logger.d("broadcast");
        synchronized (clients) {
            for (olof.sjoholm.Net.Both.Client client : clients) {
                if (client != null) {
                    Logger.d("send to client");
                    client.sendData(envelope);
                }
            }
        }
    }

}
