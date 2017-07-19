package olof.sjoholm.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import olof.sjoholm.Interfaces.OnMessageReceivedListener;
import olof.sjoholm.Utils.Robo;
import olof.sjoholm.Utils.Logger;
import olof.sjoholm.Net.Both.Envelope;

public class LobbyStage extends Stage {
    private PlayersLabel playersLabel;
    private StartGameButton startGameButton;
    private OnStartGameListener onStartGameListener;
    private int connectedPlayers;

    public LobbyStage(OnStartGameListener listener) {
        this.onStartGameListener = listener;
        init();
    }

    private void init() {
        initLayout();

        Robo.subscribeServerMessage(new OnMessageReceivedListener() {
            @Override
            public void onMessage(Envelope envelope, int clientId) {
                if (envelope instanceof Envelope.ClientConnection) {
                    connectedPlayers++;
                    onPlayersUpdated(connectedPlayers);
                } else if (envelope instanceof Envelope.ClientDisconnection) {
                    connectedPlayers--;
                    onPlayersUpdated(connectedPlayers);
                }
            }
        });
        addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                return false;
            }
        });
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
                        LobbyStage.this.onStartGameListener.onStartClicked();
                    }
                }
            });
        }

        public void setPlayers(int nrOfPlayers) {
            this.players = nrOfPlayers;
        }
    }

    public interface OnStartGameListener {

        void onStartClicked();
    }

}
