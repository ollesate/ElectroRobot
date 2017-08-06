package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
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
import olof.sjoholm.Api.Turns;
import olof.sjoholm.GameWorld.Actors.GameBoardActor.OnEndActionEvent;
import olof.sjoholm.GameWorld.Actors.GameBoardActor.OnStartActionEvent;
import olof.sjoholm.GameWorld.Levels.Level;
import olof.sjoholm.GameWorld.SpawnPoint;
import olof.sjoholm.Net.Server.Player;
import olof.sjoholm.Utils.Constants;
import olof.sjoholm.Utils.Logger;

public class GameBoard extends Group implements EventListener {
    private final List<GameBoardActor> movingActors = new ArrayList<GameBoardActor>();
    private Set<SpawnPoint> occupiedSpawnPoints = new HashSet<SpawnPoint>();

    // TODO: extract away this. Would be cool to handle in base class.
    private List<GameBoardActor> spawnedActors = new ArrayList<GameBoardActor>();
    private Map<Player, PlayerToken> playerTokens = new HashMap<Player, PlayerToken>();
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

        for (GameBoardActor movingActor : movingActors) {
            // TODO: Can we remove this?
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
                if (!level.isWithinBounds(toTile.x, toTile.y)) {
                    isBlocked = true;
                }
            } else if (Math.abs(dir.x) > 0 && Math.abs(pos.x % Constants.STEP_SIZE) > 0) {
                if (!level.isWithinBounds(toTile.x, toTile.y)) {
                    isBlocked = true;
                }
            }
            if (isBlocked) {
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
        playerToken.setX(spawnPoint.x * Constants.STEP_SIZE);
        playerToken.setY(spawnPoint.y * Constants.STEP_SIZE);
        playerToken.setColor(player.getColor());
        playerToken.setSpawnPoint(spawnPoint);
        addActor(playerToken);

        Badge badge = new Badge(playerToken, player.getName());
        badges.add(badge);
        addActor(badge);

        playerTokens.put(player, playerToken);
        spawnedActors.add(playerToken);
    }

    private List<Badge> badges = new ArrayList<Badge>();

    public void performActions(PlayerAction... playerActions) {
        SequenceAction sequence = new SequenceAction();
        for (PlayerAction action : playerActions) {
            Player player = action.player;
            BoardAction boardAction = action.boardAction;
            PlayerToken playerToken = playerTokens.get(player);
            if (playerToken == null) {
                // TODO: handle player died
            }
            sequence.addAction(new FireEventAction(new OnStartActionEvent(player, boardAction)));
            sequence.addAction(new ActionWrapper(playerToken, action.boardAction));
            sequence.addAction(new FireEventAction(new OnEndActionEvent(player, boardAction)));
        }
        addAction(sequence);
    }

    public void startTurns(Turns turns) {
        float playSpeed = Config.get(Config.PLAY_SPEED);
        SequenceAction sequence = new SequenceAction();
        for (int i = 0; i < turns.size(); i++) {
            for (PlayerAction playerAction : turns.getTurn(i)) {
                Player player = playerAction.player;
                BoardAction boardAction = playerAction.boardAction;
                PlayerToken playerToken = playerTokens.get(player);
                if (playerToken == null) {
                    // TODO: handle player died
                }

                sequence.addAction(new FireEventAction(new OnStartActionEvent(player, boardAction)));
                sequence.addAction(new ActionWrapper(playerToken, boardAction));
                sequence.addAction(new FireEventAction(new OnEndActionEvent(player, boardAction)));
                sequence.addAction(new DelayAction(Constants.CARD_POST_DELAY / playSpeed));
            }
        }
        addAction(sequence);
    }

    @Override
    public boolean handle(Event e) {
        Logger.d("Event " + e);
        if (e instanceof OnStartActionEvent) {
            // On start action
            OnStartActionEvent event = (OnStartActionEvent) e;
            PlayerToken playerToken = playerTokens.get(event.player);
            if (playerToken == null) {
                // TODO: handle player died. Should be possible?
            }
            movingActors.add(playerToken);
        } else if (e instanceof OnEndActionEvent) {
            // On end action
            OnEndActionEvent event = (OnEndActionEvent) e;
            PlayerToken playerToken = playerTokens.get(event.player);
            if (playerToken == null) {
                // TODO: handle player died. Should be possible?
            }
            movingActors.add(playerToken);
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
}
