package olof.sjoholm.Interfaces;

import com.badlogic.gdx.math.Vector2;

import olof.sjoholm.GameLogic.PlayerController;


public interface IGameBoard {

    void spawnToken(PlayerController owner);

    MovableToken getToken(PlayerController owner);

    int isTileAvailable(int x, int y);

    int getPossibleSteps(Vector2 direction, int x, int y);

}
