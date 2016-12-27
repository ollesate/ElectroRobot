package olof.sjoholm.GameWorld.GameManagers;

import com.badlogic.gdx.math.Vector2;

import org.junit.Test;

import olof.sjoholm.GameWorld.Level;
import olof.sjoholm.GameWorld.Utils.Direction;

import static org.junit.Assert.*;

public class GameScreenTest {
    private int[][] testLevel = new int[][]{
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
    };
    private Level map = new Level(testLevel);
    private olof.sjoholm.GameWorld.Actors.GameBoard gameBoard = new olof.sjoholm.GameWorld.Actors.GameBoard();
    private static final Vector2 UP = new Vector2(Direction.UP.x, Direction.UP.y);
    private static final Vector2 DOWN = new Vector2(Direction.DOWN.x, Direction.DOWN.y);
    private static final Vector2 LEFT = new Vector2(Direction.LEFT.x, Direction.LEFT.y);
    private static final Vector2 RIGHT = new Vector2(Direction.RIGHT.x, Direction.RIGHT.y);

    @Test
    public void upFromBottomLeft() {
        gameBoard.loadMap(map);
        int actual = gameBoard.getPossibleSteps(UP, 0, 0);
        assertEquals(2, actual);
    }

    @Test
    public void leftFromBottomLeft() {
        gameBoard.loadMap(map);
        int actual = gameBoard.getPossibleSteps(LEFT, 0, 0);
        assertEquals(0, actual);
    }

    @Test
    public void rightFromBottomLeft() {
        gameBoard.loadMap(map);
        int actual = gameBoard.getPossibleSteps(RIGHT, 0, 0);
        assertEquals(4, actual);
    }

    @Test
    public void downFromBottomLeft() {
        gameBoard.loadMap(map);
        int actual = gameBoard.getPossibleSteps(DOWN, 0, 0);
        assertEquals(0, actual);
    }

    @Test
    public void downFromBottomCenter() {
        gameBoard.loadMap(map);
        int actual = gameBoard.getPossibleSteps(DOWN, 2, 0);
        assertEquals(0, actual);
    }

    @Test
    public void upFromBottomCenter() {
        gameBoard.loadMap(map);
        int actual = gameBoard.getPossibleSteps(UP, 2, 0);
        assertEquals(2, actual);
    }

    @Test
    public void leftFromBottomCenter() {
        gameBoard.loadMap(map);
        int actual = gameBoard.getPossibleSteps(LEFT, 2, 0);
        assertEquals(2, actual);
    }

    @Test
    public void rightFromBottomCenter() {
        gameBoard.loadMap(map);
        int actual = gameBoard.getPossibleSteps(RIGHT, 2, 0);
        assertEquals(2, actual);
    }

}