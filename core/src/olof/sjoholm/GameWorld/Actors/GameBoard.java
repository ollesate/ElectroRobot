package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import olof.sjoholm.Api.BoardAction;
import olof.sjoholm.Api.Config;
import olof.sjoholm.Api.Turn;
import olof.sjoholm.GameWorld.Actors.GameBoardActor.OnEndActionEvent;
import olof.sjoholm.GameWorld.Actors.GameBoardActor.OnStartActionEvent;
import olof.sjoholm.GameWorld.Levels.Level;
import olof.sjoholm.GameWorld.SpawnPoint;
import olof.sjoholm.Net.Server.Player;
import olof.sjoholm.Utils.Constants;
import olof.sjoholm.Utils.Logger;

public class GameBoard extends Group implements EventListener {
    private Set<SpawnPoint> occupiedSpawnPoints = new HashSet<SpawnPoint>();

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
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        for (Badge badge : badges) {
            badge.update();
        }
    }

    private Point getLeftDownTilePos(PlayerToken playerToken) {
        Vector2 pos = new Vector2(playerToken.getX(), playerToken.getY());

        int tileY;
        if (pos.y > 0) {
            tileY = (int) (pos.y / Constants.STEP_SIZE);
        } else {
            tileY = (int) (pos.y / Constants.STEP_SIZE) - 1;
        }

        int tileX;
        if (pos.x > 0) {
            tileX = (int) (pos.x / Constants.STEP_SIZE);
        } else {
            tileX = (int) (pos.x / Constants.STEP_SIZE) - 1;
        }
        return new Point(tileX, tileY);
    }

    public List<SpawnPoint> getSpawnPoints() {
        List<SpawnPoint> availableSpots = new ArrayList<SpawnPoint>();
        for (SpawnPoint spawnPoint : level.getSpawnPoints()) {
            if (!occupiedSpawnPoints.contains(spawnPoint)) {
                availableSpots.add(spawnPoint);
            }
        }
        return availableSpots;
    }

    public void initializePlayer(SpawnPoint spawnPoint, Player player) {
        occupiedSpawnPoints.add(spawnPoint);

        PlayerToken playerToken = new PlayerToken();
        playerToken.setSize(tileSize, tileSize);
        playerToken.setX(spawnPoint.x * Constants.STEP_SIZE);
        playerToken.setY(spawnPoint.y * Constants.STEP_SIZE);
        playerToken.setColor(player.getColor());
        playerToken.setSpawnPoint(spawnPoint);
        playerToken.setPlayer(player);
        addActor(playerToken);

        Badge badge = new Badge(playerToken, player.getName());
        badges.add(badge);
        addActor(badge);

        playerTokens.put(player, playerToken);
        spawnedActors.add(playerToken);
    }

    private List<Badge> badges = new ArrayList<Badge>();

    public void startTurn(Turn turn) {
        float playSpeed = Config.get(Config.PLAY_SPEED);
        SequenceAction sequence = new SequenceAction();
        for (int i = 0; i < turn.size(); i++) {
            List<Action> shootActions = new ArrayList<Action>();

            for (PlayerAction playerAction : turn.getRound(i)) {
                Player player = playerAction.player;
                BoardAction boardAction = playerAction.boardAction;
                PlayerToken playerToken = playerTokens.get(player);
                if (playerToken == null) {
                    // TODO: handle player died
                    throw new IllegalStateException("Player is dead for some reason?");
                }

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
        addAction(sequence);
    }

    @Override
    public boolean handle(Event e) {
        Logger.d("Event " + e);
        if (e instanceof PlayerToken.Destroyed) {
            playerTokens.values().remove(((PlayerToken.Destroyed) e).playerToken);
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
        SpawnPoint spawnPoint = playerToken.getSpawnPoint();
        occupiedSpawnPoints.remove(spawnPoint);
        Badge badgeToRemove = null;
        for (Badge badge : badges) {
            if (badge.getTarget().equals(playerToken)) {
                badgeToRemove = badge;
            }
        }
        removeActor(badgeToRemove);
    }

    public void updatePlayer(Player player) {
        PlayerToken playerToken = playerTokens.get(player);
        if (playerToken != null) {
            playerToken.setColor(player.getColor());
        }
        for (Badge badge : badges) {
            if (badge.getTarget().equals(playerToken)) {
                badge.setText(player.getName());
            }
        }
    }

    public PlayerToken getPlayerToken(Player player) {
        return playerTokens.get(player);
    }

    public static class AllPlayersShootAction extends Event {

    }
}
