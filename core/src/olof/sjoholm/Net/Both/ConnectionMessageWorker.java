package olof.sjoholm.Net.Both;

import com.badlogic.gdx.net.Socket;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import olof.sjoholm.Api.Request;
import olof.sjoholm.Api.Response;
import olof.sjoholm.Utils.Logger;


public class ConnectionMessageWorker {
    private final Map<Long, OnResponseCallback> waitingResponses = new HashMap<Long, OnResponseCallback>();
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private OnMessageListener onMessageListener;
    private OnDisconnectedListener onDisconnectedListener;
    private final int id;

    public interface OnDisconnectedListener {

        void onDisconnected(ConnectionMessageWorker connectionMessageWorker);
    }

    public interface OnResponseCallback {

        void onResponse(Envelope envelope);
    }

    public interface OnMessageListener {

        void onMessage(Envelope envelope);

        Envelope onRequest(Request request);
    }

    public ConnectionMessageWorker(int id, ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        this.id = id;
        objectInputStream = inputStream;
        objectOutputStream = outputStream;
    }

    /**
     * Initiate a handshake, sending the id to the other side.
     */
    public static ConnectionMessageWorker connect(Socket socket, int id) throws IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeObject(id);
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        return new ConnectionMessageWorker(id, inputStream, outputStream);
    }

    /**
     * Respond to a handshake, receiving an id.
     */
    public static ConnectionMessageWorker accept(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        int id = ((Integer) inputStream.readObject());
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        return new ConnectionMessageWorker(id, inputStream, outputStream);
    }

    public void sendData(Envelope envelope) {
        try {
            objectOutputStream.writeObject(envelope);
        } catch (IOException e) {
            Logger.e(e.getMessage());
        }
    }

    public void sendRequest(Request request, OnResponseCallback onResponseCallback) {
        synchronized (waitingResponses) {
            try {
                objectOutputStream.writeObject(request);
                waitingResponses.put(request.id, onResponseCallback);
            } catch (IOException e) {
                Logger.e(e.getMessage());
            }
        }
    }

    public void startReading() {
        new ReaderThread().start();
    }

    private class ReaderThread extends Thread {
        @Override
        public void run() {
            try {
                Logger.d("Start reading...");
                boolean isOpen = true;
                while (isOpen) {
                    try {
                        Object readObject = objectInputStream.readObject();
                        if (readObject instanceof Response) {
                            onResponse(((Response) readObject));
                        } else if (readObject instanceof Request) {
                            onRequest(((Request) readObject));
                        } else if (readObject instanceof Envelope) {
                            onMessage(((Envelope) readObject));
                        }
                    } catch (EOFException e) {
                        isOpen = false;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                objectInputStream.close();
                onDisconnectedListener.onDisconnected(ConnectionMessageWorker.this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onResponse(Response response) {
        OnResponseCallback callback = waitingResponses.get(response.id);
        if (callback != null) {
            Logger.d("Handled response ");
            callback.onResponse(response.envelope);
        } else {
            Logger.d("Warning: a response was not handled");
        }
    }

    private void onMessage(Envelope envelope) {
        Logger.d("Read message " + envelope.toString());
        envelope.setOwnerId(id);
        onMessageListener.onMessage(envelope);
    }

    private void onRequest(Request request) {
        Envelope envelope = onMessageListener.onRequest(request);
        Response response = new Response(envelope, request);
        try {
            objectOutputStream.writeObject(response);
        } catch (IOException e) {
            Logger.e(e.getMessage());
        }
    }

    public void setOnMessageListener(OnMessageListener onMessageListener) {
        this.onMessageListener = onMessageListener;
    }

    public void setOnDisconnectedListener(OnDisconnectedListener onDisconnectedListener) {
        this.onDisconnectedListener = onDisconnectedListener;
    }

    public int getId() {
        return id;
    }

    public void dispose() {
        // TODO: implement
    }
}
