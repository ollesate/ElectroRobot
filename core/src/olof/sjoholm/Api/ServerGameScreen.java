package olof.sjoholm.Api;

import com.badlogic.gdx.Gdx;

import olof.sjoholm.Net.Both.Envelope;
import olof.sjoholm.Net.Server.Player;
import olof.sjoholm.Views.GameStage;

public class ServerGameScreen extends ServerScreen {
    private final GameStage gameStage;

    public ServerGameScreen(ServerScreenHandler serverScreenHandler) {
        gameStage = new GameStage();
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(gameStage);
    }

    @Override
    public void render(float delta) {
        gameStage.act(delta);
        gameStage.draw();
    }

    @Override
    public void onMessage(Player player, Envelope envelope) {

    }

    @Override
    public void onPlayerConnected(Player player) {

    }

    @Override
    public void onPlayerDisconnected(Player player) {

    }
}
