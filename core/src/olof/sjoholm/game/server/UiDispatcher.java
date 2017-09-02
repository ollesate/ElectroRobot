package olof.sjoholm.game.server;

import com.badlogic.gdx.Gdx;

import olof.sjoholm.net.Envelope;
import olof.sjoholm.net.Player;
import olof.sjoholm.net.ServerConnection;

public class UiDispatcher implements ServerConnection.OnMessageListener,
        ServerConnection.OnPlayerConnectedListener, ServerConnection.OnPlayerDisconnectedListener {

    private final ServerConnection.OnMessageListener messageListener;
    private final ServerConnection.OnPlayerConnectedListener connectedListener;
    private final ServerConnection.OnPlayerDisconnectedListener disconnectedListener;

    public UiDispatcher(ServerConnection.OnMessageListener messageListener,
                        ServerConnection.OnPlayerConnectedListener connectedListener,
                        ServerConnection.OnPlayerDisconnectedListener disconnectedListener) {

        this.messageListener = messageListener;
        this.connectedListener = connectedListener;
        this.disconnectedListener = disconnectedListener;
    }

    @Override
    public void onMessage(final Player player, final Envelope envelope) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                messageListener.onMessage(player, envelope);
            }
        });
    }

    @Override
    public void onPlayerConnected(final Player player) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                connectedListener.onPlayerConnected(player);
            }
        });
    }

    @Override
    public void onPlayerDisconnected(final Player player) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                disconnectedListener.onPlayerDisconnected(player);
            }
        });
    }
}
