package olof.sjoholm.Api;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import olof.sjoholm.Net.Both.Envelope;
import olof.sjoholm.Net.Server.ClientConnection;
import olof.sjoholm.Utils.Logger;

public abstract class PlayerScreen implements Screen, ClientConnection.OnMessageListener,
        ClientConnection.OnConnectionListener, ClientConnection.OnDisconnectedListener {
    private final ClientConnection clientConnection;
    private final ClientServerHandler serverHandler;

    public PlayerScreen() {
        clientConnection = ClientConnection.getInstance();
        serverHandler = new ClientServerHandler(this, this, this);
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

    public void connect() {
        Logger.d("Connecting...!");
        clientConnection.setOnMessageListener(serverHandler);
        clientConnection.setOnConnectionListener(serverHandler);
        clientConnection.setOnDisconnectedListener(serverHandler);
        clientConnection.connect();
    }

    public void disconnect() {
        clientConnection.setOnMessageListener(null);
        clientConnection.setOnConnectionListener(null);
        clientConnection.setOnDisconnectedListener(null);
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
