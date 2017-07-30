package olof.sjoholm.Api;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import olof.sjoholm.GameWorld.Actors.GameBoard;
import olof.sjoholm.GameWorld.Maps;
import olof.sjoholm.GameWorld.SpawnPoint;
import olof.sjoholm.Net.Both.Envelope;
import olof.sjoholm.Net.Server.Player;
import olof.sjoholm.Utils.Constants;
import olof.sjoholm.Utils.Logger;
import olof.sjoholm.Views.CountDownText;
import olof.sjoholm.Views.GameStage;

public class ServerGameScreen extends ServerScreen {
    private final GameStage gameStage;
    private boolean paused;
    private final GameBoard gameBoard;

    public ServerGameScreen(ServerScreenHandler serverScreenHandler) {
        super();
        gameBoard = new GameBoard((int) Constants.STEP_SIZE);
        gameStage = new GameStage(gameBoard);
        gameBoard.loadMap(Maps.Level1());

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

    }

    @Override
    public void onHandlePlayerConnected(Player player) {
        Logger.d("onPlayerConnected");
        player.setColor(Color.BROWN);
        player.setName("Player " + player.id);
        // TODO: will throw NPE here.
        SpawnPoint spawnPoint = gameBoard.getSpawnPoints().get(0);
        gameBoard.initializePlayer(spawnPoint, player);

        Envelope.SendCards sendCards = new Envelope.SendCards(CardGenerator.generateList(5));
        send(player, sendCards);
    }

    @Override
    public void onHandlePlayerDisconnected(Player player) {
        Logger.d("onPlayerDisconnected");
        gameBoard.removePlayer(player);
    }
}
