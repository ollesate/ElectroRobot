package olof.sjoholm.game.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import olof.sjoholm.assets.Fonts;
import olof.sjoholm.configuration.Config;
import olof.sjoholm.configuration.Constants;
import olof.sjoholm.game.server.logic.Levels;
import olof.sjoholm.game.server.logic.PlaySet;
import olof.sjoholm.game.server.logic.PlayerAction;
import olof.sjoholm.game.server.logic.Turn;
import olof.sjoholm.game.server.objects.CardFlowPanel;
import olof.sjoholm.game.server.objects.ConveyorBelt;
import olof.sjoholm.game.server.objects.EventLog;
import olof.sjoholm.game.server.objects.GameBoard;
import olof.sjoholm.game.server.objects.GameBoard.AllPlayersShootEvent;
import olof.sjoholm.game.server.objects.GameBoardActor;
import olof.sjoholm.game.server.objects.GameStage;
import olof.sjoholm.game.server.objects.OnEndActionEvent;
import olof.sjoholm.game.server.objects.OnStartActionEvent;
import olof.sjoholm.game.server.objects.Terminal;
import olof.sjoholm.game.server.server_logic.Player;
import olof.sjoholm.game.shared.logic.CardGenerator;
import olof.sjoholm.game.shared.logic.Movement;
import olof.sjoholm.game.shared.logic.Rotation;
import olof.sjoholm.game.shared.logic.cards.BoardAction;
import olof.sjoholm.game.shared.objects.PlayerToken;
import olof.sjoholm.utils.Logger;
import olof.sjoholm.utils.NumberUtils;
import olof.sjoholm.utils.ui.objects.LabelActor;

public class ServerGameScreen extends ScreenAdapter implements EventListener {
    private List<Player> players = new ArrayList<Player>();
    private final GameStage gameStage;
    private boolean paused;
    private final GameBoard gameBoard;
    private final CardFlowPanel cardFlowPanel;
    private Turn currentTurn;
    private final EventLog eventLog;
    private final Terminal terminal;

    public enum GamePhase {
        LOBBY,
        CARD,
        GAME;

    }
    private GamePhase gamePhase = GamePhase.LOBBY;
    public ServerGameScreen() {
        super();
        float tileSize = Constants.STEP_SIZE;
        gameBoard = new GameBoard((int) tileSize);
        gameStage = new GameStage(gameBoard);
        gameBoard.loadMap(Levels.level1());
        gameBoard.addListener(this);

        gameStage.addActor(gameBoard);

        cardFlowPanel = new CardFlowPanel();
        cardFlowPanel.setDebug(true);
        gameStage.addActor(cardFlowPanel);


        LabelActor actor = new LabelActor("Host: " + "undefined", Fonts.get(Fonts.FONT_34));
        gameStage.addActor(actor);
        eventLog = new EventLog();
        gameStage.addActor(eventLog);

        terminal = new Terminal();
        terminal.setSize(500, 500);
        terminal.setVisible(false);
        gameStage.addActor(terminal);

        gameStage.addListener(new InputListener() {

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.T:
                        if (!terminal.hasFocus()) {
                            terminal.setFocus(!terminal.isVisible());
                            terminal.setVisible(!terminal.isVisible());
                        }
                        return true;
                }
                return false;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                terminal.setFocus(terminal.hit(x, y, true) != null);
                return false;
            }
        });
        gameStage.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event instanceof TerminalEvent) {
                    TerminalEvent terminalEvent = (TerminalEvent) event;
                    new DebugTerminalController(gameBoard, terminal).handleEvent(terminalEvent);
                }
                return false;
            }
        });
    }

    public void setMessageListener(EventListener eventListener) {
        gameStage.addListener(eventListener);
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(gameStage);
    }

    @Override
    public void resize(int width, int height) {
        gameStage.getViewport().update(width, height);

        cardFlowPanel.setWidth(0.2f * width);
        cardFlowPanel.setPosition(0.8f * width, height, Align.topLeft);

        eventLog.setWidth(width);
        eventLog.setPosition(0, height, Align.topLeft);
    }

    @Override
    public void render(float delta) {
        if (false) {
            paused = !paused;
        }
        if (!paused) {
            gameStage.act(delta);
        }
        gameStage.draw();
    }

    public void updateAppearance(Player player) {
        gameBoard.updatePlayer(player);
    }

    public void allPlayersReady() {
        Logger.d("All players ready: start");
        startCardPhase();
    }

    public void onPlayerReady(Player player, boolean ready) {
        // Nothing to do here yet.
    }

    public void onPlayerCardsReady(Player player, boolean ready) {

    }

    public void onPlayerCardsReceived(Player player, List<BoardAction> cards) {

    }

    private void startCardPhase() {
        final float delay = Config.get(Config.CARD_WAIT);

        gamePhase = GamePhase.CARD;
        for (Player connectedPlayer : players) {
            connectedPlayer.startGame();
            connectedPlayer.startCountdown(delay);
            connectedPlayer.sendCards(CardGenerator.generateList(Constants.CARDS_TO_DEAL));
        }
        // Let them arrange their cards before calling turn ended.
        new Timer().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                onCardPhaseEnd();
            }
        }, delay);

        eventLog.addEventAction(new EventLog.TextAction() {
            float timeLeft = delay;

            @Override
            public String onTick(float delta) {
                timeLeft -= delta;
                return String.format("Time until card phase end %s", (int) timeLeft);
            }
        }, delay);
    }

    private void onCardPhaseEnd() {
        // TODO: do something if someone disconnects
        for (Player player : players) {
            // This will tell them to return cards to play.
            player.sendCardPhaseEnded();
        }
        // They need a little time to give their response.
        new Timer().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                gamePhase = GamePhase.GAME;
                // TODO: What to do if not everyone has given their cards?
                // Maybe just play in the order they were given out.
                onGameBegin();
            }
        }, Constants.WAIT_FOR_CARD_RESPONSE_DURATION);
    }

    private void onGameBegin() {
        List<PlaySet> playSets = new ArrayList<PlaySet>();
        for (Player player : players) {
            playSets.add(new PlaySet(gameBoard.getPlayerToken(player), player.getCards()));
        }
        gameBoard.playTurn(playSets);
    }

    public void onPlayerConnected(Player player) {
        Logger.d("onPlayerConnected");
        gameBoard.createPlayerToken(player);
        players.add(player);
    }

    public void onPlayerDisconnected(Player player) {
        Logger.d("onPlayerDisconnected");
        gameBoard.removePlayer(player);
        players.remove(player);
    }

    @Override
    public boolean handle(Event event) {
        if (event instanceof OnStartActionEvent) {
            OnStartActionEvent startEvent = (OnStartActionEvent) event;
            Player player = startEvent.playerToken.getPlayer();
            if (player != null) { // May be testing
                player.onCardActivated(startEvent.boardAction);
            }
            // TODO: implement again
            // cardFlowPanel.next();
        } else if (event instanceof OnEndActionEvent) {
            OnEndActionEvent endEvent = (OnEndActionEvent) event;
            Player player = endEvent.playerToken.getPlayer();
            if (player != null) { // We may be testing
                player.onCardDeactivated(endEvent.boardAction);
            }

            /*
            // TODO: implement again
            if (currentTurn.isLastOfRound(endEvent.boardAction)) {
                int currentRound = currentTurn.getRoundOf(endEvent.boardAction);
                eventLog.addEventText("Round " + (currentRound + 1), 4f);
            }
             */
        } else if (event instanceof AllPlayersShootEvent) {
            // TODO: reimplement
//            cardFlowPanel.next();
        } else if (event instanceof PlayerToken.DamagedEvent) {
            // Player got damaged
            PlayerToken.DamagedEvent damagedEvent = (PlayerToken.DamagedEvent) event;
            int damaged = damagedEvent.maxHealth - damagedEvent.healthLeft;
            PlayerToken playerToken = (PlayerToken) event.getTarget();
            Player player = playerToken.getPlayer();
            if (player != null) { // We may be testing
                player.updateDamage(damaged);
            }
        } else if (event instanceof GameBoard.TurnFinishedEvent) {
            if (players.isEmpty()) {
                // This shouldn't happen except during debugging so don't do anything here.
                return false;
            }
            for (Player player : players) {
                PlayerToken playerToken = gameBoard.getPlayerToken(player);
                if (playerToken == null) {
                    // Player died. Respawn new token
                    gameBoard.respawnPlayerToken(player);
                }
            }

            startCardPhase();
        }
        return false;
    }

    public void onTerminalError(String message) {
        terminal.writeError(message);
    }
}
