package olof.sjoholm.Net.Game;

import java.util.List;

import olof.sjoholm.Interfaces.OnCardsReceivedListener;
import olof.sjoholm.Utils.Logger;
import olof.sjoholm.Net.Both.Client;
import olof.sjoholm.Net.Envelope;
import olof.sjoholm.Models.CardModel;

public class PlayerApi implements olof.sjoholm.Interfaces.IPlayerApi {
    private Client client;

    public PlayerApi(Client client) {
        this.client = client;
    }

    public void sendCards(List<CardModel> cards) {
        client.sendData(new Envelope.SendCards(cards));
    }

    public void getCards(final olof.sjoholm.Interfaces.OnCardsReceivedListener onCardsReceivedListener) {
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
            List<CardModel> cards = envelope.getContents(List.class);
            onCardsReceivedListener.onCardsReceived(cards);
            Logger.d("Received action from player");
            for (CardModel card : cards) {
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
