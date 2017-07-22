package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import olof.sjoholm.GameWorld.Actors.GameBoardActor.OnEndActionEvent;
import olof.sjoholm.GameWorld.Actors.GameBoardActor.OnStartActionEvent;
import olof.sjoholm.GameWorld.Maps.Map;
import olof.sjoholm.GameWorld.Maps.SpawnPoint;
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

    public List<SpawnPoint> getSpawnPoints() {
        return map.getSpawnPoints();
    }

    private java.util.Map<Player, PlayerToken> players = new HashMap<Player, PlayerToken>();

    public void spawnPlayer(SpawnPoint spawnPoint, Player player) {
        PlayerToken playerToken = new PlayerToken();
        playerToken.setX(spawnPoint.x * Constants.STEP_SIZE);
        playerToken.setY(spawnPoint.y * Constants.STEP_SIZE);
        addActor(playerToken);
        playerToken.setColor(player.color);

        Badge badge = new Badge(playerToken, player.getName());
        badges.add(badge);
        addActor(badge);

        players.put(player, playerToken);
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
}
