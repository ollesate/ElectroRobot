package olof.sjoholm.game.player;

import com.badlogic.gdx.Screen;

import olof.sjoholm.game.shared.AppPrefs;
import olof.sjoholm.net.ClientConnection;
import olof.sjoholm.net.Envelope;
import olof.sjoholm.utils.Logger;

public abstract class PlayerScreen implements Screen, ClientConnection.OnMessageListener,
        ClientConnection.OnConnectionListener, ClientConnection.OnDisconnectedListener {
    private final ClientConnection clientConnection;
    private final ClientServerHandler serverHandler;

    public PlayerScreen() {
        clientConnection = ClientConnection.getInstance();
        serverHandler = new ClientServerHandler(this, this, this);
        clientConnection.addOnMessageListener(serverHandler);
        clientConnection.addOnConnectionListener(serverHandler);
        clientConnection.addOnDisconnectedListener(serverHandler);
    }

    @Override
    public void show() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void hide() {

    }

    public void connect(String ipAddress) {
        Logger.d("Connecting...!");
        clientConnection.connect(ipAddress);
    }

    public void disconnect() {
        clientConnection.addOnMessageListener(null);
        clientConnection.addOnConnectionListener(null);
        clientConnection.addOnDisconnectedListener(null);
        clientConnection.disconnect();
    }

    public boolean isConnected() {
        return clientConnection.isConnected();
    }

    public void send(Envelope envelope) {
        clientConnection.send(envelope);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void resize(int width, int height) {}
}
