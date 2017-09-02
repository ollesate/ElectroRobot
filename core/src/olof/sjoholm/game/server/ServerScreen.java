package olof.sjoholm.game.server;

import com.badlogic.gdx.Screen;

import java.util.List;

import olof.sjoholm.net.Envelope;
import olof.sjoholm.net.ServerConnection;
import olof.sjoholm.net.Player;

public abstract class ServerScreen implements Screen, ServerConnection.OnMessageListener,
        ServerConnection.OnPlayerConnectedListener, ServerConnection.OnPlayerDisconnectedListener {
    private final ServerConnection serverConnection;
    private final UiDispatcher uiDispatcher;

    public ServerScreen() {
        uiDispatcher = new UiDispatcher(this, this, this);
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
        serverConnection.setOnMessageListener(uiDispatcher);
        serverConnection.setOnPlayerConnectedListener(uiDispatcher);
        serverConnection.setOnPlayerDisconnectedListener(uiDispatcher);
        serverConnection.openConnection();
    }

    public void disconnect() {
        serverConnection.setOnMessageListener(null);
        serverConnection.setOnPlayerConnectedListener(null);
        serverConnection.setOnPlayerDisconnectedListener(null);
        serverConnection.disconnect();
    }

    public void broadcast(Envelope envelope) {
        serverConnection.broadcast(envelope);
    }

    public String getHostName() {
        return serverConnection.getHostName();
    }

    public void send(Player player, Envelope envelope) {
        serverConnection.send(player, envelope);
    }

    public List<Player> getConnectedPlayers() {
        return serverConnection.getConnectedPlayers();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void resize(int width, int height) {
    }
}
