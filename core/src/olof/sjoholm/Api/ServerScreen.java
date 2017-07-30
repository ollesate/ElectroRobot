package olof.sjoholm.Api;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import olof.sjoholm.Net.Both.Envelope;
import olof.sjoholm.Net.Server.ServerConnection;
import olof.sjoholm.Net.Server.Player;

public abstract class ServerScreen implements Screen, ServerConnection.OnMessageListener,
        ServerConnection.OnPlayerConnectedListener, ServerConnection.OnPlayerDisconnectedListener {
    private final ServerConnection serverConnection;

    public ServerScreen() {
        serverConnection = ServerConnection.getInstance();
        serverConnection.setOnMessageListener(this);
        serverConnection.setOnPlayerConnectedListener(this);
        serverConnection.setOnPlayerDisconnectedListener(this);
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
        serverConnection.setOnMessageListener(this);
        serverConnection.setOnPlayerConnectedListener(this);
        serverConnection.setOnPlayerDisconnectedListener(this);
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
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void resize(int width, int height) {}

    @Override
    public void onMessage(final Player player, final Envelope envelope) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                onHandleMessage(player, envelope);
            }
        });
    }

    protected abstract void onHandleMessage(Player player, Envelope envelope);

    @Override
    public void onPlayerConnected(final Player player) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                onHandlePlayerConnected(player);
            }
        });
    }

    protected abstract void onHandlePlayerConnected(Player player);

    @Override
    public void onPlayerDisconnected(final Player player) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                onHandlePlayerDisconnected(player);
            }
        });
    }

    protected abstract void onHandlePlayerDisconnected(Player player);
}
