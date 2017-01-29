package olof.sjoholm.GameWorld;

import olof.sjoholm.Interfaces.IGameBoard;
import olof.sjoholm.Interfaces.IPlayerHands;

public interface IGameStage {

    IGameBoard getGameBoard();

    IPlayerHands getPlayerHands();

    void startCountdown(float timeSec);
}
