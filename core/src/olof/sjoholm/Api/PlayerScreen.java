package olof.sjoholm.Api;

import com.badlogic.gdx.Screen;

import olof.sjoholm.Net.Both.Envelope;
import olof.sjoholm.Net.Server.ClientConnection;

public abstract class PlayerScreen implements Screen, ClientConnection.OnMessageListener,
        ClientConnection.OnConnectionListener, ClientConnection.OnDisconnectedListener {
    private final ClientConnection clientConnection;

    public PlayerScreen() {
        clientConnection = ClientConnection.getInstance();
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
        clientConnection.setOnMessageListener(this);
        clientConnection.setOnConnectionListener(this);
        clientConnection.setOnDisconnectedListener(this);
        clientConnection.connect();
    }

    public void disconnect() {
        clientConnection.setOnMessageListener(null);
        clientConnection.setOnConnectionListener(null);
        clientConnection.setOnDisconnectedListener(null);
        clientConnection.disconnect();
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
