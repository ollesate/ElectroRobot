package olof.sjoholm.Client;

import java.util.List;

import olof.sjoholm.GameWorld.Net.OnMessageReceivedListener;
import olof.sjoholm.GameWorld.Utils.Logger;
import olof.sjoholm.Net.Envelope;
import olof.sjoholm.common.CardModel;

/**
 * Created by sjoholm on 23/12/16.
 */
public class MessageHandler implements OnMessageReceivedListener {
    private ServerConnection serverConnection;
    private final MessageDispatcher dispatcher;

    public MessageHandler(ServerConnection serverConnection, MessageDispatcher dispatcher) {
        this.serverConnection = serverConnection;
        this.dispatcher = dispatcher;
        serverConnection.addOnMessageReceivedListener(this);
    }

    @Override
    public void onMessage(final Envelope envelope, Long clientId) {
        if (envelope instanceof Envelope.SendCards) {
            Logger.d("Server sent me cards");
            List<CardModel> list = envelope.getContents(List.class);
            dispatcher.dealCards(list);
        } else if (envelope instanceof Envelope.RequestCards) {
            Logger.d("Server requests my cards");
            List<CardModel> cards = dispatcher.getCards();
            Envelope.SendCards sendCards = new Envelope.SendCards(cards);
            sendCards.tagWithResponseId(envelope.getResponseId());
            serverConnection.send(sendCards);
        } else if (envelope instanceof Envelope.StartGame) {
            Logger.d("Server started the game");
            dispatcher.startGame();
        }
    }

}
