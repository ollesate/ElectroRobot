package olof.sjoholm.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import olof.sjoholm.utils.Logger;


public class NetClient {
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private final int id;

    public interface Listener {

        void onMessage(NetClient netClient, Envelope readObject);

        void onDisconnected(NetClient netClient);

    }
    public NetClient(int id, ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        this.id = id;
        objectInputStream = inputStream;
        objectOutputStream = outputStream;
    }

    /**
     * Initiate a handshake, sending the id to the other side.
     */
    public static NetClient open(Socket socket, int id) throws ConnectionException {
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(id);
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new ConnectionException(e.getMessage());
        }
        return new NetClient(id, inputStream, outputStream);
    }

    /**
     * Respond to a handshake, receiving an id.
     */
    public static NetClient accept(String host, int port) throws ConnectionException {
        ObjectInputStream inputStream;
        ObjectOutputStream outputStream;
        int id;
        try {
            Socket socket = Gdx.net.newClientSocket(
                    Net.Protocol.TCP, host, port, new SocketHints());
            inputStream = new ObjectInputStream(socket.getInputStream());
            id = ((Integer) inputStream.readObject());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new ConnectionException(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new ConnectionException(e.getMessage());
        } catch (GdxRuntimeException e) {
            throw new ConnectionException(e.getMessage());
        }
        return new NetClient(id, inputStream, outputStream);
    }

    public void sendData(Envelope envelope) {
        try {
            objectOutputStream.writeObject(envelope);
        } catch (IOException e) {
            Logger.e(e.getMessage());
        }
    }

    public void startReading(Listener listener) {
        new ReaderThread(listener).start();
    }

    private class ReaderThread extends Thread {

        private Listener listener;
        public ReaderThread(Listener listener) {
            this.listener = listener;
        }

        @Override
        public void run() {
            try {
                Logger.d("Start reading...");
                boolean isOpen = true;
                while (isOpen) {
                    try {
                        Object readObject = objectInputStream.readObject();
                        listener.onMessage(NetClient.this, ((Envelope) readObject));
                    } catch (EOFException e) {
                        isOpen = false;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                objectInputStream.close();
                listener.onDisconnected(NetClient.this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public void disconnect() {
        //TODO: implement, use a stoppable thread for reader thread
    }

    public int getId() {
        return id;
    }

}
