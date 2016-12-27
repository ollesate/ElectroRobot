package olof.sjoholm.Interfaces;

import olof.sjoholm.GameWorld.Game.PlayerController;
import olof.sjoholm.GameWorld.Server.Player;
import olof.sjoholm.GameWorld.Utils.Direction;


public interface IGameBoard {

    void spawnToken(PlayerController owner);

    MovableToken getToken(PlayerController owner);

    int isTileAvailable(int x, int y);

    int getPossibleSteps(Direction direction, int x, int y);

}
