package olof.sjoholm.GameWorld.Server;

import java.util.List;

import olof.sjoholm.GameWorld.Utils.Logger;
import olof.sjoholm.Interfaces.Action;
import olof.sjoholm.Net.Both.Client;
import olof.sjoholm.Net.Envelope;

public class PlayerApi {
    private Client client;

    public PlayerApi(Client client) {
        this.client = client;
    }

    public void sendCards(List<olof.sjoholm.common.CardModel> cards) {
        client.sendData(new Envelope.SendCards(cards));
    }

    public void getCards(final OnCardsReceivedListener onCardsReceivedListener) {
        client.sendData(new Envelope.RequestCards(), new Client.OnResponseCallback() {
            @Override
            public void onResponse(Envelope envelope) {
                onGetCardsResponse(envelope, onCardsReceivedListener);
            }
        });
    }

    private void onGetCardsResponse(Envelope envelope,
                                    OnCardsReceivedListener onCardsReceivedListener) {
        if (isProperResponse(envelope)) {
            List<Action> cards = envelope.getContents(List.class);
            onCardsReceivedListener.onCardsReceived(cards);
            Logger.d("Received action from player");
            for (Action card : cards) {
                Logger.d("Card->" + card.toString());
            }
        } else {
            throw new IllegalStateException(
                    "getCards() did not receive response envelope of type " +
                            Envelope.SendCards.class.getSimpleName()
            );
        }
    }

    private boolean isProperResponse(Envelope envelope) {
        return envelope instanceof Envelope.SendCards;
    }
}
