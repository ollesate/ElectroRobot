package olof.sjoholm.Interfaces;

import java.util.List;

import olof.sjoholm.Models.CardModel;

public interface IPlayerApi {

    void sendCards(List<CardModel> cards);

    void getCards(final OnCardsReceivedListener onCardsReceivedListener);
}
