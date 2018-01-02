package olof.sjoholm.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import olof.sjoholm.utils.Logger;

public final class ServerConnection implements NetClient.Listener {
    public static final int PORT = 9123;

    private static ServerConnection instance;

    private ServerSocket serverSocket;
    private final List<NetClient> netClients = new ArrayList<NetClient>();
    private final Set<Integer> players = new HashSet<Integer>();

    private int clientCounter;
    private OnPlayerConnectedListener onPlayerConnectedListener;
    private OnPlayerDisconnectedListener onPlayerDisconnectedListener;
    private OnMessageListener onMessageListener;
    private LoopingThread loopingThread;
    private final String hostName;

    public interface OnMessageListener {

        void onMessage(int playerId, Envelope envelope);
    }

    public interface OnPlayerConnectedListener {

        void onPlayerConnected(int playerId);
    }

    public interface OnPlayerDisconnectedListener {

        void onPlayerDisconnected(int playerId);
    }

    public ServerConnection() {
        String host;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            Logger.e(e.getMessage());
            host = "unavailable";
        }
        hostName = host;
    }

    public static ServerConnection getInstance() {
        if (instance == null) {
            instance = new ServerConnection();
        }
        return instance;
    }

    private synchronized void onNewConnection(Socket socket) {
        try {
            clientCounter++;
            NetClient client = NetClient.open(socket, clientCounter);
            client.startReading(this);

            netClients.add(client);

            if (onPlayerConnectedListener != null) {
                onPlayerConnectedListener.onPlayerConnected(client.getId());
            }
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(NetClient netClient, Envelope envelope) {
        if (onMessageListener != null) {
            onMessageListener.onMessage(netClient.getId(), envelope);
        }
    }

    @Override
    public void onDisconnected(NetClient netClient) {
        if (onPlayerDisconnectedListener != null) {
            onPlayerDisconnectedListener.onPlayerDisconnected(netClient.getId());
        }

        players.remove(netClient.getId());
        netClients.remove(netClient);
    }

    // API

    public void connect() {
        Logger.d("connect");

        if (serverSocket == null) {
            serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, hostName, PORT, null);
        }

        if (loopingThread != null && loopingThread.isRunning()) {
            throw new IllegalStateException("Connection already opened");
        }
        loopingThread = new LoopingThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socketConnection = serverSocket.accept(null);
                    onNewConnection(socketConnection);
                    Logger.d("New connection!");
                } catch (GdxRuntimeException ignored) {
                    // Seems to be only be caused by socket time outs here
                }
            }
        });
        loopingThread.start();
    }

    public synchronized void disconnect() {
        for (NetClient netClient : netClients) {
            netClient.disconnect();
        }
        netClients.clear();
        loopingThread.stop();
    }

    public Collection<Integer> getConnectedPlayers() {
        return players;
    }

    public boolean isConnectionOpen() {
        return loopingThread != null && loopingThread.isRunning();
    }

    public synchronized void broadcast(Envelope envelope) {
        for (NetClient netClient : netClients) {
            netClient.sendData(envelope);
        }
    }

    public synchronized void send(int id, Envelope envelope) {
        for (NetClient netClient : netClients) {
            if (netClient.getId() == id) {
                netClient.sendData(envelope);
            }
        }
    }

    public void setOnMessageListener(OnMessageListener listener) {
        onMessageListener = listener;
    }

    public void setOnPlayerConnectedListener(OnPlayerConnectedListener listener) {
        onPlayerConnectedListener = listener;
    }

    public void setOnPlayerDisconnectedListener(OnPlayerDisconnectedListener listener) {
        onPlayerDisconnectedListener = listener;
    }

    public String getHostName() {
        return hostName;
    }
}
