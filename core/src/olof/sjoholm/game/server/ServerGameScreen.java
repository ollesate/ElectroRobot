package olof.sjoholm.game.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.assets.Fonts;
import olof.sjoholm.configuration.Config;
import olof.sjoholm.configuration.Constants;
import olof.sjoholm.game.server.logic.Levels;
import olof.sjoholm.game.server.logic.PlayerAction;
import olof.sjoholm.game.server.logic.Turn;
import olof.sjoholm.game.server.objects.CardFlowPanel;
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
                    String command = terminalEvent.getCommand();
                    if ("spawn".equals(command)) {
                        String what = terminalEvent.get(1);
                        if ("token".equals(what)) {
                            try {
                                int x = terminalEvent.getInt(2);
                                int y = terminalEvent.getInt(3);
                                int id = gameBoard.spawnToken(x, y).getId();
                                terminal.writeLine("Spawned token with id " +id);
                            } catch (TerminalException e) {
                                terminal.writeError(e.getMessage());
                            }
                        }
                    } else if ("token".equals(command)) {
                        PlayerToken playerToken = null;
                        try {
                            GameBoardActor gameBoardActor = gameBoard.getActor(terminalEvent.getInt(1));
                            if (gameBoardActor instanceof PlayerToken) {
                                playerToken = ((PlayerToken) gameBoardActor);
                            }
                        } catch (TerminalException e) {
                            return false;
                        }
                        if (playerToken == null) {
                            terminal.writeError("No token for " + terminalEvent.get(1));
                            return false;
                        }

                        if ("perform".equals(terminalEvent.get(2))) {
                            int length = terminalEvent.getLength(3);
                            if (length == 0) {
                                terminal.writeError("No actions");
                            }
                            SequenceAction sequenceAction = new SequenceAction();
                            for (int i = 3; i < length + 3; i++) {
                                String[] actions = terminalEvent.get(i).split(":");
                                String action = actions.length > 0 ? actions[0] : null;
                                Action gdxAction = null;
                                if ("move".equals(action) && actions.length == 3) {
                                    Movement movement = Movement.fromString(actions[1]);
                                    int steps = NumberUtils.toInt(actions[2], -1);
                                    if (movement != null && steps != -1) {
                                        terminal.writeLine("Perform movement " + movement + " " + steps);
                                        PlayerToken.MoveAction moveAction =
                                                new PlayerToken.MoveAction(movement, steps);
                                        moveAction.setPlayerToken(playerToken);
                                        gdxAction = moveAction;
                                    } else {
                                        terminal.writeError("Failure with move");
                                    }
                                } else if ("rotate".equals(action) && actions.length == 2) {
                                    Rotation rotation = Rotation.fromString(actions[1]);
                                    if (rotation != null) {
                                        terminal.writeLine("Perform rotation "+ rotation);
                                        gdxAction = playerToken.rotate(rotation);
                                    }
                                }
                                if (gdxAction != null) {
                                    sequenceAction.addAction(gdxAction);
                                }
                            }
                            if (sequenceAction.getActions().size > 0) {
                                System.out.print("add sequence");
                                gameStage.addAction(sequenceAction);
                            }
                        }
                    }
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
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
        Turn turn = new Turn(Constants.CARDS_TO_PLAY);
        for (Player player : players) {
            for (int i = 0; i < Constants.CARDS_TO_PLAY; i++) {
                turn.addToRound(i, new PlayerAction(player, player.getCards().get(i)));
            }
        }
        gameBoard.startTurn(turn);
        cardFlowPanel.setTurn(turn);

        currentTurn = turn;
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
            Player player = startEvent.player;
            player.onCardActivated(startEvent.boardAction);
            cardFlowPanel.next();
        } else if (event instanceof OnEndActionEvent) {
            OnEndActionEvent endEvent = (OnEndActionEvent) event;
            Player player = endEvent.player;
            player.onCardDeactivated(endEvent.boardAction);

            if (currentTurn.isLastOfRound(endEvent.boardAction)) {
                int currentRound = currentTurn.getRoundOf(endEvent.boardAction);
                eventLog.addEventText("Round " + (currentRound + 1), 4f);
            }
        } else if (event instanceof AllPlayersShootEvent) {
            cardFlowPanel.next();
        } else if (event instanceof PlayerToken.DamagedEvent) {
            // Player got damaged
            PlayerToken.DamagedEvent damagedEvent = (PlayerToken.DamagedEvent) event;
            int damaged = damagedEvent.maxHealth - damagedEvent.healthLeft;
            PlayerToken playerToken = (PlayerToken) event.getTarget();
            playerToken.getPlayer().updateDamage(damaged);
        } else if (event instanceof GameBoard.TurnFinishedEvent) {
            startCardPhase();
        }
        return false;
    }

    public void onTerminalError(String message) {
        terminal.writeError(message);
    }
}
