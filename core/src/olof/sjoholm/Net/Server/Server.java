package olof.sjoholm.Net.Server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;

import java.io.IOException;

import olof.sjoholm.GameWorld.Utils.Logger;
import olof.sjoholm.Net.Envelope;
import olof.sjoholm.Net.ServerConstants;

/**
 * Created by sjoholm on 02/10/16.
 */

public class Server implements IncomingConnectionListener.ConnectionListener {
    private IncomingConnectionListener connectionListener;
    private Client client;

    public Server() {
        ServerSocket socket = Gdx.net.newServerSocket(
                Net.Protocol.TCP,
                ServerConstants.HOST_NAME,
                ServerConstants.CONNECTION_PORT,
                new ServerSocketHints()
        );
        connectionListener = new IncomingConnectionListener(this, socket);
        connectionListener.start();
    }

    public void stop() {
        connectionListener.stop();
    }

    @Override
    public void onNewConnection(Client client) {
        this.client = client;
        client.sendData(new Envelope.Message("Welcome my friend. :)"));
    }

    public void broadcast(String broadcast) {
        Logger.d("Broadcast: " + broadcast);
        client.sendData(new Envelope.Message(broadcast));
    }


}
