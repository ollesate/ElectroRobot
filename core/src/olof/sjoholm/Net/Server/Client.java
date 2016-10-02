package olof.sjoholm.Net.Server;

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
        read();
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

    public void read() {
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
                        } catch (EOFException e) {
                            e.printStackTrace();
                            isOpen = false;
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    objectInputStream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
