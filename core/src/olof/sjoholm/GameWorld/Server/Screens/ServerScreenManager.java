package olof.sjoholm.GameWorld.Server.Screens;

import com.badlogic.gdx.Game;

import olof.sjoholm.GameWorld.Client.Screens.IScreenManager;

public class ServerScreenManager implements IScreenManager {
    private final LobbyScreen lobbyScreen;
    private final GameScreen gameScreen;
    private Game game;

    public ServerScreenManager(Game game) {
        this.game = game;
        lobbyScreen = new LobbyScreen(new LobbyScreen.OnStartGameListener() {
            @Override
            public void onStartGame() {
                showGameScreen();
            }
        });
        gameScreen = new GameScreen();
    }

    @Override
    public void showGameLobby() {
        game.setScreen(lobbyScreen);
    }

    @Override
    public void showGameScreen() {
        game.setScreen(gameScreen);
    }
}
