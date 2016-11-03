package olof.sjoholm.Net.Both;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import olof.sjoholm.GameWorld.Utils.Logger;
import olof.sjoholm.Net.Envelope;

/**
 * Created by sjoholm on 02/10/16.
 */

public class Client {
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream objectInputStream;
    private OnMessageListener onMessageListener;
    private OnDisconnectedListener onDisconnectedListener;


    public Client(Socket socket) {
        this.socket = socket;
        initializeSockets();
    }

    public Client(String host, int port) {
        Logger.d("Client trying to connect...");
        socket = Gdx.net.newClientSocket(
                Net.Protocol.TCP,
                host,
                port,
                new SocketHints()
        );
        initializeSockets();
    }

    private void initializeSockets() {
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendData(Envelope envelope) {
        try {
            outputStream.writeObject(envelope);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startReading() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Logger.d("Start reading...");
                    boolean isOpen = true;
                    while (isOpen) {
                        try{
                            Envelope envelope = (Envelope) objectInputStream.readObject();
                            Logger.d("Read message " + envelope.toString());
                            onMessageListener.onMessage(Client.this, envelope);
                        } catch (EOFException e) {
                            isOpen = false;
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    objectInputStream.close();
                    onDisconnectedListener.onDisconnected(Client.this);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void setOnMessageListener(OnMessageListener onMessageListener) {
        this.onMessageListener = onMessageListener;
    }

    public void setOnDisconnectedListener(OnDisconnectedListener onDisconnectedListener) {
        this.onDisconnectedListener = onDisconnectedListener;
    }

    public interface OnDisconnectedListener {

        void onDisconnected(Client client);

    }

}
