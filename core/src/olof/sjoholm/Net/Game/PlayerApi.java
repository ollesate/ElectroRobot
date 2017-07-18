package olof.sjoholm.Net.Game;

import java.util.List;

import olof.sjoholm.Api.Request;
import olof.sjoholm.Interfaces.IPlayerApi;
import olof.sjoholm.Interfaces.OnCardsReceivedListener;
import olof.sjoholm.Net.Both.NetClient;
import olof.sjoholm.Utils.Logger;
import olof.sjoholm.Net.Both.Envelope;
import olof.sjoholm.Models.CardModel;

public class PlayerApi implements IPlayerApi {
    private NetClient netClient;

    public PlayerApi(NetClient netClient) {
        this.netClient = netClient;
    }

    public void sendCards(List<CardModel> cards) {
        netClient.sendData(new Envelope.SendCards(cards));
    }

    public void getCards(final OnCardsReceivedListener onCardsReceivedListener) {
        Request request = new Request(new Envelope.RequestCards());
        netClient.sendRequest(request, new NetClient.OnResponseCallback() {
            @Override
            public void onResponse(Envelope envelope) {
                onGetCardsResponse(envelope, onCardsReceivedListener);
            }
        });
    }

    private void onGetCardsResponse(Envelope envelope, OnCardsReceivedListener onCardsReceivedListener) {
        if (envelope instanceof Envelope.SendCards) {
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
}
