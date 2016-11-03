package olof.sjoholm.GameWorld.Server.Lobby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import olof.sjoholm.GameWorld.Utils.Logger;
import olof.sjoholm.GameWorld.Utils.ScreenAdapter;

/**
 * Created by sjoholm on 02/10/16.
 */

public class LobbyScreen extends ScreenAdapter {
    private Stage stage;
    private PlayersLabel playersLabel;
    private StartGameButton startGameButton;
    private LobbyActions lobbyActions;

    public LobbyScreen(LobbyActions lobbyActions) {
        this.lobbyActions = lobbyActions;
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        initLayout();
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

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {

    }

    public void onPlayersUpdated(int connectedPlayers) {
        startGameButton.setPlayers(connectedPlayers);
        playersLabel.setPlayers(connectedPlayers);
    }

    private static class LoadingLabel extends Label {
        private static final String baseText = "Waiting for players";

        public LoadingLabel(Skin skin) {
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

    private static class PlayersLabel extends Label {
        private static final String baseText = "Number of players: ";

        public PlayersLabel(Skin skin) {
            super("", skin);
            setPlayers(0);
        }

        public void setPlayers(int nrOfPlayers) {
            setText(baseText + nrOfPlayers);
        }
    }

    private class StartGameButton extends TextButton {
        private int players = 0;

        public StartGameButton(Skin skin) {
            super("Start game", skin);
            addListener(new ChangeListener() {
                public void changed (ChangeEvent event, Actor actor) {
                    if (players > 0) {
                        Logger.d("Start game");
                        LobbyScreen.this.lobbyActions.onStartGame();
                    }
                }
            });
        }

        public void setPlayers(int nrOfPlayers) {
            this.players = nrOfPlayers;
        }
    }

    public interface LobbyActions {

        void onStartGame();

    }

}
