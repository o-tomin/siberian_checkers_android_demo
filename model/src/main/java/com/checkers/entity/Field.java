package com.checkers.entity;

import com.checkers.analytics.GameMath;
import com.checkers.analytics.FieldStateAnalyzer;
import com.checkers.util.ArraysUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Field {

    private static final char[] LETTERS = {'H', 'G', 'F', 'E', 'D', 'C', 'B', 'A'};
    private static final String NUMBERS = "  1 2 3 4 5 6 7 8";

    public static final int[] WHITE_FIGURE_LEFT_VECTOR = {-1, 1};
    public static final int[] WHITE_FIGURE_RIGHT_VECTOR = {1, 1};
    public static final int[] BLACK_FIGURE_LEFT_VECTOR = {1, -1};
    public static final int[] BLACK_FIGURE_RIGHT_VECTOR = {-1, -1};
    public static final byte WHITE_CELL_CODE = 0;
    public static final byte BLACK_CELL_CODE = 1;
    public static final byte WHITE_FIGURE_CODE = 2;
    public static final byte WHITE_QUEEN_CODE = 3;
    public static final byte BLACK_FIGURE_CODE = 4;
    public static final byte BLACK_QUEEN_CODE = 5;

    private static final char WHITE_CELL_CHAR = '-';
    private static final char BLACK_CELL_CHAR = '#';
    private static final char WHITE_FIGURE_CHAR = '0';
    private static final char WHITE_QUEEN_CHAR = 'M';
    private static final char BLACK_FIGURE_CHAR = 'X';
    private static final char BLACK_QUEEN_CHAR = 'W';

    private final byte[][] FIELD = {
           //1  2  3  4  5  6  7  8
            {2, 0, 2, 0, 1, 0, 4, 0}, //H
            {0, 2, 0, 1, 0, 4, 0, 4}, //G
            {2, 0, 2, 0, 1, 0, 4, 0}, //F
            {0, 2, 0, 1, 0, 4, 0, 4}, //E
            {2, 0, 2, 0, 1, 0, 4, 0}, //D
            {0, 2, 0, 1, 0, 4, 0, 4}, //C
            {2, 0, 2, 0, 1, 0, 4, 0}, //B
            {0, 2, 0, 1, 0, 4, 0, 4}};//A
           //1  2  3  4  5  6  7  8

    private FieldStateAnalyzer stateAnalyzer;

    public Field() {
        this.stateAnalyzer = new FieldStateAnalyzer(this);
        this.stateAnalyzer.updateDataForAnalysis();
    }

    // ----------------------------------- public methods -----------------------------------

    // - operations with figures
    public void moveFigureToEmptyBlackCell(@NotNull Cell from, @NotNull Cell to) throws RuntimeException {
        if (isCellValid(from) && isCellValid(to) && isFigure(from) && isBlackCell(to)) {
            swap(from.toIndexedCell(), to.toIndexedCell());
        } else {
            throw new RuntimeException(String.format("Failed to move figure from [%s] to [%s]", from, to));
        }
        stateAnalyzer.updateDataForAnalysis();
    }

    public void switchFigureToQueen(@NotNull Cell cell) throws RuntimeException {
        if (isCellValid(cell) && isFigure(cell)) {
            if (isWhiteFigure(cell)) {
                insertCode(cell.toIndexedCell(), WHITE_QUEEN_CODE);
            } else if (isBlackFigure(cell)) {
                insertCode(cell.toIndexedCell(), BLACK_QUEEN_CODE);
            }
        } else {
            throw new RuntimeException(String.format("Failed to switch figure [%s] to queen", cell));
        }
        stateAnalyzer.updateDataForAnalysis();
    }

    public void removeFigure(@NotNull Cell cell) throws RuntimeException {
        if (isCellValid(cell) && isFigure(cell)) {
            insertCode(cell.toIndexedCell(), BLACK_CELL_CODE);
        } else {
            throw new RuntimeException(String.format("Failed to remove figure from cell [%s]", cell));
        }
        stateAnalyzer.updateDataForAnalysis();
    }

    public Optional<Cell> calculateNextFigureValidCell(Cell cell, int[] vector) {
        int[] nextCellIndexes = GameMath.calculateNextCellBasedOnOrtVector(cell.toIndexedCell(), vector);
        return Optional.of(new Cell(nextCellIndexes)).filter(this::isCellValid);
    }

    public Cell calculateNextCell(Cell cell, Cell next) {
        return new Cell(GameMath.calculateNextCellBasedOnTwoCells(cell.toIndexedCell(), next.toIndexedCell()));
    }

    public List<Cell> calculateNextCells(Cell killer, Cell victim) {
        List<Cell> emptyCells = new ArrayList<>();
        Cell previous = killer;
        Cell next = victim;
        do {
            Cell tmp = next;
            next = calculateNextCell(previous, next);
            previous = tmp;
            if (isBlackCell(next)) {
                emptyCells.add(next);
            }
        } while (isBlackCell(next));
        return emptyCells;
    }

    public List<Cell> calculateInBetweenCells(Cell left, Cell right) {
        return Stream.of(
                GameMath.calculateInBetweenCells(left.toIndexedCell(), right.toIndexedCell()))
                    .flatMap(Stream::of)
                    .map(Cell::new)
                    .collect(Collectors.toList());
    }

    public byte[][] cloneField() {
        return FIELD.clone();
    }

    // WARNING: Do not use this method!!!
    // This method does not guarantee safe field update!!!
    // May corrupt field and game!!!
    // For now this methods exist only to support unit tests.
    // Must be refactored good before using in production!!!
    @Deprecated
    public void magicUpdate(byte[][] newField) throws Exception {
        byte[][] newFieldCopy = new byte[newField.length][newField[0].length];
        for (int i = 0; i < newField.length; i++) {
            for (int j = 0; j < newField[i].length; j++) {
                newFieldCopy[i][j] = newField[i][j];
            }
        }
        java.lang.reflect.Field field = Field.class.getDeclaredField("FIELD");
        field.setAccessible(true);
        field.set(this, newFieldCopy);
    }

    public void forEachCell(Consumer<Cell> cellConsumer) {
        for (int line = 0; line < FIELD.length; line++) {
            for (int cell = 0; cell < FIELD[0].length; cell++) {
                cellConsumer.accept(new Cell(line, cell));
            }
        }
    }

    public <T> T runOnImaginingField(Cell that, Cell is, Supplier<T> command) {
        if (isCellValid(that) && isCellValid(is)) {
            byte[][] before = ArraysUtil.copy(FIELD);
            insertCode(that.toIndexedCell(), toCode(is.toIndexedCell()));
            T result = command.get();
            ArraysUtil.reset(before, FIELD);
            return result;
        } else {
            throw new RuntimeException(String.format("Failed to imaging that %s is %s.", that, is));
        }
    }

    // - public boolean methods
    public boolean isWhiteCell(@NotNull Cell cell) {
        return isCellValid(cell) && (toCode(cell.toIndexedCell()) == WHITE_CELL_CODE);
    }
    public boolean isBlackCell(@NotNull Cell cell) {
        return isCellValid(cell) && (toCode(cell.toIndexedCell()) == BLACK_CELL_CODE);
    }

    public boolean isBlackFigure(@NotNull Cell cell) {
        return isCellValid(cell) && (toCode(cell.toIndexedCell()) == BLACK_FIGURE_CODE);
    }

    public boolean isBlackQueen(@NotNull Cell cell) {
        return isCellValid(cell) && (toCode(cell.toIndexedCell()) == BLACK_QUEEN_CODE);
    }

    public boolean isWhiteFigure(@NotNull Cell cell) {
        return isCellValid(cell) && (toCode(cell.toIndexedCell()) == WHITE_FIGURE_CODE);
    }

    public boolean isWhiteQueen(@NotNull Cell cell) {
        return isCellValid(cell) && (toCode(cell.toIndexedCell()) == WHITE_QUEEN_CODE);
    }

    public boolean isWhitesDirection(@NotNull Cell from, @NotNull Cell to) {
        if (isCellValid(from) && isCellValid(to)) {
            return from.compareTo(to) == Cell.NEXT_LEFT || from.compareTo(to) == Cell.NEXT_RIGHT;
        }
        return false;
    }

    public boolean isBlacksDirection(@NotNull Cell from, @NotNull Cell to) {
        if (isCellValid(from) && isCellValid(to)) {
            return from.compareTo(to) == Cell.PREVIOUS_LEFT || from.compareTo(to) == Cell.PREVIOUS_RIGHT;
        }
        return false;
    }

    public boolean isFigureOnOppositeEdge(Cell cell) {
        if (isWhiteFigure(cell)) {
            return cell.toIndexedCell()[1] == (FIELD[0].length - 1);
        } else if (isBlackFigure(cell)) {
            return cell.toIndexedCell()[1] == 0;
        }
        return false;
    }

    public boolean isCellValid(@NotNull Cell cell) {
        int[] indexes = cell.toIndexedCell();
        return indexes.length == 2 &&
                ( indexes[0] >= 0 && indexes[0] < FIELD.length ) &&
                ( indexes[1] >= 0 && indexes[1] < FIELD[0].length );
    }

    public boolean isFigure(@NotNull Cell cell) {
        switch (toCode(cell.toIndexedCell())) {
            case WHITE_FIGURE_CODE:
            case WHITE_QUEEN_CODE:
            case BLACK_FIGURE_CODE:
            case BLACK_QUEEN_CODE:
                return true;
        }
        return false;
    }

    public boolean isOnOneDiagonal(@NotNull Cell first, @NotNull Cell second) {
        return GameMath.isOnOneDiagonal(first.toIndexedCell(), second.toIndexedCell());
    }

    public boolean isSequential(@NotNull Cell first, @NotNull Cell medium, @NotNull Cell last) {
        if (isCellValid(first) && isCellValid(medium) && isCellValid(last)) {
            return GameMath.isSequential(first.toIndexedCell(), medium.toIndexedCell(), last.toIndexedCell());
        }
        return false;
    }

    public boolean isCloseTo(@NotNull Cell first, @NotNull Cell second) {
        return first.isCloseTo(second);
    }

    //getters
    public FieldStateAnalyzer getStateAnalyzer() {
        return stateAnalyzer;
    }

    // ----------------------------------- private methods -----------------------------------
    private void swap(int[] from, int[] to) throws ArrayIndexOutOfBoundsException {
        byte figureCode = toCode(from);
        insertCode(from, toCode(to));
        insertCode(to, figureCode);
    }

    private char mapFigureCodeToConsoleChar(int line, int cell) throws RuntimeException {
        switch (FIELD[line][cell]) {
            case WHITE_CELL_CODE:
                return WHITE_CELL_CHAR;
            case BLACK_CELL_CODE:
                return BLACK_CELL_CHAR;
            case WHITE_FIGURE_CODE:
                return WHITE_FIGURE_CHAR;
            case BLACK_FIGURE_CODE:
                return BLACK_FIGURE_CHAR;
            case WHITE_QUEEN_CODE:
                return WHITE_QUEEN_CHAR;
            case BLACK_QUEEN_CODE:
                return BLACK_QUEEN_CHAR;
        }

        throw new RuntimeException("Bad char to map: " + FIELD[line][cell]);
    }

    private char mapFigureCodeToConsoleChar(byte[][] FIELD, int line, int cell) throws RuntimeException {
        switch (FIELD[line][cell]) {
            case WHITE_CELL_CODE:
                return WHITE_CELL_CHAR;
            case BLACK_CELL_CODE:
                return BLACK_CELL_CHAR;
            case WHITE_FIGURE_CODE:
                return WHITE_FIGURE_CHAR;
            case BLACK_FIGURE_CODE:
                return BLACK_FIGURE_CHAR;
            case WHITE_QUEEN_CODE:
                return WHITE_QUEEN_CHAR;
            case BLACK_QUEEN_CODE:
                return BLACK_QUEEN_CHAR;
        }

        throw new RuntimeException("Bad char to map: " + FIELD[line][cell]);
    }

    private byte toCode(@NotNull int[] cell) {
            return FIELD[cell[0]][cell[1]];
    }

    private void insertCode(int[] cell, byte code) {
        FIELD[cell[0]][cell[1]] = code;
    }

    private Optional<Cell> returnIfEmpty(Cell cell) {
        if (isBlackCell(cell)) {
            return Optional.of(cell);
        }
        return Optional.empty();
    }

    // ----------------------------------- common overrides -----------------------------------
    @Override
    public String toString() {
        StringBuilder fieldString = new StringBuilder();
        fieldString.append(NUMBERS);
        for (int line = 0; line < FIELD.length; line++) {
            for (int cell = 0; cell < FIELD[0].length; cell++) {
                if (cell == 0) {
                    fieldString.append("\n");
                    fieldString.append(LETTERS[line]).append(' ');
                }

                fieldString.append(mapFigureCodeToConsoleChar(line, cell)).append(' ');

                if (cell == FIELD[0].length - 1) {
                    fieldString.append(LETTERS[line]);
                }
            }
        }
        fieldString.append("\n");
        fieldString.append(NUMBERS);
        return fieldString.toString();
    }

    public String toString(byte[][] FIELD) {
        StringBuilder fieldString = new StringBuilder();
        fieldString.append(NUMBERS);
        for (int line = 0; line < FIELD.length; line++) {
            for (int cell = 0; cell < FIELD[0].length; cell++) {
                if (cell == 0) {
                    fieldString.append("\n");
                    fieldString.append(LETTERS[line]).append(' ');
                }

                fieldString.append(mapFigureCodeToConsoleChar(FIELD, line, cell)).append(' ');

                if (cell == FIELD[0].length - 1) {
                    fieldString.append(LETTERS[line]);
                }
            }
        }
        fieldString.append("\n");
        fieldString.append(NUMBERS);
        return fieldString.toString();
    }
}
