package olof.sjoholm.game.server;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import java.util.List;

import olof.sjoholm.assets.Fonts;
import olof.sjoholm.assets.Skins;
import olof.sjoholm.assets.Textures;
import olof.sjoholm.game.server.server_logic.ServerLogic;
import olof.sjoholm.game.shared.logic.cards.BoardAction;
import olof.sjoholm.net.Envelope;
import olof.sjoholm.net.ServerConnection;
import olof.sjoholm.utils.Logger;

public class ServerGame extends Game implements ServerConnection.OnMessageListener,
        ServerConnection.OnPlayerConnectedListener, ServerConnection.OnPlayerDisconnectedListener {

    private ServerConnection serverConnection;
    private ServerLogic serverLogic;

    public ServerGame() {
        serverConnection = new ServerConnection();
        serverConnection.setOnMessageListener(this);
        serverConnection.setOnPlayerConnectedListener(this);
        serverConnection.setOnPlayerDisconnectedListener(this);
    }

    @Override
    public void create () {
        Logger.d("Game create");
        Textures.initialize();
        Skins.initialize();
        Fonts.initialize();
        ServerGameScreen serverGameScreen = new ServerGameScreen();
        setScreen(serverGameScreen);
        serverConnection.connect();
        serverLogic = new ServerLogic(serverGameScreen, serverConnection);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    @Override
    public void dispose () {
        super.dispose();
        Textures.dispose();
        Skins.dispose();
        serverConnection.disconnect();
    }

    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
        Logger.d("Set screen " + screen.getClass().getSimpleName());
    }

    @Override
    public void onMessage(final int playerId, final Envelope envelope) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                onMessageUi(playerId, envelope);
            }
        });
    }

    private void onMessageUi(int id, Envelope envelope) {
        if (envelope instanceof Envelope.PlayerSelectColor) {
            Color color = ((Envelope.PlayerSelectColor) envelope).getColor();
            serverLogic.onPlayerChangeColor(id, color);
        } else if (envelope instanceof Envelope.PlayerSelectName) {
            String name = ((Envelope.PlayerSelectName) envelope).name;
            serverLogic.onPlayerChangeName(id, name);
        } else if (envelope instanceof Envelope.PlayerReady) {
            boolean ready = ((Envelope.PlayerReady) envelope).ready;
            serverLogic.onPlayerReady(id, ready);
        } else if (envelope instanceof Envelope.SendCards) {
            List<BoardAction> cards = ((Envelope.SendCards) envelope).cards;
            serverLogic.onCardsReceived(id, cards);
        } else if (envelope instanceof Envelope.UnReadyMyCards) {
            serverLogic.onCardsReady(id, false);
        }
    }

    @Override
    public void onPlayerConnected(int playerId) {
        serverLogic.onPlayerConnected(playerId);
    }

    @Override
    public void onPlayerDisconnected(int playerId) {
        serverLogic.onPlayerDisconnected(playerId);
    }

}