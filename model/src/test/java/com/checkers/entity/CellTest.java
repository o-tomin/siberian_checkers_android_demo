package com.checkers.entity;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CellTest {
    private static final String INPUT_CELL = "D, 7";
    private static final String BAD_INPUT = "E, ";
    private static final int LETTER_INDEX = 4;
    private static final int NUMBER_INDEX = 6;
    private static final int[] INDEXES = {LETTER_INDEX, NUMBER_INDEX};
    private static final Cell CELL_ONE = new Cell(LETTER_INDEX, NUMBER_INDEX);
    private static final Cell CELL_TWO = new Cell(LETTER_INDEX - 1, NUMBER_INDEX + 1);
    private static final Cell SOME_CELL = new Cell(1, 7);

    @Test
    public void toIndexedCellTest() {
        Cell cell = new Cell(LETTER_INDEX, NUMBER_INDEX);
        assertEquals(cell.toIndexedCell(), INDEXES);
    }

    @Test
    public void isCloseToTrueTest() {
        assertTrue(CELL_ONE.isCloseTo(CELL_TWO));
    }

    @Test
    public void isCloseToFalseTest() {
        assertFalse(CELL_ONE.isCloseTo(SOME_CELL));
    }

    @Test
    public void fromGoodStringTest() throws Exception {
        assertEquals(Cell.fromString(INPUT_CELL), new Cell(INDEXES));
    }

    @Test(expectedExceptions = Exception.class)
    public void fromBadStringTest() throws Exception {
        assertEquals(Cell.fromString(BAD_INPUT), new Cell(INDEXES));
    }
}
