package olof.sjoholm.Api;

import com.badlogic.gdx.Gdx;

import olof.sjoholm.Client.stages.HandStage;
import olof.sjoholm.Net.Both.Envelope;
import olof.sjoholm.Utils.Logger;

public class PlayerGameScreen extends PlayerScreen {
    private final HandStage handStage;

    public PlayerGameScreen() {
        handStage = new HandStage();
        connect();
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(handStage);
    }

    @Override
    public void render(float delta) {
        handStage.act(delta);
        handStage.draw();
    }

    @Override
    public void onConnected(boolean status) {
        Logger.d("Connected");
        // No behaviour right now
    }

    @Override
    public void onDisconnected() {
        Logger.d("Disconnected");
        // No behaviour right now
    }

    @Override
    public void onMessage(Envelope envelope) {
        Logger.d("Player receive message " + envelope);
    }
}
