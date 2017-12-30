package olof.sjoholm.net;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.utils.Logger;

public class ClientConnection implements NetClient.Listener {
    private static ClientConnection instance;
    private NetClient connection;
    private List<OnConnectionListener> onConnectionListeners = new ArrayList<OnConnectionListener>();
    private List<OnDisconnectedListener> onDisconnectedListeners = new ArrayList<OnDisconnectedListener>();
    private List<OnMessageListener> onMessageListeners = new ArrayList<OnMessageListener>();
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
        for (OnMessageListener listener : onMessageListeners) {
            listener.onMessage(envelope);
        }
    }

    @Override
    public void onDisconnected(NetClient netClient) {
        for (OnDisconnectedListener listener : onDisconnectedListeners) {
            listener.onDisconnected();
        }
    }

    public void addOnConnectionListener(OnConnectionListener listener) {
        onConnectionListeners.add(listener);
    }

    public void addOnDisconnectedListener(OnDisconnectedListener listener) {
        onDisconnectedListeners.add(listener);
    }

    public void addOnMessageListener(OnMessageListener listener) {
        onMessageListeners.add(listener);
    }

    public void connect(String ipAddress) {
        if (isConnected) {
            throw new IllegalStateException("Already connected");
        }
        try {
            connection = NetClient.accept(ipAddress, ServerConnection.PORT);
            connection.startReading(this);
            for (OnConnectionListener listener : onConnectionListeners) {
                listener.onConnected();
            }
            isConnected = true;
        } catch (ConnectionException e) {
            // TODO: Make this async instead.
            Logger.d(e.getMessage());
            isConnected = false;
            for (OnConnectionListener listener : onConnectionListeners) {
                listener.onConnectionFailed(e.getMessage());
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
