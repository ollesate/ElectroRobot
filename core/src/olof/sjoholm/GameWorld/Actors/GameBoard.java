package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import olof.sjoholm.Api.BoardAction;
import olof.sjoholm.Api.Config;
import olof.sjoholm.Api.Turn;
import olof.sjoholm.GameWorld.Actors.GameBoardActor.OnEndActionEvent;
import olof.sjoholm.GameWorld.Actors.GameBoardActor.OnStartActionEvent;
import olof.sjoholm.GameWorld.Levels.Level;
import olof.sjoholm.Net.Server.Player;
import olof.sjoholm.Utils.Constants;
import olof.sjoholm.Utils.Logger;

public class GameBoard extends Group implements EventListener {
    private List<SpawnPoint> spawnPoints;

    // TODO: extract away this. Would be cool to handle in base class.
    private List<GameBoardActor> spawnedActors = new ArrayList<GameBoardActor>();
    public Map<Player, PlayerToken> playerTokens = new HashMap<Player, PlayerToken>();
    private int tileSize;
    private Level level;

    public GameBoard(int tileSize) {
        this.tileSize = tileSize;
        addListener(this);
    }

    public void loadMap(Level level) {
        this.level = level;
        clearChildren(); // TODO: why?
        level.create(this, tileSize);
        spawnPoints = level.getSpawnPoints();
    }

    public void createPlayerToken(Player player) {
        SpawnPoint spawnPoint = findSpawnPoint();
        if (spawnPoint == null) {
            // TODO?
        }
        spawnPlayerToken(player, spawnPoint);
    }

    public PlayerToken spawnPlayerToken(Player player, SpawnPoint spawnPoint) {
        spawnPoint.setOwner(player);
        spawnPoint.setColor(player.getColor());

        PlayerToken playerToken = new PlayerToken();
        playerToken.setSize(tileSize, tileSize);
        playerToken.setX(spawnPoint.getBoardX() * tileSize);
        playerToken.setY(spawnPoint.getBoardY() * tileSize);
        playerToken.setColor(player.getColor());
        playerToken.setPlayer(player);
        addActor(playerToken);

        playerTokens.put(player, playerToken);
        playerToken.setPlayerName(player.getName());
        spawnedActors.add(playerToken);

        return playerToken;
    }

    private SpawnPoint findSpawnPoint() {
        for (SpawnPoint spawnPoint : spawnPoints) {
            if (spawnPoint.getOwner() == null) {
                return spawnPoint;
            }
        }
        return null;
    }

    private SpawnPoint getSpawnPoint(Player player) {
        for (SpawnPoint spawnPoint : spawnPoints) {
            if (player.equals(spawnPoint.getOwner())) {
                return spawnPoint;
            }
        }
        throw new IllegalStateException("No spawn point for " + player);
    }

    public void startTurn(final Turn turn) {
        float playSpeed = Config.get(Config.PLAY_SPEED);
        SequenceAction sequence = new SequenceAction();

        // Spawn dead players again
        for (Map.Entry<Player, PlayerToken> entry : playerTokens.entrySet()) {
            boolean isDead = entry.getValue() == null;
            Logger.d(entry.getKey().getName() +  " token is " + (isDead ? "dead" : "alive"));


            if (entry.getValue() == null) { // Player died
                Logger.d("Spawn player token");
                Player player = entry.getKey();

                SpawnPoint spawnPoint = getSpawnPoint(player);

                PlayerToken spawnedToken = spawnPlayerToken(player, spawnPoint);
                entry.setValue(spawnedToken);
            }
        }

        for (int i = 0; i < turn.size(); i++) {
            List<Action> shootActions = new ArrayList<Action>();

            for (PlayerAction playerAction : turn.getRound(i)) {
                Player player = playerAction.player;
                BoardAction boardAction = playerAction.boardAction;

                sequence.addAction(new FireEventAction(new OnStartActionEvent(player, boardAction)));
                sequence.addAction(new DoPlayerAction(this, playerAction));
                sequence.addAction(new FireEventAction(new OnEndActionEvent(player, boardAction)));
                sequence.addAction(new DelayAction(Constants.CARD_POST_DELAY / playSpeed));

                shootActions.add(new DoPlayerAction(this, new PlayerAction(player,
                        new BoardAction.Shoot())));
            }
            // Let all players shoot
            ParallelAction allShootAction = new ParallelAction();
            for (Action shootAction : shootActions) {
                allShootAction.addAction(shootAction);
            }
            sequence.addAction(new FireEventAction(new AllPlayersShootAction()));
            sequence.addAction(allShootAction);
        }
        sequence.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                turn.finished();
                return true;
            }
        });

        addAction(sequence);
    }

    @Override
    public boolean handle(Event e) {
        if (e instanceof PlayerToken.Destroyed) {
            PlayerToken playerToken = ((PlayerToken.Destroyed) e).playerToken;
            cleanPlayerToken(playerToken);
        }
        return false;
    }

    public Level getLevel() {
        return level;
    }

    public List<GameBoardActor> getActorsAt(int x, int y) {
        List<GameBoardActor> actors = new ArrayList<GameBoardActor>();
        for (GameBoardActor spawnedActor : spawnedActors) {
            if ((int) (spawnedActor.getX() / Constants.STEP_SIZE) == x &&
                    (int) (spawnedActor.getY() / Constants.STEP_SIZE) == y) {
                actors.add(spawnedActor);
            }
        }
        return actors;
    }

    public List<GameBoardActor> getActors(float x, float y, float width, float height) {
        List<GameBoardActor> actors = new ArrayList<GameBoardActor>();
        for (GameBoardActor actor : spawnedActors) {
            if (actor.getX() + actor.getWidth() < x || actor.getX() > x + width) {
                continue;
            }
            if (actor.getY() + actor.getHeight() < y || actor.getY() > y + height) {
                continue;
            }
            actors.add(actor);
        }
        return actors;
    }

    public static Vector2 getBoardPosition(float x, float y) {
        return new Vector2(x / Constants.STEP_SIZE, y / Constants.STEP_SIZE);
    }

    public void removePlayer(Player player) {
        PlayerToken playerToken = playerTokens.get(player);
        removeActor(playerToken);
        cleanPlayerToken(playerToken);
    }

    private void cleanPlayerToken(PlayerToken playerToken) {
        playerTokens.put(playerToken.getPlayer(), null);
        spawnedActors.remove(playerToken);
    }

    public void updatePlayer(Player player) {
        PlayerToken playerToken = playerTokens.get(player);
        if (playerToken != null) {
            playerToken.setColor(player.getColor());
            player.setName(player.getName());
        }
    }

    public PlayerToken getPlayerToken(Player player) {
        return playerTokens.get(player);
    }

    public static class AllPlayersShootAction extends Event {

    }
}
