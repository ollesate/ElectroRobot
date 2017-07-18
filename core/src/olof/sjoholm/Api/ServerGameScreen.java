package olof.sjoholm.Api;

import olof.sjoholm.GameLogic.GameManager;
import olof.sjoholm.Net.Both.Envelope;
import olof.sjoholm.Net.Server.Server;
import olof.sjoholm.Views.GameScreen;
import olof.sjoholm.Views.LobbyStage;

public class ServerGameScreen extends ServerScreen {

    public ServerGameScreen(Server server, ScreenHandler screenHandler) {
        gameScreen = new GameScreen(new LobbyStage.OnStartGameListener() {
            @Override
            public void onStartClicked() {
                startGame();
            }
        });
        game.setScreen(gameScreen);

        gameManager = new GameManager(gameScreen.getGameStage());

        super(server);
    }

    @Override
    public void onMessage(Envelope envelope) {

    }

    @Override
    public void onPlayerConnected(Player player) {

    }

    @Override
    public void onPlayerDisconnected(Player player) {

    }

    @Override
    public void render(float delta) {

    }
}
