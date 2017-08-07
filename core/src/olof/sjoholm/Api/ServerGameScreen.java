package olof.sjoholm.Api;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import olof.sjoholm.Client.stages.DrawableActor;
import olof.sjoholm.GameWorld.Actors.GameBoard;
import olof.sjoholm.GameWorld.Actors.GameBoardActor.OnEndActionEvent;
import olof.sjoholm.GameWorld.Actors.GameBoardActor.OnStartActionEvent;
import olof.sjoholm.GameWorld.Actors.PlayerAction;
import olof.sjoholm.GameWorld.Actors.PlayerToken;
import olof.sjoholm.GameWorld.Assets.TextureDrawable;
import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.GameWorld.Levels;
import olof.sjoholm.GameWorld.SpawnPoint;
import olof.sjoholm.Net.Both.Envelope;
import olof.sjoholm.Net.Server.Player;
import olof.sjoholm.Utils.Constants;
import olof.sjoholm.Utils.Logger;
import olof.sjoholm.Views.CountDownText;
import olof.sjoholm.Views.GameStage;

public class ServerGameScreen extends ServerScreen implements EventListener {
    private final GameStage gameStage;
    private boolean paused;
    private final GameBoard gameBoard;
    private final CardFlowPanel cardFlowPanel;
    private Turn currentTurn;
    private Missile missile;

    public enum GamePhase {
        LOBBY,
        CARD,
        GAME
    }

    private GamePhase gamePhase = GamePhase.LOBBY;

    public ServerGameScreen() {
        super();
        gameBoard = new GameBoard((int) Constants.STEP_SIZE);
        gameStage = new GameStage(gameBoard);
        gameBoard.loadMap(Levels.level1());
        gameBoard.addListener(this);

        CountDownText countDownText = new CountDownText();


        gameStage.addActor(gameBoard);

        startServer();

        cardFlowPanel = new CardFlowPanel();
        gameStage.addActor(cardFlowPanel);

        List<Player> players = new ArrayList<Player>();
        for (int i = 0; i < 4; i++) {
            Player player = DebugUtil.getPlayer(i);
            SpawnPoint spawnPoint = gameBoard.getSpawnPoints().get(0);
            gameBoard.initializePlayer(spawnPoint, player);
            players.add(player);
        }
        PlayerToken playerToken = gameBoard.playerTokens.get(players.get(0));
        playerToken.shoot();
//
//        currentTurn = DebugUtil.generateTurns(players);
//        cardFlowPanel.setTurns(currentTurn);
//        gameBoard.startTurns(currentTurn);

        gameBoard.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                return false;
            }
        });

//        missile = new Missile(4f, Direction.UP, Constants.MISSILE_SPEED);
//        missile.setColor(Color.RED);
//        missile.setPosition(100f, 100f);


//        Logger.d("Get pos " + missile.getX() + " " + missile.getY());
//        gameBoard.addActor(missile);

        gameStage.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                missile.setPosition(x, y, Align.center);
            }
        });
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

    private static class CardFlowPanel extends LinearLayout {
        private Map<BoardAction, CardActor> cardActors = new HashMap<BoardAction, CardActor>();

        private static class CardActor extends Group {

            private final TextureDrawable background;
            private final Label nameLabel;
            private final Color backgroundColor;
            private final Label cardLabel;
            private boolean selected;

            public CardActor(String player, String cardText, Color color) {
                backgroundColor = new Color(color);
                Label.LabelStyle style = new Label.LabelStyle(Fonts.get(Fonts.FONT_24), Color.BLACK);
                nameLabel = new Label(player, style);
                addActor(nameLabel);
                cardLabel = new Label(cardText, style);
                addActor(cardLabel);
                background = new TextureDrawable(Textures.BACKGROUND);
            }

            @Override
            protected void sizeChanged() {
                nameLabel.setHeight(getHeight());
                nameLabel.setX(0.05f * getWidth());
                cardLabel.setHeight(getHeight());
                cardLabel.setX(50 + nameLabel.getX(Align.topRight));
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                backgroundColor.set(backgroundColor.r, backgroundColor.g, backgroundColor.b,
                        selected ? 1f : 0.3f);
                background.draw(batch, parentAlpha, getX(), getY(), getWidth(), getHeight(),
                        1f, 1f, 0f, 0f, 0f, backgroundColor);
                super.draw(batch, parentAlpha);
            }

            public void select() {
                selected = true;
            }

            public void deselect() {
                selected = false;
            }

        }

        private static class RoundTitleActor extends Group {
            private final Label label;

            public RoundTitleActor(String round) {
                Label.LabelStyle style = new Label.LabelStyle(Fonts.get(Fonts.FONT_24), Color.BLACK);
                label = new Label(round, style);
                label.setAlignment(Align.center);
                addActor(label);
            }

            @Override
            protected void sizeChanged() {
                label.setSize(getWidth(), getHeight());
            }
        }

        public void setTurns(Turn turns) {
            for (int i = 0; i < turns.size(); i++) {
                List<PlayerAction> turn = turns.getRound(i);
                RoundTitleActor roundTitleActor = new RoundTitleActor("Round " + (i + 1));
                roundTitleActor.setHeight(GraphicsUtil.dpToPixels(100));
                addActor(roundTitleActor);
                for (PlayerAction playerAction : turn) {
                    add(playerAction);
                }
            }
        }

        public void add(PlayerAction playerAction) {
            Color color = playerAction.player.getColor();
            String name = playerAction.player.getName();
            String description = playerAction.boardAction.getText();
            CardActor cardActor = new CardActor(name, description, color);
            cardActor.setHeight(GraphicsUtil.dpToPixels(50));
            addActor(cardActor);
            cardActors.put(playerAction.boardAction, cardActor);
        }

        public void select(BoardAction boardAction) {
            CardActor cardActor = cardActors.get(boardAction);
            cardActor.select();
        }

        public void deselect(BoardAction boardAction) {
            CardActor cardActor = cardActors.get(boardAction);
            cardActor.deselect();
        }
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
                float delay = Config.get(Config.CARD_WAIT);
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
            }
            gamePhase = GamePhase.CARD;
        }
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
        gameBoard.startTurns(turn);
        cardFlowPanel.setTurns(turn);
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
        SpawnPoint spawnPoint = gameBoard.getSpawnPoints().get(0);
        gameBoard.initializePlayer(spawnPoint, player);
    }

    @Override
    public void onPlayerDisconnected(Player player) {
        Logger.d("onPlayerDisconnected");
        gameBoard.removePlayer(player);
    }

    private BoardAction selectedBoardAction;

    @Override
    public boolean handle(Event event) {
        if (event instanceof OnStartActionEvent) {
            OnStartActionEvent startEvent = (OnStartActionEvent) event;
            Player player = startEvent.player;
            send(player, new Envelope.OnCardActivated(startEvent.boardAction));
            if (selectedBoardAction != null) {
                cardFlowPanel.deselect(selectedBoardAction);
            }
            selectedBoardAction = startEvent.boardAction;
            cardFlowPanel.select(selectedBoardAction);
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
        }
        return false;
    }
}
