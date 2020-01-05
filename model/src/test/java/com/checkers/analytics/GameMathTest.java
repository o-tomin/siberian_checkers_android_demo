package com.checkers.analytics;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.*;

public class GameMathTest {

    private static final int[] FIRST_CELL = {4, 4};
    private static final int[] SECOND_CELL = {3, 5};
    private static final int[] THIRD_CELL = {2, 6};
    private static final int[] SOME_CELL = {2, 1};

    private static final int[] ONE_ONE_CELL = {1, 1};
    private static final int[] FOUR_FOUR_CALL = {4, 4};
    private static final int[] FIVE_FIVE_CELL = {5, 5};
    private static final int[] SIX_SIX_CELL = {6, 6};
    private static final int[] SEVEN_SEVEN_CELL = {7, 7};
    private static final int[] SIX_FOUR_CELL = {6, 4};
    private static final int[] SEVEN_ONE_CELL = {7, 1};
    private static final int[] TWO_EIGHT_CELL = {2, 8};
    private static final int[] FOUR_SIX_CELL = {4, 6};
    private static final int[] EIGHT_TWO_CELL = {8, 2};
    private static final int[] ONE_SEVEN_CELL = {1, 7};

    @Test
    public void calculateNextCellIncrementalOrderTest() {
        int[] nextCell = GameMath.calculateNextCellBasedOnTwoCells(FIRST_CELL, SECOND_CELL);
        assertEquals(nextCell, THIRD_CELL);
    }

    @Test
    public void calculateNextCellDecrementalOrderTest() {
        int[] nextCell = GameMath.calculateNextCellBasedOnTwoCells(THIRD_CELL, SECOND_CELL);
        assertEquals(nextCell, FIRST_CELL);
    }

    @Test
    public void calculateInBetweenCellsTest() {
        int[][] cells = GameMath.calculateInBetweenCells(ONE_ONE_CELL, SEVEN_SEVEN_CELL);
        assertTrue(Arrays.deepEquals(cells, array("{2,2}{3,3}{4,4}{5,5}{6,6}")));

        cells = GameMath.calculateInBetweenCells(SEVEN_ONE_CELL, ONE_SEVEN_CELL);
        assertTrue(Arrays.deepEquals(cells, array("{6,2}{5,3}{4,4}{3,5}{2,6}")));

        cells = GameMath.calculateInBetweenCells(ONE_SEVEN_CELL, SEVEN_ONE_CELL);
        assertTrue(Arrays.deepEquals(cells, array("{2,6}{3,5}{4,4}{5,3}{6,2}")));
    }

    @Test
    public void isOnOneDiagonalTrueTest() {
        assertTrue(GameMath.isOnOneDiagonal(FIRST_CELL, THIRD_CELL));
    }

    @Test
    public void isOnOneDiagonalFalseTest() {
        assertFalse(GameMath.isOnOneDiagonal(SOME_CELL, THIRD_CELL));
    }

    @Test
    public void isSequentialTest() {
        Assert.assertTrue(GameMath.isSequential(ONE_ONE_CELL,       FIVE_FIVE_CELL,     SIX_SIX_CELL));
        Assert.assertTrue(GameMath.isSequential(FOUR_FOUR_CALL,     FIVE_FIVE_CELL,     SIX_SIX_CELL));
        Assert.assertTrue(GameMath.isSequential(FIVE_FIVE_CELL,     SIX_SIX_CELL,       SEVEN_SEVEN_CELL));

        Assert.assertTrue(GameMath.isSequential(SIX_SIX_CELL,       FIVE_FIVE_CELL,     ONE_ONE_CELL));
        Assert.assertTrue(GameMath.isSequential(SIX_SIX_CELL,       FIVE_FIVE_CELL,     FOUR_FOUR_CALL));
        Assert.assertTrue(GameMath.isSequential(SEVEN_SEVEN_CELL,   SIX_SIX_CELL,       FIVE_FIVE_CELL));

        Assert.assertTrue(GameMath.isSequential(SIX_FOUR_CELL,      FIVE_FIVE_CELL,     FOUR_SIX_CELL));
        Assert.assertTrue(GameMath.isSequential(EIGHT_TWO_CELL,     SIX_FOUR_CELL,      FOUR_SIX_CELL));
        Assert.assertTrue(GameMath.isSequential(SIX_FOUR_CELL,      FOUR_SIX_CELL,      TWO_EIGHT_CELL));

        Assert.assertTrue(GameMath.isSequential(FOUR_SIX_CELL,      FIVE_FIVE_CELL,     SIX_FOUR_CELL));
        Assert.assertTrue(GameMath.isSequential(FOUR_SIX_CELL,      SIX_FOUR_CELL,      EIGHT_TWO_CELL));
        Assert.assertTrue(GameMath.isSequential(TWO_EIGHT_CELL,     FOUR_SIX_CELL,      SIX_FOUR_CELL));

        Assert.assertFalse(GameMath.isSequential(ONE_ONE_CELL,      SIX_SIX_CELL,       FIVE_FIVE_CELL));
        Assert.assertFalse(GameMath.isSequential(SIX_SIX_CELL,      ONE_ONE_CELL,       FIVE_FIVE_CELL));
        Assert.assertFalse(GameMath.isSequential(SIX_FOUR_CELL,     FOUR_SIX_CELL,      FIVE_FIVE_CELL));
        Assert.assertFalse(GameMath.isSequential(FOUR_SIX_CELL,     SIX_FOUR_CELL,      FIVE_FIVE_CELL));

        Assert.assertFalse(GameMath.isSequential(ONE_SEVEN_CELL,    SIX_FOUR_CELL,      SIX_SIX_CELL));
    }

    private static int[][] array(String arrayString) {
        String[] arrays = arrayString.split("\\{");
        int[][] arraysInt = new int[arrays.length - 1][];
        for (int i = 1; i < arrays.length; i++) {
            String[] splitted = arrays[i].split(",");
            arraysInt[i - 1] = new int[] {Integer.parseInt(splitted[0].trim()),
                    Integer.parseInt(splitted[1].substring(0, 1))};
        }
        return arraysInt;
    }
}
