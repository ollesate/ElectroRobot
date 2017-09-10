package olof.sjoholm.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import olof.sjoholm.utils.Logger;

public final class ServerConnection implements NetClient.Listener {
    public static final int PORT = 9123;
    public static final String HOST_NAME = "127.0.0.1";

    private static ServerConnection instance;

    private final ServerSocket serverSocket;
    private final List<NetClient> netClients = new ArrayList<NetClient>();
    private final Map<Integer, Player> players = new HashMap<Integer, Player>();

    private int clientCounter;
    private OnPlayerConnectedListener onPlayerConnectedListener;
    private OnPlayerDisconnectedListener onPlayerDisconnectedListener;
    private OnMessageListener onMessageListener;
    private LoopingThread loopingThread;
    private final String hostName;

    public interface OnMessageListener {

        void onMessage(Player player, Envelope envelope);
    }

    public interface OnPlayerConnectedListener {

        void onPlayerConnected(Player player);
    }

    public interface OnPlayerDisconnectedListener {

        void onPlayerDisconnected(Player player);
    }

    private ServerConnection() {
        String host;
        try {
            host = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            Logger.e(e.getMessage());
            host = "unavailable";
        }
        hostName = host;
        serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, HOST_NAME, PORT, null);
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

            int id = client.getId();
            Player player = new Player(id);
            players.put(id, player);
            netClients.add(client);

            if (onPlayerConnectedListener != null) {
                onPlayerConnectedListener.onPlayerConnected(player);
            }
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(NetClient netClient, Envelope envelope) {
        if (onMessageListener != null) {
            onMessageListener.onMessage(players.get(netClient.getId()), envelope);
        }
    }

    @Override
    public void onDisconnected(NetClient netClient) {
        Player player = players.get(netClient.getId());

        if (onPlayerDisconnectedListener != null) {
            onPlayerDisconnectedListener.onPlayerDisconnected(player);
        }

        players.remove(netClient.getId());
        netClients.remove(netClient);
    }

    // API

    public void openConnection() {
        Logger.d("openConnection");
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

    public List<Player> getConnectedPlayers() {
        return new ArrayList<Player>(players.values());
    }

    public boolean isConnectionOpen() {
        return loopingThread != null && loopingThread.isRunning();
    }

    public synchronized void broadcast(Envelope envelope) {
        for (NetClient netClient : netClients) {
            netClient.sendData(envelope);
        }
    }

    public synchronized void send(Player player, Envelope envelope) {
        for (NetClient netClient : netClients) {
            if (netClient.getId() == player.getId()) {
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
