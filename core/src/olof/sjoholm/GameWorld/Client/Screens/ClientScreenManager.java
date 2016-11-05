package olof.sjoholm.GameWorld.Client.Screens;

import com.badlogic.gdx.Game;

import olof.sjoholm.GameWorld.Utils.Logger;

/**
 * Created by sjoholm on 02/10/16.
 */

public class ClientScreenManager implements olof.sjoholm.GameWorld.Client.IScreenManager {
    private Game game;

    public ClientScreenManager(Game game) {
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
