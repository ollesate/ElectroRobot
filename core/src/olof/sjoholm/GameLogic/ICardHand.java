package olof.sjoholm.GameLogic;

import olof.sjoholm.GameWorld.Actors.MovableToken;

/**
 * Created by sjoholm on 27/09/16.
 */
public interface ICardHand {

    void createRandomCard(MovableToken movableToken);

    int size();

    ICard popTopCard();

}
