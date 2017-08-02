package olof.sjoholm.Api;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import olof.sjoholm.Net.Both.Envelope;
import olof.sjoholm.Net.Server.ServerConnection;
import olof.sjoholm.Net.Server.Player;

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

    public void send(Player player, Envelope envelope) {
        serverConnection.send(player, envelope);
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
