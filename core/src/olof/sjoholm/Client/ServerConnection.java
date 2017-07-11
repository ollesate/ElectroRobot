package olof.sjoholm.Client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

import java.io.IOException;
import java.io.ObjectInputStream;

import olof.sjoholm.Net.Both.ConnectionMessageWorker;
import olof.sjoholm.Net.Both.Envelope;

class ServerConnection {
    private ConnectionMessageWorker serverMessageWorker;

    public ServerConnection(String host, int port, ConnectionMessageWorker.OnMessageListener messageListener)
            throws IOException, ClassNotFoundException {
        Socket connectionToServer = Gdx.net.newClientSocket(
                Net.Protocol.TCP,
                host,
                port,
                new SocketHints()
        );
        Object welcomeObject = new ObjectInputStream(connectionToServer.getInputStream()).readObject();
        Envelope.Welcome welcome = (Envelope.Welcome) welcomeObject;

        serverMessageWorker = new ConnectionMessageWorker(connectionToServer, welcome.id);
        serverMessageWorker.startReading();
        serverMessageWorker.setOnMessageListener(messageListener);
    }

    public void send(Envelope.SendCards sendCards) {
        serverMessageWorker.sendData(sendCards);
    }

    public void dispose() {
        serverMessageWorker.dispose();
    }
}
