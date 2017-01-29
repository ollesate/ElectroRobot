package olof.sjoholm.Net.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import olof.sjoholm.Interfaces.*;
import olof.sjoholm.Net.Both.Client;
import olof.sjoholm.Net.Envelope;
import olof.sjoholm.Net.Server.Server;

public class GameServer {
    private final Map<Long, olof.sjoholm.Interfaces.IPlayerApi> players = new ConcurrentHashMap<Long, olof.sjoholm.Interfaces.IPlayerApi>();
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
                olof.sjoholm.Utils.Robo.broadcast(envelope, clientId);
            }
        });
    }

    public List<olof.sjoholm.Interfaces.IPlayerApi> getConnectedPlayers() {
        synchronized (players) {
            return new ArrayList<olof.sjoholm.Interfaces.IPlayerApi>(players.values());
        }
    }

    public void broadcast(Envelope envelope) {
        server.broadcast(envelope);
    }
}
