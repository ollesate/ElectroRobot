package olof.sjoholm.GameWorld;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.GameWorld.Actors.Tile;

public class Maps {

    public static Map Level1() {
        return new Map(
                new int[][]{
                        {0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 1, 0, 0, 0, 0, 1, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 1, 0, 0, 0, 0, 1, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0},
                }
        );
    }

    public static class Map {
        public static final int OUT_OF_BOUNDS = -1;
        public static final int FLOOR = 0;
        public static final int SPAWN = 1;

        private final List<SpawnPoint> spawnPoints = new ArrayList<SpawnPoint>();
        private final int[][] tileArray;
        private final int width;
        private final int height;

        private Map(int [][] tileArray) {
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
                            spawnPoints.add(new SpawnPoint(x, y));
                            break;
                    }

                }
            }
        }

        private void addTile(Group group, int tileSize, int x, int y) {
            Tile tile = new Tile(x, y);
            tile.setWidth(tileSize);
            tile.setHeight(tileSize);
            group.addActor(tile);
        }

        public int get(int x, int y) {
            if (isWithinBounds(x, y)) {
                return tileArray[y][x];
            }
            return OUT_OF_BOUNDS;
        }

        private boolean isWithinBounds(int x, int y) {
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

    public static class SpawnPoint {
        public final int x;
        public final int y;

        public SpawnPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
