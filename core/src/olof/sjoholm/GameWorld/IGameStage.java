package olof.sjoholm.GameWorld;

import olof.sjoholm.Interfaces.IGameBoard;

public interface IGameStage {

    IGameBoard getGameBoard();

    void startCountdown(float timeSec);
}
