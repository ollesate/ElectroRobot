package olof.sjoholm.Api;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import olof.sjoholm.GameWorld.Actors.GameBoard;
import olof.sjoholm.GameWorld.Actors.GameBoardActor.OnEndActionEvent;
import olof.sjoholm.GameWorld.Actors.GameBoardActor.OnStartActionEvent;
import olof.sjoholm.GameWorld.Actors.PlayerAction;
import olof.sjoholm.GameWorld.Actors.PlayerToken;
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
//
//        Player player = new Player(1);
//        player.setName("Olof");
//        player.setColor(Color.ORANGE);
//        SpawnPoint spawnPoint = gameBoard.getSpawnPoints().get(0);
//        gameBoard.initializePlayer(spawnPoint, player);
//        PlayerToken token = gameBoard.getToken(player);
//
//        gameBoard.performActions(new PlayerAction(token, new BoardAction.MoveForward(2)),
//                new PlayerAction(token, new BoardAction.Rotate(Rotation.LEFT)),
//                new PlayerAction(token, new BoardAction.MoveForward(2)));
        startServer();

        onHandlePlayerConnected(new Player(1));
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
    public void onHandleMessage(Player player, Envelope envelope) {
        if (envelope instanceof Envelope.PlayerSelectColor) {
            player.setColor(((Envelope.PlayerSelectColor) envelope).color);
            gameBoard.updatePlayer(player);
        }
        if (envelope instanceof Envelope.PlayerSelectName) {
            player.setName(((Envelope.PlayerSelectName) envelope).name);
            gameBoard.updatePlayer(player);
        }
    }

    private Map<PlayerToken, Player> players = new HashMap<PlayerToken, Player>();

    @Override
    public void onHandlePlayerConnected(final Player player) {
        Logger.d("onPlayerConnected");
        player.setColor(Color.BROWN);
        player.setName("Player " + player.id);
        // TODO: will throw NPE here.
        SpawnPoint spawnPoint = gameBoard.getSpawnPoints().get(0);
        gameBoard.initializePlayer(spawnPoint, player);

        // TODO: we should not start game directly when someone joins.
        final List<BoardAction> boardActions = CardGenerator.generateList(5);

        Envelope.SendCards sendCards = new Envelope.SendCards(boardActions);
        send(player, sendCards);
        float turnDuration = Config.get(Config.STAGE_CARD_TURN_DURATION);
        send(player, new Envelope.StartCountdown(turnDuration));

        new Timer().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                PlayerAction[] actions = new PlayerAction[boardActions.size()];
                PlayerToken token = gameBoard.getToken(player);
                for (int i = 0; i < boardActions.size(); i++) {
                    actions[i] = new PlayerAction(token, boardActions.get(i));
                }
                gameBoard.performActions(actions);
            }
        }, turnDuration);
    }

    @Override
    public void onHandlePlayerDisconnected(Player player) {
        Logger.d("onPlayerDisconnected");
        gameBoard.removePlayer(player);
    }

    @Override
    public boolean handle(Event event) {
        if (event instanceof OnStartActionEvent) {
            OnStartActionEvent startEvent = (OnStartActionEvent) event;
            Player player = gameBoard.getPlayer(startEvent.playerAction.playerToken);
            send(player, new Envelope.OnCardActivated(startEvent.playerAction.boardAction));
        } else if (event instanceof OnEndActionEvent) {
            OnEndActionEvent endEvent = (OnEndActionEvent) event;
            Player player = gameBoard.getPlayer(endEvent.playerAction.playerToken);
            send(player, new Envelope.OnCardDeactivated(endEvent.playerAction.boardAction));
        }
        return false;
    }
}
