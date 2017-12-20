package olof.sjoholm.game.server.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import olof.sjoholm.configuration.Config;
import olof.sjoholm.configuration.Constants;
import olof.sjoholm.game.server.logic.DoPlayerAction;
import olof.sjoholm.game.server.logic.Levels.Level;
import olof.sjoholm.game.server.logic.PlayerAction;
import olof.sjoholm.game.server.logic.TileType;
import olof.sjoholm.game.server.logic.Turn;
import olof.sjoholm.game.shared.logic.cards.BoardAction;
import olof.sjoholm.game.shared.objects.PlayerToken;
import olof.sjoholm.game.server.server_logic.Player;
import olof.sjoholm.utils.Logger;
import olof.sjoholm.utils.actions.FireEventAction;

public class GameBoard extends Group implements EventListener {
    private List<SpawnPoint> spawnPoints;
    private List<GameBoardActor> gameBoardActors = new ArrayList<GameBoardActor>();
    public Map<Player, PlayerToken> playerTokens = new HashMap<Player, PlayerToken>();
    private int tileSize;
    private Level level;

    public GameBoard(int tileSize) {
        this.tileSize = tileSize;
        addListener(this);
    }

    @Override
    public void addActor(Actor actor) {
        super.addActor(actor);
        if (actor instanceof GameBoardActor) {
            gameBoardActors.add((GameBoardActor) actor);
        }
    }

    @Override
    public boolean removeActor(Actor actor, boolean unfocus) {
        if (actor instanceof GameBoardActor) {
            gameBoardActors.remove(actor);
        }
        return super.removeActor(actor, unfocus);
    }

    public GameBoardActor getActor(int id) {
        for (GameBoardActor gameBoardActor : gameBoardActors) {
            if (gameBoardActor.getId() == id) {
                return gameBoardActor;
            }
        }
        return null;
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

        return playerToken;
    }

    public PlayerToken spawnToken(int x, int y) {
        PlayerToken playerToken = new PlayerToken();
        playerToken.setSize(tileSize, tileSize);
        playerToken.setX(x * tileSize);
        playerToken.setY(y * tileSize);
        addActor(playerToken);
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

        for (int i = 0; i < turn.size(); i++) {
            for (PlayerAction playerAction : turn.getRound(i)) {
                Player player = playerAction.player;
                BoardAction boardAction = playerAction.boardAction;

                sequence.addAction(new FireEventAction(new OnStartActionEvent(player, boardAction)));
                sequence.addAction(new DoPlayerAction(this, playerAction));
                sequence.addAction(new FireEventAction(new OnEndActionEvent(player, boardAction)));
                sequence.addAction(new DelayAction(Constants.CARD_POST_DELAY / playSpeed));
            }
            sequence.addAction(new AllPlayersShootAction());
            sequence.addAction(new RunConveyorBeltAction());
            sequence.addAction(new ShootLasersAction());

        }
        sequence.addAction(new TurnFinishedAction());
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

    public <T extends GameBoardActor> List<T> getActorsAt(Point point, Class<T> clazz) {
        return getActorsAt(point.x, point.y, clazz);
    }

    public <T extends GameBoardActor> List<T> getActorsAt(int x, int y, Class<T> clazz) {
        List<T> actors = new ArrayList<T>();
        for (GameBoardActor spawnedActor : gameBoardActors) {
            if ((int) (spawnedActor.getX() / Constants.STEP_SIZE) == x &&
                    (int) (spawnedActor.getY() / Constants.STEP_SIZE) == y) {
                if (clazz != null && clazz.isInstance(spawnedActor)) {
                    actors.add(clazz.cast(spawnedActor));
                }
            }
        }
        return actors;
    }

    public List<GameBoardActor> getActorsAt(int x, int y) {
        return getActorsAt(x, y, null);
    }

    public List<GameBoardActor> getActors(float x, float y, float width, float height) {
        List<GameBoardActor> actors = new ArrayList<GameBoardActor>();
        for (GameBoardActor actor : gameBoardActors) {
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

    public <T> List<T> getActors(Class<T> clazz) {
        List<T> actors = new ArrayList<T>();
        for (Actor actor : getChildren()) {
            if (clazz.isInstance(actor)) {
                actors.add(clazz.cast(actor));
            }
        }
        return actors;
    }

    public static Point getBoardPosition(float x, float y) {
        return new Point((int)(x / Constants.STEP_SIZE), (int)(y / Constants.STEP_SIZE));
    }

    public Point getBoardPosition(GameBoardActor actor) {
        return new Point((int)(actor.getX() / Constants.STEP_SIZE),
                (int)(actor.getY() / Constants.STEP_SIZE));
    }

    public void removePlayer(Player player) {
        PlayerToken playerToken = playerTokens.get(player);
        removeActor(playerToken);
        cleanPlayerToken(playerToken);
    }

    private void cleanPlayerToken(PlayerToken playerToken) {
        playerTokens.put(playerToken.getPlayer(), null);
    }

    public void updatePlayer(Player player) {
        PlayerToken playerToken = playerTokens.get(player);
        if (playerToken != null) {
            playerToken.setColor(player.getColor());
            playerToken.setPlayerName(player.getName());
        }
    }

    public PlayerToken getPlayerToken(Player player) {
        return playerTokens.get(player);
    }

    public boolean isPit(int x, int y) {
        return level.getTile(x, y) == TileType.PIT;
    }

    public boolean isPassableTerrain(int x, int y) {
        return level.getTile(x, y) == TileType.FLOOR;
    }

    public boolean isAvailableSpace(int x, int y) {
        return isPassableTerrain(x, y)  && getActorsAt(x, y, PlayerToken.class).isEmpty();
    }

    public static class AllPlayersShootEvent extends Event {}

    public static class TurnFinishedEvent extends Event {}

    private class AllPlayersShootAction extends Action {
        Action shootActions;
        @Override
        public boolean act(float delta) {
            if (shootActions == null) {
                shootActions = getShootActions();
                fire(new AllPlayersShootEvent());
            }
            return shootActions.act(delta);
        }

        private Action getShootActions() {
            List<PlayerToken> actors = getActors(PlayerToken.class);
            ParallelAction parallelAction = new ParallelAction();
            for (PlayerToken token : actors) {
                parallelAction.addAction(token.getShootAction());
            }
            return parallelAction;
        }
    }

    private class RunConveyorBeltAction extends Action {
        Action beltActions;
        @Override
        public boolean act(float delta) {
            if (beltActions == null) {
                beltActions = getBeltActions();
            }
            return beltActions.act(delta);
        }

        private Action getBeltActions() {
            List<ConveyorBelt> actors = getActors(ConveyorBelt.class);
            ParallelAction parallelAction = new ParallelAction();
            for (ConveyorBelt conveyorBelt : actors) {
                parallelAction.addAction(conveyorBelt.getAction());
            }
            return parallelAction;
        }
    }

    private class ShootLasersAction extends Action {
        @Override
        public boolean act(float delta) {
            return true;
        }
    }

    private class TurnFinishedAction extends Action {

        @Override
            public boolean act(float delta) {
                fire(new TurnFinishedEvent());
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
                return true;
            }
    }
}
