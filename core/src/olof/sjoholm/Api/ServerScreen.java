package olof.sjoholm.Api;

import com.badlogic.gdx.scenes.scene2d.Stage;

import olof.sjoholm.Net.Both.Envelope;
import olof.sjoholm.Net.Server.Server;

public abstract class ServerScreen extends Stage {
    private final Server server;

    public ServerScreen(Server server) {
        this.server = server;
    }

    @Override
    public void show() {
        server.setOnClientMessageReceivedListener(this);
    }

    @Override
    public void hide() {
        server.setOnClientMessageReceivedListener(null);
        // TODO: maybe stop connection to server here?
    }

    @Override
    public void dispose() {
        server.stop();
    }

    public void broadcast(Envelope envelope) {
        server.broadcast(envelope);
    }

    @Override
    public void onMessageReceived(Envelope envelope) {
        onMessage(envelope);
    }

    public abstract void onMessage(Envelope envelope);

    public abstract void onPlayerConnected(Player player);

    public abstract void onPlayerDisconnected(Player player);

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void resize(int width, int height) {}
}
