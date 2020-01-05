package com.checkers.entity;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class FieldTest {
    private static final byte[][] TEST_FIELD = {
           //1  2  3  4  5  6  7  8
            {1, 0, 1, 0, 1, 0, 1, 0}, //H
            {0, 1, 0, 1, 0, 1, 0, 1}, //G
            {1, 0, 1, 0, 1, 0, 1, 0}, //F
            {0, 1, 0, 3, 0, 1, 0, 1}, //E
            {1, 0, 1, 0, 1, 0, 1, 0}, //D
            {0, 1, 0, 1, 0, 1, 0, 1}, //C
            {1, 0, 1, 0, 1, 0, 4, 0}, //B
            {0, 1, 0, 1, 0, 1, 0, 1}};//A
           //1  2  3  4  5  6  7  8
    private static final String WHITE_QUEEN_TEST_FIELD_COORDINATES = "E, 4";
    private static final String BLACK_FIGURE_TEST_FIELD_COORDINATES = "B, 7";

    private static final String WHITE_FIGURE_INITIAL_COORDINATES = "F, 3";
    private static final String BLACK_FIELD_INITIAL_COORDINATES_D4 = "E, 4";
    private static final String BLACK_FIELD_INITIAL_COORDINATES_F4 = "G, 4";
    private static final String BLACK_FIGURE_INITIAL_COORDINATES = "C, 6";
    private static final String BLACK_FIELD_INITIAL_COORDINATES_C5 = "B, 5";
    private static final String BLACK_FIELD_INITIAL_COORDINATES_E5 = "D, 5";
    private static final int[] COORDINATES = {5, 5};

    private Field field;
    private Cell whiteFigureCell;
    private Cell blackFieldCellE4;
    private Cell blackFieldCellG4;
    private Cell blackFieldCellB5;
    private Cell blackFieldCellD5;
    private Cell blackFigureCell;
    private Cell whiteFigureCellMock;

    @BeforeMethod
    public void init() throws Exception {
        this.field = spy(new Field());
        this.whiteFigureCell = Cell.fromString(WHITE_FIGURE_INITIAL_COORDINATES);
        this.blackFieldCellE4 = Cell.fromString(BLACK_FIELD_INITIAL_COORDINATES_D4);
        this.blackFieldCellG4 = Cell.fromString(BLACK_FIELD_INITIAL_COORDINATES_F4);
        this.blackFigureCell = Cell.fromString(BLACK_FIGURE_INITIAL_COORDINATES);
        this.blackFieldCellB5 = Cell.fromString(BLACK_FIELD_INITIAL_COORDINATES_C5);
        this.blackFieldCellD5 = Cell.fromString(BLACK_FIELD_INITIAL_COORDINATES_E5);
        this.whiteFigureCellMock = configureWhiteFigureMock(field, mock(Cell.class));
    }

    @Test
    public void moveFigureToEmptyBlackCellTest() {
        field.moveFigureToEmptyBlackCell(whiteFigureCell, blackFieldCellE4);
        assertTrue(field.isBlackCell(whiteFigureCell));
        assertTrue(field.isWhiteFigure(blackFieldCellE4));
    }

    @Test
    public void switchFigureToQueenTest() {
        field.switchFigureToQueen(whiteFigureCellMock);
        assertTrue(field.isWhiteQueen(whiteFigureCellMock));
    }

    @Test
    public void removeFigureTest() {
        field.removeFigure(whiteFigureCell);
        assertTrue(field.isBlackCell(whiteFigureCell));
    }

    @Test
    public void iterateFieldCellsTest() {
        int[] count = {0};
        field.forEachCell(cell -> count[0] += 1 );
        assertEquals(count[0], (8 * 8) );
    }

    @Test
    public void isBlackCellTest() {
        assertTrue(field.isBlackCell(blackFieldCellE4));
    }

    @Test
    public void isBlackFigureTest() {
        assertTrue(field.isBlackFigure(blackFigureCell));
    }

    @Test
    public void isBlackQueenTest() {
        field.switchFigureToQueen(blackFigureCell);
        assertTrue(field.isBlackQueen(blackFigureCell));
    }

    @Test
    public void isWhiteFigureTest() {
        assertTrue(field.isWhiteFigure(whiteFigureCell));
    }

    @Test
    public void isWhiteQueenTest() {
        field.switchFigureToQueen(whiteFigureCell);
        assertTrue(field.isWhiteQueen(whiteFigureCell));
    }

    @Test
    public void isWhitesDirectionTest() {
        assertTrue(field.isWhitesDirection(whiteFigureCell, blackFieldCellE4));
        assertTrue(field.isWhitesDirection(whiteFigureCell, blackFieldCellG4));
    }

    @Test
    public void isBlacksDirectionTest() {
        assertTrue(field.isBlacksDirection(blackFigureCell, blackFieldCellD5));
        assertTrue(field.isBlacksDirection(blackFigureCell, blackFieldCellB5));
    }

    @Test
    public void isWhiteFigureOnOppositeEdgeTest() {
        Cell whiteFigure = mock(Cell.class);
        doReturn(true).when(field).isWhiteFigure(whiteFigure);
        when(whiteFigure.toIndexedCell()).thenReturn(new int[] {anyInt(), 7});
        assertTrue(field.isFigureOnOppositeEdge(whiteFigure));
    }

    @Test
    public void isBlackFigureOnOppositeEdgeTest(){
        Cell blackFigure = mock(Cell.class);
        doReturn(false).when(field).isWhiteFigure(blackFigure);
        doReturn(true).when(field).isBlackFigure(blackFigure);
        when(blackFigure.toIndexedCell()).thenReturn(new int[] {anyInt(), 0});
        assertTrue(field.isFigureOnOppositeEdge(blackFigure));
    }

    @Test
    public void isCellValidTest() {
        assertTrue(field.isCellValid(whiteFigureCell));
        assertFalse(field.isCellValid(new Cell(Integer.MIN_VALUE, Integer.MAX_VALUE)));
    }

    @Test
    public void isFigureTest() {
        assertTrue(field.isFigure(blackFigureCell));
        assertFalse(field.isFigure(blackFieldCellB5));
    }

    private static final byte[][] RUN_ON_IMAGINING_FIELD_TO_VERIFY =  {
           //1  2  3  4  5  6  7  8
            {1, 0, 1, 0, 1, 0, 1, 0}, //H
            {0, 1, 0, 1, 0, 1, 0, 1}, //G
            {1, 0, 1, 0, 1, 0, 1, 0}, //F
            {0, 1, 0, 4, 0, 1, 0, 1}, //E
            {1, 0, 1, 0, 1, 0, 1, 0}, //D
            {0, 1, 0, 1, 0, 1, 0, 1}, //C
            {1, 0, 1, 0, 1, 0, 4, 0}, //B
            {0, 1, 0, 1, 0, 1, 0, 1}};//A
           //1  2  3  4  5  6  7  8

    @Test
    public void runOnImaginingFieldTest() throws Exception {
        field.magicUpdate(TEST_FIELD);
        Cell whiteQueen = Cell.fromString(WHITE_QUEEN_TEST_FIELD_COORDINATES);
        Cell blackFigure = Cell.fromString(BLACK_FIGURE_TEST_FIELD_COORDINATES);
        Object toReturn = new Object();
        Object returned = field.runOnImaginingField(whiteQueen, blackFigure,
            () -> {
                Assert.assertTrue(field.isBlackFigure(whiteQueen));
                Assert.assertTrue(field.isBlackFigure(blackFigure));
                Assert.assertTrue(Arrays.deepEquals(field.cloneField(), RUN_ON_IMAGINING_FIELD_TO_VERIFY));
                return toReturn;
            });
        Assert.assertEquals(returned, toReturn);
        Assert.assertTrue(Arrays.deepEquals(field.cloneField(), TEST_FIELD));
    }

    private static Cell configureWhiteFigureMock(Field fieldSpy, Cell cellMock) {
        doReturn(true).when(fieldSpy).isCellValid(cellMock);
        doReturn(true).when(fieldSpy).isFigure(cellMock);
        doReturn(true).when(fieldSpy).isWhiteFigure(cellMock);
        when(cellMock.toIndexedCell()).thenReturn(COORDINATES);
        return cellMock;
    }
}
