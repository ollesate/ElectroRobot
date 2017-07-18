package olof.sjoholm.Net.Server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

import olof.sjoholm.Net.Both.ConnectionException;
import olof.sjoholm.Net.Both.NetClient;

public class ClientConnection {
    private static ClientConnection instance;

    public interface OnConnectionListener {

        void onConnected(boolean status);
    }

    private ClientConnection() {

    }

    public static ClientConnection getInstance() {
        if (instance == null) {
            instance = new ClientConnection();
        }
        return instance;
    }

    public void connect() {
        try {
            NetClient.accept(ServerConnection.HOST_NAME, ServerConnection.PORT);
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
    }
}
