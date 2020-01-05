package com.checkers.analytics;

import com.checkers.entity.Cell;
import com.checkers.entity.Field;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.checkers.analytics.CommonFunctions.*;

public class FieldStateAnalyzer {
    private Field field;
    private List<Cell> whiteFigures = new ArrayList<>();
    private List<Cell> whiteQueens = new ArrayList<>();
    private List<Cell> blackFigures = new ArrayList<>();
    private List<Cell> blackQueens = new ArrayList<>();
    private Map<Cell, List<Cell>> whitesPossibleAttacks = new HashMap<>();
    private Map<Cell, List<Cell>> blacksPossibleAttacks = new HashMap<>();
    private Map<Cell, List<Cell>> whitesPossibleSteps = new HashMap<>();
    private Map<Cell, List<Cell>> blacksPossibleSteps = new HashMap<>();

    public FieldStateAnalyzer(Field field) {
        this.field = field;
    }

    // update analized data methods
    public void updateDataForAnalysis() {
        updateFiguresData();
        updateWhitesPossibleAttacks();
        updateBlacksPossibleAttacks();
        updateBlacksPossibleSteps();
        updateWhitesPossibleSteps();
    }

    public void updateFiguresData() {
        Stream.of(whiteFigures, blackFigures, whiteQueens, blackQueens).forEach(List::clear);
        field.forEachCell(cell -> {
            if (field.isWhiteFigure(cell)) {
                whiteFigures.add(cell);
            } else if (field.isWhiteQueen(cell)) {
                whiteQueens.add(cell);
            } else if (field.isBlackFigure(cell)) {
                blackFigures.add(cell);
            } else if (field.isBlackQueen(cell)) {
                blackQueens.add(cell);
            } });
    }

    public void updateWhitesPossibleAttacks() {
        whitesPossibleAttacks.clear();
        List<Cell> blacks = Stream.of(blackFigures, blackQueens)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        for (Cell whiteFigure : whiteFigures) {
            takeStockOfPossibleAttacks(whiteFigure, blacks, this::isReachableEnemyForWhiteFigure,
                    this::isAttackPossible, whitesPossibleAttacks);
        }
        for (Cell whiteQueen : whiteQueens) {
            takeStockOfPossibleAttacks(whiteQueen, blacks, this::isReachableEnemyForWhiteQueen,
                    this::isAttackPossible, whitesPossibleAttacks);
        }
    }

    public void updateBlacksPossibleAttacks() {
        blacksPossibleAttacks.clear();
        List<Cell> whites = Stream.of(whiteFigures, whiteQueens)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        for (Cell blackFigure : blackFigures) {
            takeStockOfPossibleAttacks(blackFigure, whites, this::isReachableEnemyForBlackFigure,
                    this::isAttackPossible, blacksPossibleAttacks);
        }
        for (Cell blackQueen : blackQueens) {
            takeStockOfPossibleAttacks(blackQueen, whites, this::isReachableEnemyForBlackQueen,
                    this::isAttackPossible, blacksPossibleAttacks);
        }
    }

    public void updateWhitesPossibleSteps() {
        whitesPossibleSteps.clear();
        whiteFigures.forEach(this::takeStockOfFigurePossibleSteps);
        whiteQueens.forEach(this::takeStockOfQueenPossibleSteps);
        takeStockOfKillingBasedSteps(field, whitesPossibleAttacks, whitesPossibleSteps, this::isGoodForNextQueenAttack);
    }

    public void updateBlacksPossibleSteps() {
        blacksPossibleSteps.clear();
        blackFigures.forEach(this::takeStockOfFigurePossibleSteps);
        blackQueens.forEach(this::takeStockOfQueenPossibleSteps);
        takeStockOfKillingBasedSteps(field, blacksPossibleAttacks, blacksPossibleSteps, this::isGoodForNextQueenAttack);
    }

    public int countWhites() {
        return whiteQueens.size() + whiteFigures.size();
    }

    public int countBlacks() {
        return blackQueens.size() + blackFigures.size();
    }

    public int countWhitesPossibleAttacks() {
        return (int) countValues(whitesPossibleAttacks);
    }

    public int countBlacksPossibleAttacks() {
        return (int) countValues(blacksPossibleAttacks);
    }

    public int countWhitesPossibleSteps() {
        return (int) countValues(whitesPossibleSteps);
    }

    public int countBlacksPossibleSteps() {
        return (int) countValues(blacksPossibleSteps);
    }

    //getters
    public Map<Cell, List<Cell>> getBlacksPossibleAttacks() {
        return blacksPossibleAttacks;
    }

    public Map<Cell, List<Cell>> getWhitesPossibleAttacks() {
        return whitesPossibleAttacks;
    }

    public Map<Cell, List<Cell>> getBlacksPossibleSteps() {
        return blacksPossibleSteps;
    }

    public Map<Cell, List<Cell>> getWhitesPossibleSteps() {
        return whitesPossibleSteps;
    }

    public List<Cell> getWhiteFigures() {
        return whiteFigures;
    }

    public List<Cell> getWhiteQueens() {
        return whiteQueens;
    }

    public List<Cell> getBlackFigures() {
        return blackFigures;
    }

    public List<Cell> getBlackQueens() {
        return blackQueens;
    }

    // private methods
    private void takeStockOfFigurePossibleSteps(Cell figure) {
        if (field.isWhiteFigure(figure)) {
            CommonFunctions.takeStockOfFigurePossibleSteps(field, figure, Field.WHITE_FIGURE_LEFT_VECTOR,
                    Field.WHITE_FIGURE_RIGHT_VECTOR, whitesPossibleSteps);
        } else if (field.isBlackFigure(figure)) {
            CommonFunctions.takeStockOfFigurePossibleSteps(field, figure, Field.BLACK_FIGURE_LEFT_VECTOR,
                    Field.BLACK_FIGURE_RIGHT_VECTOR, blacksPossibleSteps);
        }
    }

    private void takeStockOfQueenPossibleSteps(Cell queen) {
        if (field.isWhiteQueen(queen)) {
            CommonFunctions.takeStockOfQueenPossibleSteps(field, queen, Field.WHITE_FIGURE_LEFT_VECTOR, Field.WHITE_FIGURE_RIGHT_VECTOR,
                    Field.BLACK_FIGURE_LEFT_VECTOR, Field.BLACK_FIGURE_RIGHT_VECTOR, whitesPossibleSteps);
        } else if (field.isBlackQueen(queen)) {
            CommonFunctions.takeStockOfQueenPossibleSteps(field, queen, Field.WHITE_FIGURE_LEFT_VECTOR, Field.WHITE_FIGURE_RIGHT_VECTOR,
                    Field.BLACK_FIGURE_LEFT_VECTOR, Field.BLACK_FIGURE_RIGHT_VECTOR, blacksPossibleSteps);
        }
    }

    //booleans
    boolean isReachableEnemyForBlackFigure(Cell blackFigure, Cell white) {
        return isEnemies(blackFigure, white) && field.isCloseTo(blackFigure, white);
    }

    boolean isReachableEnemyForWhiteFigure(Cell whiteFigure, Cell black) {
        return isEnemies(whiteFigure, black) && field.isCloseTo(whiteFigure, black);
    }

    boolean isReachableEnemyForBlackQueen(Cell blackQueen, Cell white) {
        return isEnemies(blackQueen, white)
                && field.isOnOneDiagonal(blackQueen, white)
                && isNoConstraintsInBetween(blackQueen, white);
    }

    boolean isReachableEnemyForWhiteQueen(Cell whiteQueen, Cell black) {
        return isEnemies(whiteQueen, black)
                && field.isOnOneDiagonal(whiteQueen, black)
                && isNoConstraintsInBetween(whiteQueen, black);
    }

    boolean isAttackPossible(Cell killer, Cell possibleVictim) {
        Cell nextCell = new Cell(
                GameMath.calculateNextCellBasedOnTwoCells(killer.toIndexedCell(), possibleVictim.toIndexedCell()));
        return field.isBlackCell(nextCell);
    }

    boolean isNoConstraintsInBetween(Cell left, Cell right) {
        List<Cell> inBetweenCells = field.calculateInBetweenCells(left, right);
        long inBetweenBlackCellsCount = inBetweenCells.stream().filter(field::isBlackCell).count();
        return inBetweenBlackCellsCount == inBetweenCells.size();
    }

    boolean isEnemies(Cell origin, Cell opposite) {
        return (isWhites(origin) && isBlacks(opposite)) || (isBlacks(origin) && isWhites(opposite));
    }

    boolean isWhites(Cell cell) {
        return field.isWhiteFigure(cell) || field.isWhiteQueen(cell);
    }

    boolean isBlacks(Cell cell) {
        return field.isBlackFigure(cell) || field.isBlackQueen(cell);
    }

    boolean isGoodForNextQueenAttack(Cell killer, Cell victim, Cell validCellToGo) {
        return field.runOnImaginingField(validCellToGo, killer,
                () -> isGoodForNextQueenAttackCommand(validCellToGo, victim));
    }

    private boolean isGoodForNextQueenAttackCommand(Cell queen, Cell victimToIgnore) {
        Map<Cell, List<Cell>> possibleAttacksMap = new HashMap<>();
        if (field.isWhiteQueen(queen)) {
            List<Cell> blackFiguresCopy = new ArrayList<>(blackFigures);
            blackFiguresCopy.addAll(blackQueens);
            blackFiguresCopy.remove(victimToIgnore);
            takeStockOfPossibleAttacks(queen, blackFiguresCopy, this::isReachableEnemyForWhiteQueen,
                    this::isAttackPossible, possibleAttacksMap);
        } else {
            List<Cell> whiteFiguresCopy = new ArrayList<>(whiteFigures);
            whiteFiguresCopy.addAll(whiteQueens);
            whiteFiguresCopy.remove(victimToIgnore);
            takeStockOfPossibleAttacks(queen, whiteFiguresCopy, this::isReachableEnemyForBlackQueen,
                    this::isAttackPossible, possibleAttacksMap);
        }
        return !possibleAttacksMap.isEmpty();
    }
}
