package olof.sjoholm.GameWorld;

import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.GameWorld.Actors.Tile;

public class Levels {

    public static Level level1() {
        return new Level(
                new int[][]{
                        {0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 1, 0, 0, 0, 0, 1, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 1, 0, -1, -1, 0, 1, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0},
                }
        );
    }

    public static class Level {
        public static final int OUT_OF_BOUNDS = -2;
        public static final int FLOOR = 0;
        public static final int SPAWN = 1;

        private final List<SpawnPoint> spawnPoints = new ArrayList<SpawnPoint>();
        private final int[][] tileArray;
        private final int width;
        private final int height;

        private Level(int [][] tileArray) {
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
