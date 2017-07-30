package olof.sjoholm.Api;

import com.badlogic.gdx.Gdx;

import olof.sjoholm.Net.Both.Envelope;
import olof.sjoholm.Net.Server.ClientConnection;

class ClientServerHandler implements ClientConnection.OnMessageListener,
        ClientConnection.OnConnectionListener, ClientConnection.OnDisconnectedListener {

    private final ClientConnection.OnMessageListener messageListener;
    private final ClientConnection.OnConnectionListener connectionListener;
    private final ClientConnection.OnDisconnectedListener disconnectedListener;

    public ClientServerHandler(ClientConnection.OnMessageListener messageListener,
                               ClientConnection.OnConnectionListener connectionListener,
                               ClientConnection.OnDisconnectedListener disconnectedListener) {

        this.messageListener = messageListener;
        this.connectionListener = connectionListener;
        this.disconnectedListener = disconnectedListener;
    }

    @Override
    public void onDisconnected() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                disconnectedListener.onDisconnected();
            }
        });
    }

    @Override
    public void onConnected() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                connectionListener.onConnected();
            }
        });
    }

    @Override
    public void onConnectionFailed(final String reason) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                connectionListener.onConnectionFailed(reason);
            }
        });
    }

    @Override
    public void onMessage(final Envelope envelope) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                messageListener.onMessage(envelope);
            }
        });
    }
}