package olof.sjoholm.GameWorld.Client;

import com.badlogic.gdx.Gdx;

import olof.sjoholm.GameWorld.Net.ClientConnection;
import olof.sjoholm.GameWorld.Utils.Logger;
import olof.sjoholm.Net.Both.Protocol;
import olof.sjoholm.Net.Envelope;

/**
 * Created by sjoholm on 09/10/16.
 */

public class ClientGameHandler {
    private IScreenManager screenManager;

    public ClientGameHandler(IScreenManager screenManager) {
        this.screenManager = screenManager;

        connectToServer();
    }

    private void connectToServer() {
        screenManager.showGameLobby();

        // Setup connection
        ClientConnection.startClient();
        ClientConnection.addServerMessageListener(new ClientConnection.OnServerMessageListener() {
            @Override
            public void onServerMessage(Envelope envelope) {
                onMessageReceived(envelope);
            }
        });
    }

    private void onMessageReceived(Envelope envelope) {
        Logger.d("onMessage received " + envelope.toString());

        if (envelope instanceof Envelope.Message) {
            messageProtocol(envelope.getContents(String.class));
        }
    }

    private void messageProtocol(String message) {
        if (Protocol.START_GAME.equals(message)) {
            startGame();
        }
    }

    private void startGame() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                screenManager.showGameScreen();
            }
        });
    }

    public interface IClientGame {



    }

}
