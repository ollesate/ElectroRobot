package olof.sjoholm.Net.Both;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import olof.sjoholm.GameWorld.Utils.Logger;
import olof.sjoholm.Net.Envelope;

import static javax.swing.UIManager.get;

/**
 * Created by sjoholm on 02/10/16.
 */

public class Client {
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream objectInputStream;
    private OnMessageListener onMessageListener;
    private OnDisconnectedListener onDisconnectedListener;
    private Long id;
    private final Map<Long, OnResponseCallback> waitingResponses;

    {
        waitingResponses = new HashMap<Long, OnResponseCallback>();
    }

    /** Used by server **/
    public Client(Socket socket) {
        this.socket = socket;
        initializeSockets();
    }

    /** Used by client **/
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

    /**
     * Send data and use the callback on the response
     * @param envelope
     * @param onResponseCallback
     */
    public void sendData(Envelope envelope, OnResponseCallback onResponseCallback) {
        try {
            envelope.tagWithResponseId();
            waitingResponses.put(envelope.getResponseId(), onResponseCallback);
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
                            if (envelope.getResponseId() == -1) {
                                // Is not a response
                                onMessage(envelope);
                            } else {
                                OnResponseCallback callback =
                                        waitingResponses.get(envelope.getResponseId());
                                if (callback != null) {
                                    callback.onResponse(envelope);
                                } else {
                                    Logger.d("Warning: a response was not handled");
                                }
                            }
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

    private void onMessage(Envelope envelope) {
        Logger.d("Read message " + envelope.toString());
        if (envelope instanceof Envelope.Welcome) {
            // We received our ID
            setId(envelope.getContents(Long.class));
        } else {
            onMessageListener.onMessage(Client.this, envelope);
        }
    }

    public void setOnMessageListener(OnMessageListener onMessageListener) {
        this.onMessageListener = onMessageListener;
    }

    public void setOnDisconnectedListener(OnDisconnectedListener onDisconnectedListener) {
        this.onDisconnectedListener = onDisconnectedListener;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public interface OnDisconnectedListener {

        void onDisconnected(Client client);

    }

    public interface OnResponseCallback {

        void onResponse(Envelope envelope);
    }
}
