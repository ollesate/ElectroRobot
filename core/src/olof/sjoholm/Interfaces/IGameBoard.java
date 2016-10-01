package olof.sjoholm.Interfaces;

import olof.sjoholm.GameWorld.Utils.Direction;

/**
 * Created by sjoholm on 26/09/16.
 */

public interface IGameBoard {

    MovableToken spawnToken(int site);

    ICardHand spawnCardHand(int site);

    int getMapTile(int x, int y);

    int getPossibleSteps(Direction direction, int x, int y);

}
