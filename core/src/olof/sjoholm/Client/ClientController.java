package olof.sjoholm.Client;

import com.badlogic.gdx.Game;

import java.io.IOException;
import java.util.List;

import olof.sjoholm.Api.Request;
import olof.sjoholm.Models.CardModel;
import olof.sjoholm.Net.Both.NetClient;
import olof.sjoholm.Net.Both.Envelope;
import olof.sjoholm.Net.ServerConstants;
import olof.sjoholm.Utils.Logger;


public class ClientController implements NetClient.OnMessageListener {
    private ServerConnection serverConnection;
    private final PlayerGame playerGame;

    public ClientController(Game game) {
        try {
            serverConnection = new ServerConnection(
                    ServerConstants.HOST_NAME, ServerConstants.CONNECTION_PORT, this
            );
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        playerGame = new PlayerGame();
        game.setScreen(playerGame);
    }

    @Override
    public void onMessage(Envelope envelope) {
        if (envelope instanceof Envelope.SendCards) {
            Logger.d("Server sent me cards");
            List<CardModel> list = envelope.getContents(List.class);
            playerGame.dealCards(list);
        } else if (envelope instanceof Envelope.StartGame) {
            Logger.d("Server started the game");
            playerGame.startGame();
        }
    }

    @Override
    public Envelope onRequest(Request request) {
        if (request.envelope instanceof Envelope.RequestCards) {
            List<CardModel> cards = playerGame.getCards();
            return new Envelope.SendCards(cards);
        }
        throw new IllegalStateException("No response for this request " + request.toString());
    }

    public void dispose() {
        serverConnection.dispose();
    }
}
