package olof.sjoholm.GameWorld.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import olof.sjoholm.GameWorld.Net.OnMessageReceivedListener;
import olof.sjoholm.Net.Both.Client;
import olof.sjoholm.Net.Envelope;
import olof.sjoholm.Net.Server.Server;

public class GameServer {
    private final Map<Long, PlayerApi> players = new ConcurrentHashMap<Long, PlayerApi>();
    private Server server;

    public GameServer(Server server) {
        this.server = server;
        List<Client> clients = server.getConnectedClients();
        for (Client client : clients) {
            players.put(client.getId(), new PlayerApi(client));
        }

        server.addOnMessageListener(new OnMessageReceivedListener() {
            @Override
            public void onMessage(Envelope envelope, Long clientId) {
                if (envelope instanceof Envelope.ClientConnection) {
                    players.put(clientId, new PlayerApi(envelope.getContents(Client.class)));
                } else if (envelope instanceof Envelope.ClientDisconnection) {
                    players.remove(clientId);
                }
            }
        });
    }

    public List<PlayerApi> getConnectedPlayers() {
        synchronized (players) {
            return new ArrayList<PlayerApi>(players.values());
        }
    }

    public void broadcast(Envelope envelope) {
        server.broadcast(envelope);
    }
}
