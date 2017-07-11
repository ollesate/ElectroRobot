package olof.sjoholm.Client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

import java.io.IOException;
import java.io.ObjectInputStream;

import olof.sjoholm.Net.Both.ConnectionMessageWorker;
import olof.sjoholm.Net.Both.Envelope;
import olof.sjoholm.Utils.Logger;

class ServerConnection {
    private final ConnectionMessageWorker.OnMessageListener messageListener;
    private ConnectionMessageWorker serverMessageWorker;

    public ServerConnection(String host, int port,
                            ConnectionMessageWorker.OnMessageListener messageListener)
            throws IOException, ClassNotFoundException {
        this.messageListener = messageListener;
        new ConnectThread(host, port).start();
    }

    public void send(Envelope.SendCards sendCards) {
        serverMessageWorker.sendData(sendCards);
    }

    public void dispose() {
        serverMessageWorker.dispose();
    }

    private class ConnectThread extends Thread {
        private final String host;
        private final int port;

        public ConnectThread(String host, int port) {
            this.host = host;
            this.port = port;
        }

        @Override
        public void run() {
            Logger.d("Connecting to server");
            Socket connectionToServer = Gdx.net.newClientSocket(
                    Net.Protocol.TCP,
                    host,
                    port,
                    new SocketHints()
            );
            Logger.d("Connecting connected, waiting for id");
            try {
                serverMessageWorker = ConnectionMessageWorker.accept(connectionToServer);
                serverMessageWorker.startReading();
                serverMessageWorker.setOnMessageListener(messageListener);
                Logger.d("Received id");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
