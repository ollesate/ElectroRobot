package olof.sjoholm.GameWorld;

import olof.sjoholm.GameWorld.Utils.Constants;
import olof.sjoholm.GameWorld.Utils.Logger;

/**
 * Created by sjoholm on 06/11/16.
 */

public class Level {
    private final int mapWidth;
    private final int mapHeight;
    private int[][] map;

    public Level(int[][] map) {
        mapWidth = map[0].length;
        mapHeight = map.length;
        this.map = map;
    }

    public int getWidth() {
        return mapWidth;
    }

    public int getHeight() {
        return mapHeight;
    }

    public int get(int x, int y) {
        if (isOutsideLevel(x, y)) {
            return -1;
        }
        return map[y][x];
    }

    public boolean isOutsideLevel(int x, int y) {
        return x < 0 ||
                x >= getWidth() ||
                y < 0 ||
                y >= getHeight();
    }

    public static void setUpWorldSize(Level level) {
        int max = Math.max(level.getWidth(), level.getHeight());
        Constants.STEP_SIZE = Constants.WORLD_WIDTH / ((float) max);
    }
}
