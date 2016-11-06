package olof.sjoholm.GameWorld.Client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.GameWorld.Client.Screens.GameScreen;
import olof.sjoholm.GameWorld.Client.Screens.LobbyScreen;
import olof.sjoholm.GameWorld.Net.OnMessageReceivedListener;
import olof.sjoholm.GameWorld.Server.Player;
import olof.sjoholm.GameWorld.Utils.Logger;
import olof.sjoholm.Interfaces.ICard;
import olof.sjoholm.Net.Both.Client;
import olof.sjoholm.Net.Both.OnMessageListener;
import olof.sjoholm.Net.Both.Protocol;
import olof.sjoholm.Net.Envelope;
import olof.sjoholm.Net.ServerConstants;

/**
 * Created by sjoholm on 09/10/16.
 */

public class ClientGameHandler {
    private Game game;
    private final LobbyScreen lobbyScreen;
    private final GameScreen gameScreen;
    private ClientHandler clientHandler;

    public ClientGameHandler(Game game) {
        this.game = game;
        lobbyScreen = new LobbyScreen();
        game.setScreen(lobbyScreen);
        connectToServer();

        PlayerHandler playerHandler = new PlayerHandler(clientHandler);
        gameScreen = new GameScreen(playerHandler);
    }

    private void connectToServer() {
        Client serverConnection = new Client(
                ServerConstants.HOST_NAME,
                ServerConstants.CONNECTION_PORT
        );
        serverConnection.startReading();
        clientHandler = new ClientHandler(serverConnection);
        clientHandler.addOnMessageReceivedListener(new OnMessageReceivedListener() {
            @Override
            public void onMessage(Envelope envelope, Long clientId) {
                onMessageReceived(envelope);
            }
        });
    }

    private static class ClientHandler {
        private final List<OnMessageReceivedListener> listenerList;
        private Client client;

        public ClientHandler(Client client) {
            this.client = client;
            listenerList = new ArrayList<OnMessageReceivedListener>();
            client.setOnMessageListener(new OnMessageListener() {
                @Override
                public void onMessage(Client client, Envelope envelope) {
                    synchronized (listenerList) {
                        for (OnMessageReceivedListener listener : listenerList) {
                            listener.onMessage(envelope, client.getId());
                        }
                    }
                }
            });
        }

        public void addOnMessageReceivedListener(OnMessageReceivedListener listener) {
            listenerList.add(listener);
        }

        public void send(Envelope.SendCards sendCards) {
            client.sendData(sendCards);
        }
    }

    private void onMessageReceived(Envelope envelope) {
        Logger.d("onMessage received " + envelope.toString());

        if (envelope instanceof Envelope.Message) {
            messageProtocol(envelope.getContents(String.class));
        }
    }

    private void messageProtocol(String message) {
        if (Protocol.START_GAME.equals(message)) {
            startGame();
        }
    }

    private void startGame() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                game.setScreen(gameScreen);
            }
        });
    }

    public static class PlayerHandler {
        private Player player;

        public PlayerHandler(final ClientHandler clientHandler) {
            clientHandler.addOnMessageReceivedListener(new OnMessageReceivedListener() {
                @Override
                public void onMessage(final Envelope envelope, Long clientId) {
                    if (envelope instanceof Envelope.SendCards) {
                        Logger.d("Server sent me cards");
                        List<ICard> list = envelope.getContents(List.class);
                        player.dealCards(list);
                    } else if (envelope instanceof Envelope.RequestCards) {
                        Logger.d("Server requests my cards");
                        player.getCards(new Player.OnCardsReceivedListener() {
                            @Override
                            public void onCardsReceived(List<ICard> cards) {
                                Envelope.SendCards sendCards = new Envelope.SendCards(cards);
                                sendCards.tagWithResponseId(envelope.getResponseId());
                                clientHandler.send(sendCards);
                            }
                        });
                    }
                }
            });
        }

        public void setPlayer(Player player) {
            this.player = player;
        }

    }

    public interface ICardProvider {

        void setPlayer(Player player);
    }
}
