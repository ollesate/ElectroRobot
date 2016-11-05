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
    private Long counter;

    ClientManager() {
        clients = new ArrayList<olof.sjoholm.Net.Both.Client>();
    }

    void addClientAndAssignId(olof.sjoholm.Net.Both.Client client) {
        client.setId(counter++);

        synchronized (clients) {
            clients.add(client);
            client.startReading();
        }
    }

    int getSize() {
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

    public List<Client> getClients() {
        return clients;
    }
}
