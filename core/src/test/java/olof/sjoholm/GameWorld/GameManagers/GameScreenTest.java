package olof.sjoholm.GameWorld.GameManagers;

import org.junit.Test;

import olof.sjoholm.GameWorld.Utils.Direction;

import static org.junit.Assert.*;

/**
 * Created by sjoholm on 01/10/16.
 */
public class GameScreenTest {
    private int[][] map = new int[][]{
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
    };
    private olof.sjoholm.GameWorld.Actors.GameBoard gameBoard = new olof.sjoholm.GameWorld.Actors.GameBoard(null);

    @Test
    public void upFromBottomLeft() {
        gameBoard.setMap(map);
        int actual = gameBoard.getPossibleSteps(Direction.UP, 0, 0);
        assertEquals(2, actual);
    }

    @Test
    public void leftFromBottomLeft() {
        gameBoard.setMap(map);
        int actual = gameBoard.getPossibleSteps(Direction.LEFT, 0, 0);
        assertEquals(0, actual);
    }

    @Test
    public void rightFromBottomLeft() {
        gameBoard.setMap(map);
        int actual = gameBoard.getPossibleSteps(Direction.RIGHT, 0, 0);
        assertEquals(4, actual);
    }

    @Test
    public void downFromBottomLeft() {
        gameBoard.setMap(map);
        int actual = gameBoard.getPossibleSteps(Direction.DOWN, 0, 0);
        assertEquals(0, actual);
    }

    @Test
    public void downFromBottomCenter() {
        gameBoard.setMap(map);
        int actual = gameBoard.getPossibleSteps(Direction.DOWN, 2, 0);
        assertEquals(0, actual);
    }

    @Test
    public void upFromBottomCenter() {
        gameBoard.setMap(map);
        int actual = gameBoard.getPossibleSteps(Direction.UP, 2, 0);
        assertEquals(2, actual);
    }

    @Test
    public void leftFromBottomCenter() {
        gameBoard.setMap(map);
        int actual = gameBoard.getPossibleSteps(Direction.LEFT, 2, 0);
        assertEquals(2, actual);
    }

    @Test
    public void rightFromBottomCenter() {
        gameBoard.setMap(map);
        int actual = gameBoard.getPossibleSteps(Direction.RIGHT, 2, 0);
        assertEquals(2, actual);
    }

}