package olof.sjoholm.Api;

import com.badlogic.gdx.Game;

import olof.sjoholm.Net.Server.Server;

public class ScreenHandler {
    private final Game game;
    private final ServerLobbyScreen serverLobbyScreen;
    private final ServerGameScreen serverGameScreen;

    public ScreenHandler(Game game, Server server) {
        this.game = game;
        serverLobbyScreen = new ServerLobbyScreen(server, this);
        serverGameScreen = new ServerGameScreen(server, this);
    }

    public void showLobbyScreen() {
        game.setScreen(serverLobbyScreen);
    }

    public void showGameScreen() {
        game.setScreen(serverGameScreen);
    }
}
