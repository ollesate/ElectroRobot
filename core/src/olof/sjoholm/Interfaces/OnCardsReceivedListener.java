package olof.sjoholm.Interfaces;

import java.util.List;

import olof.sjoholm.Models.CardModel;

/**
 * Created by sjoholm on 26/12/16.
 */
public interface OnCardsReceivedListener {

    void onCardsReceived(List<CardModel> cards);
}
