package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import olof.sjoholm.GameWorld.Actors.GameBoardActor.OnEndActionEvent;
import olof.sjoholm.GameWorld.Actors.GameBoardActor.OnStartActionEvent;
import olof.sjoholm.GameWorld.Maps.Map;
import olof.sjoholm.GameWorld.SpawnPoint;
import olof.sjoholm.Net.Server.Player;
import olof.sjoholm.Utils.Constants;
import olof.sjoholm.Utils.Logger;

public class GameBoard extends Group implements EventListener {
    private final List<GameBoardActor> movingActors = new ArrayList<GameBoardActor>();
    private int tileSize;
    private Map map;

    public GameBoard(int tileSize) {
        this.tileSize = tileSize;
        addListener(this);
    }

    public void loadMap(Map map) {
        this.map = map;
        clearChildren(); // TODO: why?
        map.create(this, tileSize);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        for (GameBoardActor movingActor : movingActors) {
            if (true) continue;
            PlayerToken playerToken = (PlayerToken) movingActor;
            Vector2 dir = playerToken.getDirection();
            Vector2 pos = new Vector2(playerToken.getX(), playerToken.getY());

            Point leftDownTile = getLeftDownTilePos(playerToken);
            int actualX, actualY;
            if (dir.x < 0) {
                actualX = leftDownTile.x + 1;
            } else {
                actualX = leftDownTile.x;
            }
            if (dir.y < 0) {
                actualY = leftDownTile.y + 1;
            } else {
                actualY = leftDownTile.y;
            }
            Point fromTile = new Point(actualX, actualY);
            Point toTile = new Point((int)(fromTile.x + dir.x), (int)(fromTile.y + dir.y));
            boolean isBlocked = false;
            if (Math.abs(dir.y) > 0 && Math.abs(pos.y % Constants.STEP_SIZE) > 0) {
                if (!map.isWithinBounds(toTile.x, toTile.y)) {
                    isBlocked = true;
                }
            } else if (Math.abs(dir.x) > 0 && Math.abs(pos.x % Constants.STEP_SIZE) > 0) {
                if (!map.isWithinBounds(toTile.x, toTile.y)) {
                    isBlocked = true;
                }
            }
//            Logger.d("From " + fromTile + " to " + toTile + " blocked " + isBlocked);
            if (isBlocked) {
//                Logger.d("Set x " + fromTile.x * Constants.STEP_SIZE + " y " + fromTile.y * Constants.STEP_SIZE);
                playerToken.setX(fromTile.x * Constants.STEP_SIZE);
                playerToken.setY(fromTile.y * Constants.STEP_SIZE);
            }
        }

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

//        Logger.d("Pos " + pos + " tile " + new Vector2(tileX, tileY));
        return new Point(tileX, tileY);
    }

    private Set<SpawnPoint> occupiedSpawnPoints = new HashSet<SpawnPoint>();

    public List<SpawnPoint> getSpawnPoints() {
        List<SpawnPoint> availableSpots = new ArrayList<SpawnPoint>();
        for (SpawnPoint spawnPoint : map.getSpawnPoints()) {
            if (!occupiedSpawnPoints.contains(spawnPoint)) {
                availableSpots.add(spawnPoint);
            }
        }
        return availableSpots;
    }

    private java.util.Map<Player, PlayerToken> players = new HashMap<Player, PlayerToken>();

    private List<GameBoardActor> spawnedActors = new ArrayList<GameBoardActor>();

    public void initializePlayer(SpawnPoint spawnPoint, Player player) {
        occupiedSpawnPoints.add(spawnPoint);

        PlayerToken playerToken = new PlayerToken();
        playerToken.setX(spawnPoint.x * Constants.STEP_SIZE);
        playerToken.setY(spawnPoint.y * Constants.STEP_SIZE);
        playerToken.setColor(player.color);
        playerToken.setSpawnPoint(spawnPoint);
        addActor(playerToken);


        Badge badge = new Badge(playerToken, player.getName());
        badges.add(badge);
        addActor(badge);

        players.put(player, playerToken);
        spawnedActors.add(playerToken);
    }

    public PlayerToken getToken(Player player) {
        return players.get(player);
    }

    private List<Badge> badges = new ArrayList<Badge>();

    public void performActions(PlayerAction... playerActions) {
        SequenceAction sequence = new SequenceAction();
        for (PlayerAction action : playerActions) {
            sequence.addAction(new FireEventAction(new OnStartActionEvent(action.playerToken)));
            sequence.addAction(new ActionWrapper(action.playerToken, action.boardAction));
            sequence.addAction(new FireEventAction(new OnEndActionEvent(action.playerToken)));
        }
        addAction(sequence);
    }

    @Override
    public boolean handle(Event e) {
        Logger.d("Event " + e);
        if (e instanceof OnStartActionEvent) {
            // On start action
            OnStartActionEvent event = (OnStartActionEvent) e;
            movingActors.add(event.gameBoardActor);
        } else if (e instanceof OnEndActionEvent) {
            // On end action
            OnEndActionEvent event = (OnEndActionEvent) e;
            movingActors.remove(event.gameBoardActor);
        }
        return false;
    }

    public Map getMap() {
        return map;
    }

    public List<GameBoardActor> getActorsAt(int x, int y) {
        List<GameBoardActor> actors = new ArrayList<GameBoardActor>();
        for (GameBoardActor spawnedActor : spawnedActors) {
            Logger.d("is actor at " + x + " " + y + "?" + (int) (spawnedActor.getX() / Constants.STEP_SIZE) + " " + (int) (spawnedActor.getY() / Constants.STEP_SIZE));
            if ((int) (spawnedActor.getX() / Constants.STEP_SIZE) == x &&
                    (int) (spawnedActor.getY() / Constants.STEP_SIZE) == y) {
                Logger.d("Yes");
                actors.add(spawnedActor);
            }
        }
        return actors;
    }

    public static Vector2 getBoardPosition(float x, float y) {
        return new Vector2(x / Constants.STEP_SIZE, y / Constants.STEP_SIZE);
    }

    public void removePlayer(Player player) {
        PlayerToken playerToken = players.get(player);
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
}
