package olof.sjoholm.Api;

import com.badlogic.gdx.Screen;

import olof.sjoholm.Net.Both.Envelope;
import olof.sjoholm.Net.Server.ServerConnection;
import olof.sjoholm.Net.Server.Player;

public abstract class ServerScreen implements Screen, ServerConnection.OnMessageListener,
        ServerConnection.OnPlayerConnectedListener, ServerConnection.OnPlayerDisconnectedListener {
    private final ServerConnection serverConnection;

    public ServerScreen() {
        serverConnection = ServerConnection.getInstance();
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public void startServer() {
        serverConnection.openConnection();
        serverConnection.setOnMessageListener(this);
        serverConnection.setOnPlayerConnectedListener(this);
        serverConnection.setOnPlayerDisconnectedListener(this);
    }

    public void disconnect() {
        serverConnection.disconnect();
        serverConnection.openConnection();
        serverConnection.setOnMessageListener(null);
        serverConnection.setOnPlayerConnectedListener(null);
        serverConnection.setOnPlayerDisconnectedListener(null);
    }

    public void broadcast(Envelope envelope) {
        serverConnection.broadcast(envelope);
    }

    public void send(Player player, Envelope envelope) {
        serverConnection.send(player, envelope);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void resize(int width, int height) {}
}
