package olof.sjoholm.GameWorld.Client;

import com.badlogic.gdx.Game;

import olof.sjoholm.GameWorld.Client.Game.GameScreen;
import olof.sjoholm.GameWorld.Client.Lobby.LobbyScreen;
import olof.sjoholm.GameWorld.Utils.Logger;

/**
 * Created by sjoholm on 02/10/16.
 */

public class ScreenManager implements IScreenManager {
    private Game game;

    public ScreenManager(Game game) {
        this.game = game;
    }

    @Override
    public void showGameLobby() {
        Logger.d("showGameLobby");
        game.setScreen(new LobbyScreen());
    }

    @Override
    public void showGameScreen() {
        Logger.d("showGameScreen");
        game.setScreen(new GameScreen());
    }
}
