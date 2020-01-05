package com.checkers.analytics;

import com.checkers.entity.Cell;
import com.checkers.entity.Field;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

public class FieldStateAnalyzerTest {

    private Field field;
    private FieldStateAnalyzer analyzer;

    @BeforeMethod
    public void init() {
        this.field = spy(new Field());
        this.analyzer = new FieldStateAnalyzer(field);
    }

    @Test
    public void updateDataForAnalysisTest() {
        doReturn(true).when(field).isWhiteFigure(any(Cell.class));
        analyzer.updateDataForAnalysis();
        assertEquals(analyzer.countWhites(), (8 * 8));

        doReturn(false).when(field).isWhiteFigure(any(Cell.class));
        doReturn(true).when(field).isWhiteQueen(any(Cell.class));
        analyzer.updateDataForAnalysis();
        assertEquals(analyzer.countWhites(), (8 * 8));

        doReturn(false).when(field).isWhiteFigure(any(Cell.class));
        doReturn(false).when(field).isWhiteQueen(any(Cell.class));
        doReturn(true).when(field).isBlackFigure(any(Cell.class));
        analyzer.updateDataForAnalysis();
        assertEquals(analyzer.countBlacks(), (8 * 8));

        doReturn(false).when(field).isWhiteFigure(any(Cell.class));
        doReturn(false).when(field).isWhiteQueen(any(Cell.class));
        doReturn(false).when(field).isBlackFigure(any(Cell.class));
        doReturn(true).when(field).isBlackQueen(any(Cell.class));
        analyzer.updateDataForAnalysis();
        assertEquals(analyzer.countBlacks(), (8 * 8));
    }

    private static final byte[][] UPDATE_WHITES_POSSIBLE_ATTACKS_TEST_FIELD = new byte[][] {
           //1  2  3  4  5  6  7  8
            {1, 0, 1, 0, 1, 0, 1, 0}, //A
            {0, 1, 0, 1, 0, 1, 0, 1}, //B
            {1, 0, 4, 0, 4, 0, 5, 0}, //C
            {0, 2, 0, 2, 0, 1, 0, 1}, //D
            {1, 0, 2, 0, 3, 0, 3, 0}, //E
            {0, 1, 0, 5, 0, 5, 0, 1}, //F
            {1, 0, 1, 0, 1, 0, 2, 0}, //G
            {0, 1, 0, 1, 0, 1, 0, 1}};//H
           //1  2  3  4  5  6  7  8

    @Test
    public void updateWhitesPossibleAttacksTest() throws Exception {
        field.magicUpdate(UPDATE_WHITES_POSSIBLE_ATTACKS_TEST_FIELD);
        field.forEachCell(cell -> {
            if (field.isWhiteFigure(cell)) {
                analyzer.getWhiteFigures().add(cell);
            } else if (field.isWhiteQueen(cell)) {
                analyzer.getWhiteQueens().add(cell);
            } else if (field.isBlackFigure(cell)) {
                analyzer.getBlackFigures().add(cell);
            } else if (field.isBlackQueen(cell)) {
                analyzer.getBlackQueens().add(cell);
            }
        });
        analyzer.updateWhitesPossibleAttacks();
        assertEquals(analyzer.getWhitesPossibleAttacks(),
                new HashMap<Cell, List<Cell>>() {
                    {
                        put(Cell.fromString("E, 2"), Collections.singletonList(Cell.fromString("F, 3")));
                        put(Cell.fromString("D, 3"), Collections.singletonList(Cell.fromString("C, 4")));
                        put(Cell.fromString("E, 4"), Arrays.asList(Cell.fromString("F, 3"), Cell.fromString("F, 5")));
                        put(Cell.fromString("D, 5"), Arrays.asList(Cell.fromString("F, 7"), Cell.fromString("C, 4")));
                        put(Cell.fromString("D, 7"), Arrays.asList(Cell.fromString("F, 5"), Cell.fromString("C, 6")));
                    }
                });
    }

    private static final byte[][] UPDATE_BLACKS_POSSIBLE_ATTACKS_TEST_FIELD = new byte[][] {
           //1  2  3  4  5  6  7  8
            {4, 0, 1, 0, 4, 0, 1, 0}, //H
            {0, 2, 0, 2, 0, 3, 0, 1}, //G
            {4, 0, 1, 0, 4, 0, 1, 0}, //F
            {0, 2, 0, 3, 0, 2, 0, 1}, //E
            {4, 0, 1, 0, 5, 0, 1, 0}, //D
            {0, 3, 0, 1, 0, 2, 0, 5}, //C
            {4, 0, 1, 0, 1, 0, 1, 0}, //B
            {0, 2, 0, 1, 0, 1, 0, 5}};//A
           //1  2  3  4  5  6  7  8

    @Test
    public void updateBlacksPossibleAttacksTest() throws Exception {
        field.magicUpdate(UPDATE_BLACKS_POSSIBLE_ATTACKS_TEST_FIELD);
        field.forEachCell(cell -> {
            if (field.isWhiteFigure(cell)) {
                analyzer.getWhiteFigures().add(cell);
            } else if (field.isWhiteQueen(cell)) {
                analyzer.getWhiteQueens().add(cell);
            } else if (field.isBlackFigure(cell)) {
                analyzer.getBlackFigures().add(cell);
            } else if (field.isBlackQueen(cell)) {
                analyzer.getBlackQueens().add(cell);
            } });
        analyzer.updateBlacksPossibleAttacks();
        assertEquals(analyzer.getBlacksPossibleAttacks(),
                new HashMap<Cell, List<Cell>>() {
                    {
                        put(Cell.fromString("H, 1"), Arrays.asList(
                                Cell.fromString("G, 2")));
                        put(Cell.fromString("D, 5"), Arrays.asList(
                                Cell.fromString("E, 6"), Cell.fromString("C, 6"), Cell.fromString("E, 4")));
                        put(Cell.fromString("F, 5"), Arrays.asList(
                                Cell.fromString("G, 4"), Cell.fromString("E, 6"),
                                Cell.fromString("G, 6"), Cell.fromString("E, 4")));
                        put(Cell.fromString("H, 5"), Arrays.asList(
                                Cell.fromString("G, 4"), Cell.fromString("G, 6")));
                        put(Cell.fromString("B, 1"), Arrays.asList(
                                Cell.fromString("C, 2")));
                        put(Cell.fromString("D, 1"), Arrays.asList(
                                Cell.fromString("E, 2"), Cell.fromString("C, 2")));
                        put(Cell.fromString("F, 1"), Arrays.asList(
                                Cell.fromString("G, 2"), Cell.fromString("E, 2")));
                    }
                });
    }
}