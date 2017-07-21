package olof.sjoholm.Api;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import olof.sjoholm.GameWorld.Actors.GameBoard;
import olof.sjoholm.GameWorld.Actors.PlayerToken;
import olof.sjoholm.GameWorld.Maps;
import olof.sjoholm.Net.Both.Envelope;
import olof.sjoholm.Net.Server.Player;
import olof.sjoholm.Utils.Constants;
import olof.sjoholm.Utils.Logger;
import olof.sjoholm.Utils.Rotation;
import olof.sjoholm.Views.CountDownText;
import olof.sjoholm.Views.GameStage;

public class ServerGameScreen extends ServerScreen {
    private final GameStage gameStage;

    public ServerGameScreen(ServerScreenHandler serverScreenHandler) {
        gameStage = new GameStage();
        GameBoard gameBoard = new GameBoard((int) Constants.STEP_SIZE);
        gameBoard.loadMap(Maps.Level1());

        Maps.SpawnPoint spawnPoint = gameBoard.getSpawnPoints().get(0);
        PlayerToken playerToken = new PlayerToken();
        gameBoard.spawnToken(spawnPoint, playerToken);
        gameBoard.performActions(
                new GameBoard.PlayerAction(playerToken, new BoardAction.Rotate(Rotation.LEFT)),
                new GameBoard.PlayerAction(playerToken, new BoardAction.MoveForward(4))
        );


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
        gameStage.act(delta);
        gameStage.draw();
    }

    @Override
    public void onMessage(Player player, Envelope envelope) {

    }

    @Override
    public void onPlayerConnected(Player player) {

    }

    @Override
    public void onPlayerDisconnected(Player player) {

    }
}
