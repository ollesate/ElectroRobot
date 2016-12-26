package olof.sjoholm.GameWorld.Server;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import olof.sjoholm.Client.CardHandModel;
import olof.sjoholm.GameWorld.Net.OnMessageReceivedListener;
import olof.sjoholm.GameWorld.Utils.Logger;
import olof.sjoholm.Interfaces.Action;
import olof.sjoholm.Net.Both.Client;
import olof.sjoholm.Net.Both.Protocol;
import olof.sjoholm.Net.Envelope;
import olof.sjoholm.Net.Server.Server;
import olof.sjoholm.common.CardModel;

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
        public void dealCards(List<Action> cards) {
            super.dealCards(cards);
            client.sendData(new Envelope.SendCards(cards));
        }

        @Override
        public void getCards(final OnCardsReceivedListener onCardsReceivedListener) {
            client.sendData(new Envelope.RequestCards(), new Client.OnResponseCallback() {
                @Override
                public void onResponse(Envelope envelope) {
                    if (envelope instanceof Envelope.SendCards) {
                        List<Action> cards = envelope.getContents(List.class);
                        PlayerApi.super.updateCards(cards);
                        onCardsReceivedListener.onCardsReceived(cards);
                        Logger.d("Received card from player");
                        for (Action card : cards) {
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
        private Queue<Action> cards;

        {
            cards = new ArrayDeque<Action>();
        }

        @Override
        public void dealCards(List<Action> cards) {
            updateCards(cards);
        }

        private void updateCards(List<Action> cards) {
            this.cards.clear();
            this.cards.addAll(cards);
        }

        @Override
        public boolean hasCards() {
            return cards.size() > 0;
        }

        @Override
        public Action popTopCard() {
            return cards.poll();
        }
    }

    private static class PlayerApi2 {

        public void dealCards(List<CardModel> cards) {

        }

        public void getCards(Player.OnCardsReceivedListener onCardsReceivedListener) {

        }
    }

    private static class PlayerController {
        private final CardHandModel cardHandModel;

        public PlayerController(PlayerApi playerApi, CardHandModel cardHandModel) {
            this.cardHandModel = cardHandModel;
        }

        public void dealCards(List<CardHandModel> cards) {

        }

        public void getCards(Player.OnCardsReceivedListener onCardsReceivedListener) {

        }

        public boolean hasCards() {
            return false;
        }

        public Action popTopCard() {
            return null;
        }
    }

}
