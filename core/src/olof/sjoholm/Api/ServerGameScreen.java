package olof.sjoholm.Api;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.GameWorld.Actors.GameBoard;
import olof.sjoholm.GameWorld.Actors.GameBoardActor.OnEndActionEvent;
import olof.sjoholm.GameWorld.Actors.GameBoardActor.OnStartActionEvent;
import olof.sjoholm.GameWorld.Actors.PlayerAction;
import olof.sjoholm.GameWorld.Levels;
import olof.sjoholm.GameWorld.SpawnPoint;
import olof.sjoholm.Net.Both.Envelope;
import olof.sjoholm.Net.Server.Player;
import olof.sjoholm.Utils.Constants;
import olof.sjoholm.Utils.Logger;
import olof.sjoholm.Views.CountDownText;
import olof.sjoholm.Views.GameStage;
import sun.rmi.runtime.Log;

public class ServerGameScreen extends ServerScreen implements EventListener {
    private final GameStage gameStage;
    private boolean paused;
    private final GameBoard gameBoard;

    public enum State {
        GAME_LOBBY,
        GAME_ON_GOING
    }

    private State state = State.GAME_LOBBY;

    public ServerGameScreen() {
        super();
        gameBoard = new GameBoard((int) Constants.STEP_SIZE);
        gameStage = new GameStage(gameBoard);
        gameBoard.loadMap(Levels.level1());
        gameBoard.addListener(this);

        CountDownText countDownText = new CountDownText();

        Table table = new Table();
        table.setFillParent(true);
        table.top();
        table.row();
        table.add(getUpperTable()).fillX();
        table.row();
        table.add(gameBoard).grow().center();
        table.addActor(countDownText);

        gameStage.addActor(table);

        startServer();
    }

    private Table getUpperTable() {
        Table table = new Table();
        table.center();
        table.left();
        // table.add(playerHand1).expandX().padBottom(10f).padTop(10f).bottom();
        table.add().expandX();
        // table.add(playerHand2).expandX().padBottom(10f).padTop(10f).bottom();
        return table;
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(gameStage);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            paused = !paused;
        }
        if (!paused) {
            gameStage.act(delta);
        }
        gameStage.draw();
    }

    @Override
    public void onMessage(Player player, Envelope envelope) {
        switch (state) {
            case GAME_LOBBY:
                onMessageLobby(player, envelope);
                break;
            case GAME_ON_GOING:
                onMessageGame(player, envelope);
                break;
        }
    }

    private void onMessageLobby(Player player, Envelope envelope) {
        Logger.d("onMessageLobby " + envelope);
        if (envelope instanceof Envelope.PlayerSelectColor) {
            player.setColor(((Envelope.PlayerSelectColor) envelope).getColor());
            gameBoard.updatePlayer(player);
        }
        if (envelope instanceof Envelope.PlayerSelectName) {
            player.setName(((Envelope.PlayerSelectName) envelope).name);
            gameBoard.updatePlayer(player);
        }
        if (envelope instanceof Envelope.PlayerReady) {
            boolean ready = ((Envelope.PlayerReady) envelope).ready;
            player.setReady(ready);
            boolean allReady = true;
            for (Player connectedPlayer : getConnectedPlayers()) {
                if (!connectedPlayer.isReady()) {
                    allReady = false;
                }
            }
            Logger.d("all players ready? " + allReady + " " + getConnectedPlayers().size());
            final Turns turns = new Turns(Constants.NR_OF_CARDS);
            if (allReady) {
                float delay = Config.get(Config.GAME_TURN);
                for (Player connectedPlayer : getConnectedPlayers()) {
                    send(connectedPlayer, new Envelope.StartGame());
                    send(connectedPlayer, new Envelope.StartCountdown(delay));
                    List<BoardAction> cards = CardGenerator.generateList(Constants.NR_OF_CARDS);
                    send(connectedPlayer, new Envelope.SendCards(cards));

                    for (int i = 0; i < Constants.NR_OF_CARDS; i++) {
                        BoardAction boardAction = cards.get(i);
                        turns.addToTurn(i, new PlayerAction(connectedPlayer, boardAction));
                    }
                }
                new Timer().scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {
                        // TODO: do something if someone disconnects
                        onMatchStarted(turns);
                    }
                }, delay);
            }
            state = State.GAME_ON_GOING;
        }
    }

    public static class Turns {
        private final List<List<PlayerAction>> playerActions = new ArrayList<List<PlayerAction>>();
        private final int turnSize;

        public Turns(int turnSize) {
            this.turnSize = turnSize;
            for (int i = 0; i < turnSize; i++) {
                playerActions.add(new ArrayList<PlayerAction>());
            }
        }

        public void addToTurn(int turn, PlayerAction playerAction) {
            List<PlayerAction> turnActions = this.playerActions.get(turn);
            turnActions.add(playerAction);
        }

        public List<PlayerAction> getTurn(int turn) {
            return playerActions.get(turn);
        }

        public int size() {
            return turnSize;
        }
    }

    private void onMatchStarted(Turns turns) {
        gameBoard.startTurns(turns);
    }

    private void onMessageGame(Player player, Envelope envelope) {
        Logger.d("onMessageGame " + envelope);
    }

    @Override
    public void onPlayerConnected(final Player player) {
        Logger.d("onPlayerConnected");
        player.setColor(Color.WHITE);
        player.setName("Player " + player.getId());
        // TODO: will throw NPE here.
        SpawnPoint spawnPoint = gameBoard.getSpawnPoints().get(0);
        gameBoard.initializePlayer(spawnPoint, player);
    }

    @Override
    public void onPlayerDisconnected(Player player) {
        Logger.d("onPlayerDisconnected");
        gameBoard.removePlayer(player);
    }

    @Override
    public boolean handle(Event event) {
        if (event instanceof OnStartActionEvent) {
            OnStartActionEvent startEvent = (OnStartActionEvent) event;
            Player player = startEvent.player;
            send(player, new Envelope.OnCardActivated(startEvent.boardAction));
        } else if (event instanceof OnEndActionEvent) {
            OnEndActionEvent endEvent = (OnEndActionEvent) event;
            Player player = endEvent.player;
            send(player, new Envelope.OnCardDeactivated(endEvent.boardAction));
        }
        return false;
    }
}
