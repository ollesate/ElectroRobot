package olof.sjoholm.Api;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import olof.sjoholm.Client.stages.DrawableActor;
import olof.sjoholm.GameWorld.Actors.GameBoard;
import olof.sjoholm.GameWorld.Actors.GameBoard.AllPlayersShootAction;
import olof.sjoholm.GameWorld.Actors.GameBoardActor.OnEndActionEvent;
import olof.sjoholm.GameWorld.Actors.GameBoardActor.OnStartActionEvent;
import olof.sjoholm.GameWorld.Actors.PlayerAction;
import olof.sjoholm.GameWorld.Actors.PlayerToken;
import olof.sjoholm.GameWorld.Assets.TextureDrawable;
import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.GameWorld.Levels;
import olof.sjoholm.Net.Both.Envelope;
import olof.sjoholm.Net.Server.Player;
import olof.sjoholm.Utils.Constants;
import olof.sjoholm.Utils.Logger;
import olof.sjoholm.Views.CountDownText;
import olof.sjoholm.Views.GameStage;

public class ServerGameScreen extends ServerScreen implements EventListener, Turn.OnTurnFinishedListener {
    private final GameStage gameStage;
    private boolean paused;
    private final GameBoard gameBoard;
    private final CardFlowPanel cardFlowPanel;
    private Turn currentTurn;

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

        startServer();

        cardFlowPanel = new CardFlowPanel();
        gameStage.addActor(cardFlowPanel);
    }

    private Actor getCard(Color color, final String col) {
        DrawableActor drawableActor = new DrawableActor(new TextureDrawable(Textures.BACKGROUND)) {
            @Override
            public String toString() {
                return col;
            }
        };
        drawableActor.setColor(color);
        drawableActor.setHeight(GraphicsUtil.dpToPixels(50));
        return drawableActor;
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(gameStage);
        debugStartTurn();
    }

    private void debugStartTurn() {
        final List<Player> players = new ArrayList<Player>();
        for (int i = 0; i < 1; i++) {
            Player player = DebugUtil.getPlayer(i);
            players.add(player);
            gameBoard.createPlayerToken(player);
        }
        Turn turn = DebugUtil.generateTurns(players);
        turn.setFinishedListener(new Turn.OnTurnFinishedListener() {
            @Override
            public void onTurnFinished() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Logger.d("Start new turn soon");
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Turn newTurn = DebugUtil.generateTurns(players);

                        gameBoard.startTurn(newTurn);
                        cardFlowPanel.setTurn(newTurn);
                        currentTurn = newTurn;
                    }
                }).start();
            }
        });
        gameBoard.startTurn(turn);
        cardFlowPanel.setTurn(turn);
        currentTurn = turn;
    }

    @Override
    public void resize(int width, int height) {
        gameStage.getViewport().update(width, height);

        cardFlowPanel.setWidth(0.2f * width);
        cardFlowPanel.setPosition(0.8f * width, height, Align.topLeft);
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
        switch (gamePhase) {
            case LOBBY:
                onMessageLobby(player, envelope);
                break;
            case CARD:
                onMessageWaitingForCards(player, envelope);
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
            if (allReady) {
                startCardPhase();
            }
        }
    }

    private void startCardPhase() {
        float delay = Config.get(Config.CARD_WAIT);

        gamePhase = GamePhase.CARD;
        for (Player connectedPlayer : getConnectedPlayers()) {
            send(connectedPlayer, new Envelope.StartGame());
            send(connectedPlayer, new Envelope.StartCountdown(delay));
            List<BoardAction> cards = CardGenerator.generateList(Constants.CARDS_TO_DEAL);
            send(connectedPlayer, new Envelope.SendCards(cards));
        }
        // Let them arrange their cards before calling turn ended.
        new Timer().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                onCardPhaseEnd();
            }
        }, delay);

        CountDownText countDownText = new CountDownText();
        countDownText.startCountDown(delay);
        gameStage.addActor(countDownText);
    }

    private void onCardPhaseEnd() {
        // TODO: do something if someone disconnects
        for (Player player : getConnectedPlayers()) {
            // This will tell them to return cards to play.
            send(player, new Envelope.OnCardPhaseEnded());
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
        for (Player player : getConnectedPlayers()) {
            List<BoardAction> cards = cardsToPlay.get(player);
            for (int i = 0; i < Constants.CARDS_TO_PLAY; i++) {
                turn.addToRound(i, new PlayerAction(player, cards.get(i)));
            }
        }
        gameBoard.startTurn(turn);
        cardFlowPanel.setTurn(turn);

        turn.setFinishedListener(this);
    }

    private Map<Player, List<BoardAction>> cardsToPlay = new HashMap<Player, List<BoardAction>>();

    private void onMessageWaitingForCards(Player player, Envelope envelope) {
        Logger.d("onMessageWaitingForCards " + envelope);
        if (envelope instanceof Envelope.SendCards) {
            List<BoardAction> cards = ((Envelope.SendCards) envelope).cards;
            cardsToPlay.put(player, cards);
        }
        if (envelope instanceof Envelope.UnReadyMyCards) {
            cardsToPlay.remove(player);
        }
    }

    @Override
    public void onPlayerConnected(final Player player) {
        Logger.d("onPlayerConnected");
        player.setColor(Color.WHITE);
        player.setName("Player " + player.getId());
        // TODO: will throw NPE here.
        gameBoard.createPlayerToken(player);
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
            cardFlowPanel.next();
        } else if (event instanceof OnEndActionEvent) {
            OnEndActionEvent endEvent = (OnEndActionEvent) event;
            Player player = endEvent.player;
            send(player, new Envelope.OnCardDeactivated(endEvent.boardAction));

            if (currentTurn.isLastOfRound(endEvent.boardAction)) {
                int whatRound = currentTurn.getRoundOf(endEvent.boardAction);
                PopupText popupText = new PopupText("Round " + (whatRound + 2),
                        Fonts.get(Fonts.FONT_60), Constants.NEW_ROUND_POPUP_DURATION);
                popupText.start();
                popupText.setWidth(gameStage.getWidth());
                popupText.setAlignment(Align.center);
                popupText.setPosition(0, gameStage.getHeight(), Align.topLeft);
                gameStage.addActor(popupText);
            }
        } else if (event instanceof AllPlayersShootAction) {
            cardFlowPanel.next();
        } else if (event instanceof PlayerToken.DamagedEvent) {
            // Player got damaged
            PlayerToken.DamagedEvent damagedEvent = (PlayerToken.DamagedEvent) event;
            int damaged = damagedEvent.maxHealth - damagedEvent.healthLeft;
            PlayerToken playerToken = (PlayerToken) event.getTarget();
            send(playerToken.getPlayer(), new Envelope.UpdateDamage(damaged));
        }
        return false;
    }

    @Override
    public void onTurnFinished() {
        startCardPhase();
    }
}
