package olof.sjoholm.GameWorld.Server;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

import olof.sjoholm.GameWorld.Net.OnMessageReceivedListener;
import olof.sjoholm.GameWorld.Utils.Logger;
import olof.sjoholm.Interfaces.ICard;
import olof.sjoholm.Net.Both.Client;
import olof.sjoholm.Net.Both.Protocol;
import olof.sjoholm.Net.Envelope;
import olof.sjoholm.Net.Server.Server;

public class GameServer implements PlayerManager {
    private final Map<Long, Player> players;
    private Server server;

    public GameServer(Server server) {
        this.server = server;
        players = new HashMap<Long, Player>();
        List<Client> clients = server.getConnectedClients();
        for (Client client : clients) {
            players.put(client.getId(), new PlayerApi(client));
        }

        server.addOnMessageListener(new OnMessageReceivedListener() {
            @Override
            public void onMessage(Envelope envelope, Long clientId) {
                if (envelope instanceof Envelope.ClientConnection) {
                    synchronized (players) {
                        players.put(clientId, new PlayerApi(envelope.getContents(Client.class)));
                    }
                } else if (envelope instanceof Envelope.ClientDisconnection) {
                    synchronized (players) {
                        players.remove(clientId);
                    }
                }
            }
        });
    }

    @Override
    public List<Player> getPlayers() {
        synchronized (players) {
            return new ArrayList<Player>(players.values());
        }
    }

    @Override
    public void alertStartGame() {
        server.broadcast(new Envelope.Message(Protocol.START_GAME));
    }

    private static class PlayerApi extends PlayerCardManager {
        private Client client;

        PlayerApi(Client client) {
            this.client = client;
        }

        @Override
        public void dealCards(List<ICard> cards) {
            super.dealCards(cards);
            client.sendData(new Envelope.SendCards(cards));
        }

        @Override
        public void getCards(final OnCardsReceivedListener onCardsReceivedListener) {
            client.sendData(new Envelope.RequestCards(), new Client.OnResponseCallback() {
                @Override
                public void onResponse(Envelope envelope) {
                    if (envelope instanceof Envelope.SendCards) {
                        List<ICard> cards = envelope.getContents(List.class);
                        PlayerApi.super.updateCards(cards);
                        onCardsReceivedListener.onCardsReceived(cards);
                        Logger.d("Received card from player");
                        for (ICard card : cards) {
                            Logger.d("Card->" + card.toString());
                        }
                    } else {
                        throw new IllegalStateException(
                                "getCards() did not receive response envelope of type " +
                                        Envelope.SendCards.class.getSimpleName()
                        );
                    }
                }
            });
        }
    }

    private abstract static class PlayerCardManager implements Player {
        private Queue<ICard> cards;

        {
            cards = new ArrayDeque<ICard>();
        }

        @Override
        public void dealCards(List<ICard> cards) {
            updateCards(cards);
        }

        private void updateCards(List<ICard> cards) {
            this.cards.clear();
            this.cards.addAll(cards);
        }

        @Override
        public boolean hasCards() {
            return cards.size() > 0;
        }

        @Override
        public ICard popTopCard() {
            return cards.poll();
        }
    }
}
