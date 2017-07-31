package olof.sjoholm.Net.Server;

import olof.sjoholm.Net.Both.ConnectionException;
import olof.sjoholm.Net.Both.Envelope;
import olof.sjoholm.Net.Both.NetClient;
import olof.sjoholm.Utils.Logger;

public class ClientConnection implements NetClient.Listener {
    private static ClientConnection instance;
    private NetClient connection;
    private OnConnectionListener onConnectionListener;
    private OnDisconnectedListener onDisconnectedListener;
    private OnMessageListener onMessageListener;
    private boolean isConnected;

    public interface OnDisconnectedListener {

        void onDisconnected();
    }

    public interface OnConnectionListener {

        void onConnected();

        void onConnectionFailed(String reason);
    }

    public interface OnMessageListener {

        void onMessage(Envelope envelope);
    }

    private ClientConnection() {}

    public static ClientConnection getInstance() {
        if (instance == null) {
            instance = new ClientConnection();
        }
        return instance;
    }

    @Override
    public void onMessage(NetClient netClient, Envelope envelope) {
        if (onMessageListener != null) {
            onMessageListener.onMessage(envelope);
        }
    }

    @Override
    public void onDisconnected(NetClient netClient) {
        if (onDisconnectedListener != null) {
            onDisconnectedListener.onDisconnected();
        }
    }

    public void setOnConnectionListener(OnConnectionListener listener) {
        onConnectionListener = listener;
    }

    public void setOnDisconnectedListener(OnDisconnectedListener listener) {
        onDisconnectedListener = listener;
    }

    public void setOnMessageListener(OnMessageListener listener) {
        onMessageListener = listener;
    }

    public void connect() {
        if (isConnected) {
            throw new IllegalStateException("Already connected");
        }
        try {
            connection = NetClient.accept(ServerConnection.HOST_NAME, ServerConnection.PORT);
            connection.startReading(this);
            if (onConnectionListener != null) {
                onConnectionListener.onConnected();
            }
            isConnected = true;
        } catch (ConnectionException e) {
            // TODO: Make this async instead.
            Logger.d(e.getMessage());
            isConnected = false;
            if (onConnectionListener != null) {
                onConnectionListener.onConnectionFailed(e.getMessage());
            }
        }
    }

    public void disconnect() {
        connection.disconnect();
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void send(Envelope envelope) {
        if (!isConnected) {
            Logger.e("Not connected trying to send " + envelope);
        }
        connection.sendData(envelope);
    }
}
