package olof.sjoholm.GameWorld.Server;

import com.badlogic.gdx.Game;

import olof.sjoholm.GameWorld.Net.ServerConnection;
import olof.sjoholm.GameWorld.Server.Screens.GameScreen;
import olof.sjoholm.GameWorld.Server.Screens.LobbyScreen;
import olof.sjoholm.GameWorld.Utils.Logger;
import olof.sjoholm.Net.Both.Client;
import olof.sjoholm.Net.Both.Protocol;
import olof.sjoholm.Net.Envelope;
import olof.sjoholm.Net.Server.OnClientStateUpdated;

/**
 * Created by sjoholm on 02/10/16.
 */

public class ServerGame implements LobbyScreen.LobbyActions {
    private final Game game;
    private final LobbyScreen lobbyScreen;

    public ServerGame(Game game) {
        this.game = game;
        lobbyScreen = new LobbyScreen(this);

        game.setScreen(lobbyScreen);

        Logger.d("Starting game server");
        ServerConnection.startServer();
        ServerConnection.addOnDisconnectedListener(new OnClientStateUpdated() {
            @Override
            public void onClientConnected(Client client) {
                lobbyScreen.onPlayersUpdated(ServerConnection.getNrConnectedPlayers());
            }

            @Override
            public void onClientDisconnected(Client client) {
                lobbyScreen.onPlayersUpdated(ServerConnection.getNrConnectedPlayers());
            }
        });
    }

    @Override
    public void onStartGame() {
        ServerConnection.send(new Envelope.Message(Protocol.START_GAME));
        game.setScreen(new GameScreen());
    }

}
