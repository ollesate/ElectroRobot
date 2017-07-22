package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import olof.sjoholm.Api.BoardAction;
import olof.sjoholm.GameWorld.Actors.GameBoardActor.OnEndActionEvent;
import olof.sjoholm.GameWorld.Actors.GameBoardActor.OnStartActionEvent;
import olof.sjoholm.GameWorld.Maps.Map;
import olof.sjoholm.GameWorld.Maps.SpawnPoint;
import olof.sjoholm.Net.Server.Player;
import olof.sjoholm.Skins;
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

    public static class Badge extends Label {
        private final Actor target;

        public Badge(Actor target, CharSequence text) {
            super(text, Skins.DEFAULT);
            this.target = target;
            Pixmap labelColor = new Pixmap(1, 1, Pixmap.Format.RGB888);
            labelColor.setColor(Color.GRAY);
            labelColor.fill();
            getStyle().background = new Image(new Texture(labelColor)).getDrawable();
        }

        public void update() {
            setX(target.getX() + (target.getWidth() - getWidth()) / 2);
            setY(target.getY() + (target.getHeight() - getHeight()) / 2);
        }
    }

    private List<Badge> badges = new ArrayList<Badge>();

    public static class PlayerAction {
        public final PlayerToken playerToken;
        public final BoardAction boardAction;

        public PlayerAction(PlayerToken playerToken, BoardAction boardAction) {
            this.playerToken = playerToken;
            this.boardAction = boardAction;
        }
    }

    public void performActions(PlayerAction... playerActions) {
        SequenceAction sequence = new SequenceAction();
        for (PlayerAction action : playerActions) {
            sequence.addAction(new FireEventAction(new OnStartActionEvent(action.playerToken)));
            sequence.addAction(new ActionWrapper(action.playerToken, action.boardAction));
            sequence.addAction(new FireEventAction(new OnEndActionEvent(action.playerToken)));
        }
        addAction(sequence);
    }

    public static class FireEventAction extends Action {
        private final Event event;

        public FireEventAction(Event event) {
            this.event = event;
        }

        @Override
        public boolean act(float delta) {
            Actor target = getTarget();
            target.fire(event);
            return true;
        }
    }

    private class ActionWrapper extends Action {
        private final PlayerToken playerToken;
        private final BoardAction boardAction;
        private final Action perform;
        private boolean added;

        public ActionWrapper(PlayerToken playerToken, BoardAction boardAction) {
            // TODO: Rename target action or similiar, or can we use add action here? No, dont think so
            this.playerToken = playerToken;
            this.boardAction = boardAction;
            perform = boardAction.perform(playerToken);
        }

        @Override
        public boolean act(float delta) {
            if (!added) {
                playerToken.addAction(perform);
                added = true;
            }
            return perform.act(delta);
        }
    }

    @Override
    protected void sizeChanged() {
        float width = getWidth();
        float height = getHeight();

        if (map != null) {
            float maxWidth = width / map.getWidth();
            float maxHeight = height / map.getHeight();

            float min = Math.min(maxWidth, maxHeight);

            tileSize = (int) min;
            Logger.d("New size: " + width + ", " + height + ". Tilesize is now set to " + tileSize);
        }
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
