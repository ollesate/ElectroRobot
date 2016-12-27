package olof.sjoholm.GameWorld.Server;

import java.util.List;

import olof.sjoholm.Interfaces.Action;

/**
 * Created by sjoholm on 26/12/16.
 */
public interface OnCardsReceivedListener {

    void onCardsReceived(List<Action> cards);
}
