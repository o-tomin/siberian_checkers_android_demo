package com.checkers.entity;

import com.checkers.analytics.GameMath;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Cell implements Comparable, Serializable {
    public static final int NEXT_LEFT = 1;
    public static final int NEXT_RIGHT = 2;
    public static final int PREVIOUS_LEFT = 3;
    public static final int PREVIOUS_RIGHT = 4;
    public static final int ON_ONE_DIAGONAL = 5;

    private static final int QUARTER_ONE = 6;
    private static final int QUARTER_TWO = 7;
    private static final int QUARTER_THREE = 8;
    private static final int QUARTER_FOUR = 9;

    private static final List<Integer> CLOSE_CELLS_CODES =
            Arrays.asList(NEXT_LEFT, NEXT_RIGHT, PREVIOUS_LEFT, PREVIOUS_RIGHT);

    private int letterIndex;
    private int numberIndex;

    //Constructors
    public Cell(int letterIndex, int numberIndex) {
        this.letterIndex = letterIndex;
        this.numberIndex = numberIndex;
    }

    public Cell(int[] indexes) {
        this.letterIndex = indexes[0];
        this.numberIndex = indexes[1];
    }

    // public methods
    public int[] toIndexedCell() {
        return new int[] {letterIndex, numberIndex};
    }

    // public booleans
    public boolean isCloseTo(Cell anotherCell) {
        return CLOSE_CELLS_CODES.contains(this.compareTo(anotherCell));
    }

    // public static
    public static Cell fromString(@NotNull String letterToNumberIndex) throws Exception {
        return new Cell(parseIndexes(letterToNumberIndex));
    }

    public static List<Cell> fromStrings(@NotNull String... letterToNumberIndexes) throws Exception {
        List<Cell> list = new ArrayList<>();
        for (String letterToNumberIndex : letterToNumberIndexes) {
            list.add(fromString(letterToNumberIndex));
        }
        return list;
    }

    // private methods
    private static int[] parseIndexes(String input) throws Exception {
        input = input.trim();
        if (input.startsWith("[") && input.endsWith("]")) {
            input = input.substring(1, input.length() - 1);
        }
        int[] indexes = new int[2];
        try {
            String[] letterToNumber = input.split(",");
            String letterString = letterToNumber[0].trim();
            String numberString = letterToNumber[1].trim();
            indexes[0] = mapLetterToIndex(letterString.charAt(0));
            indexes[1] = mapNumberToIndex(numberString);
        } catch (Exception e) {
            throw new Exception(String.format("Cant parse cell indexes from [%s]", input), e);
        }
        return indexes;
    }

    private static int mapNumberToIndex(String numberString) throws Exception {
        int number = Integer.parseInt(numberString);
        if (number < 9 && number > 0) {
            return number - 1;
        }

        throw new Exception(String.format("Number must be in range of 1 to 8, but found %s", numberString));
    }

    private static int mapLetterToIndex(char letter) throws Exception {
        switch (letter) {
            case 'H': return 0;
            case 'G': return 1;
            case 'F': return 2;
            case 'E': return 3;
            case 'D': return 4;
            case 'C': return 5;
            case 'B': return 6;
            case 'A': return 7;
        }

        throw new Exception(String.format("Letter must be in range of A to H, but found [%s]", String.valueOf(letter)));
    }

    // ----------------------------------- common overrides -----------------------------------
    @Override// todo: make compatible with sort methods
    public int compareTo(@NotNull Object o) {
        if (this == o || this.equals(o)) {
            return 0;
        }
        if (o instanceof Cell) {
            Cell that = (Cell) o;
            if (this.letterIndex + 1 == that.letterIndex) {
                if (this.numberIndex + 1 == that.numberIndex) {
                    return NEXT_RIGHT;
                } else if (this.numberIndex - 1 == that.numberIndex) {
                    return PREVIOUS_RIGHT;
                }
            } else if (this.letterIndex - 1 == that.letterIndex) {
                if (this.numberIndex + 1 == that.numberIndex) {
                    return NEXT_LEFT;
                } else if (this.numberIndex - 1 == that.numberIndex) {
                    return PREVIOUS_LEFT;
                }
            } else if (GameMath.isOnOneDiagonal(this.toIndexedCell(), that.toIndexedCell())) {
                return ON_ONE_DIAGONAL;
            } else if (this.letterIndex > that.letterIndex){
                if (this.numberIndex > that.numberIndex) {
                    return QUARTER_ONE;
                } else {
                    return QUARTER_TWO;
                }
            } else {
                if (this.numberIndex > that.numberIndex) {
                    return QUARTER_THREE;
                } else {
                    return QUARTER_FOUR;
                }
            }
        }

        return Integer.MIN_VALUE;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that instanceof Cell) {
            Cell thatCell = (Cell) that;
            return this.letterIndex == thatCell.letterIndex &&
                    this.numberIndex == thatCell.numberIndex;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(letterIndex, numberIndex);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        switch (letterIndex) {
            case 0: stringBuilder.append('H'); break;
            case 1: stringBuilder.append('G'); break;
            case 2: stringBuilder.append('F'); break;
            case 3: stringBuilder.append('E'); break;
            case 4: stringBuilder.append('D'); break;
            case 5: stringBuilder.append('C'); break;
            case 6: stringBuilder.append('B'); break;
            case 7: stringBuilder.append('A'); break;
        }

        return stringBuilder.append(',')
                .append(' ')
                .append(numberIndex + 1).toString();
    }
}
