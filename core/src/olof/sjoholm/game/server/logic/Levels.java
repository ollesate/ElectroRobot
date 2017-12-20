package olof.sjoholm.game.server.logic;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.game.server.objects.ConveyorBelt;
import olof.sjoholm.game.server.objects.SpawnPoint;
import olof.sjoholm.game.server.objects.Tile;
import olof.sjoholm.game.shared.logic.Rotation;

public class Levels {

    public static Level level1() {
        return new Level(
                new int[][]{
                        {6, 3, 3, 3, 3, 3, 3, 7},
                        {2, 0, 0, 0, 0, 0, 1, 4},
                        {2, 0, 0, 0, 0, 0, 0, 4},
                        {2, 1, 0, 0, 0, 0, 1, 4},
                        {9, 5, 5, 5, 5, 5, 5, 8},
                }
        );
    }

    public static class Level {
        public static final int OUT_OF_BOUNDS = -2;
        public static final int FLOOR = 0;
        public static final int SPAWN = 1;
        public static final int CONVEYOR_BELT_UP = 2;
        public static final int CONVEYOR_BELT_RIGHT = 3;
        public static final int CONVEYOR_BELT_DOWN = 4;
        public static final int CONVEYOR_BELT_LEFT = 5;
        public static final int CONVEYOR_BELT_UP_RIGHT = 6;
        public static final int CONVEYOR_BELT_RIGHT_RIGHT = 7;
        public static final int CONVEYOR_BELT_DOWN_RIGHT = 8;
        public static final int CONVEYOR_BELT_LEFT_RIGHT = 9;

        private final List<SpawnPoint> spawnPoints = new ArrayList<SpawnPoint>();
        private final int[][] tileArray;
        private final int width;
        private final int height;

        private Level(int[][] tileArray) {
            this.tileArray = tileArray;
            width = tileArray[0].length;
            height = tileArray.length;
        }

        public void create(Group group, int tileSize) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    switch (get(x, y)) {
                        case FLOOR:
                            addTile(group, tileSize, x, y);
                            break;
                        case SPAWN:
                            addTile(group, tileSize, x, y);
                            addSpawn(group, tileSize, x, y);
                            break;
                        case CONVEYOR_BELT_UP:
                            addActor(group, new ConveyorBelt(Direction.UP, 1),tileSize, x, y);
                            break;
                        case CONVEYOR_BELT_RIGHT:
                            addActor(group, new ConveyorBelt(Direction.RIGHT, 1),tileSize, x, y);
                            break;
                        case CONVEYOR_BELT_DOWN:
                            addActor(group, new ConveyorBelt(Direction.DOWN, 1),tileSize, x, y);
                            break;
                        case CONVEYOR_BELT_LEFT:
                            addActor(group, new ConveyorBelt(Direction.LEFT, 1),tileSize, x, y);
                            break;
                        case CONVEYOR_BELT_UP_RIGHT:
                            addActor(group, new ConveyorBelt(Direction.RIGHT, Rotation.RIGHT, 1), tileSize, x, y);
                            break;
                        case CONVEYOR_BELT_RIGHT_RIGHT:
                            addActor(group, new ConveyorBelt(Direction.DOWN, Rotation.RIGHT, 1), tileSize, x, y);
                            break;
                        case CONVEYOR_BELT_DOWN_RIGHT:
                            addActor(group, new ConveyorBelt(Direction.LEFT, Rotation.RIGHT, 1), tileSize, x, y);
                            break;
                        case CONVEYOR_BELT_LEFT_RIGHT:
                            addActor(group, new ConveyorBelt(Direction.UP, Rotation.RIGHT, 1), tileSize, x, y);
                            break;
                        default:
                            addTile(group, tileSize, x, y);
                    }

                }
            }
        }

        private void addActor(Group group, Actor actor, int tileSize, int x, int y) {
            actor.setX(x * tileSize);
            actor.setY(y * tileSize);
            actor.setWidth(tileSize);
            actor.setHeight(tileSize);
            group.addActor(actor);
        }

        private void addTile(Group group, int tileSize, int x, int y) {
            Tile tile = new Tile();
            tile.setX(x * tileSize);
            tile.setY(y * tileSize);
            tile.setWidth(tileSize);
            tile.setHeight(tileSize);
            group.addActor(tile);
        }

        private void addSpawn(Group group, int tileSize, int x, int y) {
            SpawnPoint spawnPoint = new SpawnPoint(tileSize, x, y);
            spawnPoint.setPosition((x + .5f) * tileSize, (y + .5f) * tileSize, Align.center);
            group.addActor(spawnPoint);
            spawnPoints.add(spawnPoint);
        }

        public int get(int x, int y) {
            int invertedY = height - y - 1;

            if (isWithinBounds(x, invertedY)) {
                return tileArray[invertedY][x];
            }
            return OUT_OF_BOUNDS;
        }

        public TileType getTile(int x, int y) {
            int id = get(x, y);
            return TileType.fromId(id);
        }

        public boolean isWithinBounds(int x, int y) {
            return x >= 0 && x < width && y >= 0 && y < height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public List<SpawnPoint> getSpawnPoints() {
            return spawnPoints;
        }
    }

}
