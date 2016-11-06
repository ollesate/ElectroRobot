package olof.sjoholm.GameWorld.GameManagers;

import org.junit.Test;

import olof.sjoholm.GameWorld.Level;
import olof.sjoholm.GameWorld.Utils.Direction;

import static org.junit.Assert.*;

/**
 * Created by sjoholm on 01/10/16.
 */
public class GameScreenTest {
    private int[][] testLevel = new int[][]{
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
    };
    private Level map = new Level(testLevel);
    private olof.sjoholm.GameWorld.Actors.GameBoard gameBoard = new olof.sjoholm.GameWorld.Actors.GameBoard(null);

    @Test
    public void upFromBottomLeft() {
        gameBoard.loadMap(map);
        int actual = gameBoard.getPossibleSteps(Direction.UP, 0, 0);
        assertEquals(2, actual);
    }

    @Test
    public void leftFromBottomLeft() {
        gameBoard.loadMap(map);
        int actual = gameBoard.getPossibleSteps(Direction.LEFT, 0, 0);
        assertEquals(0, actual);
    }

    @Test
    public void rightFromBottomLeft() {
        gameBoard.loadMap(map);
        int actual = gameBoard.getPossibleSteps(Direction.RIGHT, 0, 0);
        assertEquals(4, actual);
    }

    @Test
    public void downFromBottomLeft() {
        gameBoard.loadMap(map);
        int actual = gameBoard.getPossibleSteps(Direction.DOWN, 0, 0);
        assertEquals(0, actual);
    }

    @Test
    public void downFromBottomCenter() {
        gameBoard.loadMap(map);
        int actual = gameBoard.getPossibleSteps(Direction.DOWN, 2, 0);
        assertEquals(0, actual);
    }

    @Test
    public void upFromBottomCenter() {
        gameBoard.loadMap(map);
        int actual = gameBoard.getPossibleSteps(Direction.UP, 2, 0);
        assertEquals(2, actual);
    }

    @Test
    public void leftFromBottomCenter() {
        gameBoard.loadMap(map);
        int actual = gameBoard.getPossibleSteps(Direction.LEFT, 2, 0);
        assertEquals(2, actual);
    }

    @Test
    public void rightFromBottomCenter() {
        gameBoard.loadMap(map);
        int actual = gameBoard.getPossibleSteps(Direction.RIGHT, 2, 0);
        assertEquals(2, actual);
    }

}