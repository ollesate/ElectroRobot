package olof.sjoholm.GameWorld.Server;

import java.util.List;

import olof.sjoholm.common.CardModel;

/**
 * Created by sjoholm on 26/12/16.
 */
public interface OnCardsReceivedListener {

    void onCardsReceived(List<CardModel> cards);
}
