package olof.sjoholm.GameLogic;

import olof.sjoholm.GameWorld.Actors.MovableToken;

/**
 * Created by sjoholm on 26/09/16.
 */

public interface IGameBoard {

    MovableToken spawnToken(int site);

    ICardHand spawnCardHand(int site);

}
