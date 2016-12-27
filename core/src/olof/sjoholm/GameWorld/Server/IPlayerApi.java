package olof.sjoholm.GameWorld.Server;

import java.util.List;

import olof.sjoholm.common.CardModel;

public interface IPlayerApi {

    void sendCards(List<CardModel> cards);

    void getCards(final OnCardsReceivedListener onCardsReceivedListener);
}
