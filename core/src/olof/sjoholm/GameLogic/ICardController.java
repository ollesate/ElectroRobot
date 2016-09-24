package olof.sjoholm.GameLogic;

import olof.sjoholm.GameWorld.Actors.ICardView;

/**
 * Created by sjoholm on 26/09/16.
 */

public interface ICardController {

    void useCard(int index);

    int getCardValue(int index);

}
