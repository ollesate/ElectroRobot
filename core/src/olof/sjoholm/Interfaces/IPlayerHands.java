package olof.sjoholm.Interfaces;

import java.util.List;

import olof.sjoholm.Models.CardModel;

public interface IPlayerHands {

    void setCards(int playerId, List<CardModel> cards);
}