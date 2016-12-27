package olof.sjoholm.Interfaces;

import com.badlogic.gdx.math.Vector2;

import olof.sjoholm.GameWorld.Game.PlayerController;
import olof.sjoholm.GameWorld.Server.Player;
import olof.sjoholm.GameWorld.Utils.Direction;


public interface IGameBoard {

    void spawnToken(PlayerController owner);

    MovableToken getToken(PlayerController owner);

    int isTileAvailable(int x, int y);

    int getPossibleSteps(Vector2 direction, int x, int y);

}
