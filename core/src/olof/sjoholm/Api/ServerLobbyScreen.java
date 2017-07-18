package olof.sjoholm.Api;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import olof.sjoholm.Net.Both.Envelope;
import olof.sjoholm.Net.Server.Server;
import olof.sjoholm.Utils.Logger;

public class ServerLobbyScreen extends ServerScreen {
    private final ScreenHandler screenHandler;

    private PlayersLabel playersLabel;
    private StartGameButton startGameButton;
    private int connectedPlayers;

    public ServerLobbyScreen(Server server, ScreenHandler screenHandler) {
        super(server);
        this.screenHandler = screenHandler;
        initLayout();
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void onMessage(Envelope envelope) {

    }

    @Override
    public void onPlayerConnected(Player player) {
        connectedPlayers++;
        playersLabel.setPlayers(connectedPlayers);
        startGameButton.setPlayers(connectedPlayers);
    }

    @Override
    public void onPlayerDisconnected(Player player) {
        connectedPlayers--;
        playersLabel.setPlayers(connectedPlayers);
        startGameButton.setPlayers(connectedPlayers);
    }

    private void initLayout() {
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        startGameButton = new StartGameButton(skin);
        playersLabel = new PlayersLabel(skin);

        Table table = new Table(skin);
        table.setFillParent(true);
        table.center();
        table.add(playersLabel).width(100);
        table.row();
        table.add(new LoadingLabel(skin)).width(100);
        table.row();
        table.add(startGameButton).width(100);

        addActor(table);
    }

    private void startGame() {
        broadcast(new Envelope.StartGame());
        screenHandler.showGameScreen();
    }

    private static class PlayersLabel extends Label {
        private static final String baseText = "Number of players: ";

        PlayersLabel(Skin skin) {
            super("", skin);
            setPlayers(0);
        }

        void setPlayers(int nrOfPlayers) {
            setText(baseText + nrOfPlayers);
        }
    }

    private class StartGameButton extends TextButton {
        private int players = 0;

        StartGameButton(Skin skin) {
            super("Start game", skin);
            addListener(new ChangeListener() {
                public void changed (ChangeEvent event, Actor actor) {
                    if (players > 0) {
                        Logger.d("Start game");
                        startGame();
                    }
                }
            });
        }

        void setPlayers(int nrOfPlayers) {
            this.players = nrOfPlayers;
        }
    }

    private static class LoadingLabel extends Label {
        private static final String baseText = "Waiting for players";

        LoadingLabel(Skin skin) {
            super(baseText + "...", skin);
            addAction(new Action() {
                private float elapsedTime;
                private float duration = 1.0f;
                private int currentIndex;

                @Override
                public boolean act(float delta) {
                    elapsedTime += delta;
                    if (elapsedTime > duration) {
                        elapsedTime = 0;
                        int nrOfDots = currentIndex + 1;
                        String dotsText = "";
                        for (int i = 0; i < nrOfDots; i++) {
                            dotsText += ".";
                        }
                        setText(baseText + dotsText);
                        currentIndex = (currentIndex + 1) % 3;
                    }
                    return false;
                }
            });
        }
    }
}
