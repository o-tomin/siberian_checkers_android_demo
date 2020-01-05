package com.checkers;

import com.checkers.analytics.FieldStateAnalyzer;
import com.checkers.entity.Cell;
import com.checkers.entity.Field;
import com.checkers.entity.GameContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Game {

    /**
     * Siberian checkers
     */

    private final GameContext context;

    public Game(GameContext context) {
        this.context = context;
    }

    public static Game newGame() {
        GameContext context = new GameContext();
        Field field = new Field();
        context.setField(field);
        FieldStateAnalyzer stateAnalyzer = new FieldStateAnalyzer(field);
        stateAnalyzer.updateDataForAnalysis();
        context.setWhitesTurn(true);
        context.setStateAnalyzer(stateAnalyzer);
        return new Game(context);
    }

    public static Game savedGame(GameContext context) {
        return new Game(context);
    }

    public GameContext go(Cell from, Cell to) {
        // Check if step is available with preprocessor
        if (!getStepsFor(from).contains(to)) {
            return context;
        }
        context.getField().moveFigureToEmptyBlackCell(from, to);

        // Post process results
        // If figure has victims it should kill at least one of them
        List<Cell> victims = getVictimsFor(from);
        boolean isShouldToPunish = false;
        boolean isKilledFigureDuringStep = false;
        if (!victims.isEmpty()) {
            for (Cell victim : victims) {
                if (context.getField().isSequential(from, victim, to)) {
                    isKilledFigureDuringStep = true;
                    context.getField().removeFigure(victim);
                    break;
                }
            }

            // If step is released, but none of expected victims killed player should be punished
            if (!isKilledFigureDuringStep) {
                isShouldToPunish = true;
            }
        } else if (!isVictimsEmpty()){
            // If there are victims it means that player had to kill at least one, but he did not: remove one of his
            // killers.
            isShouldToPunish = true;
        }

        // If figure achieved opposite end of the board it should be turned to queen
        boolean isTurnedToQueen = false;
        if(!(context.getField().isWhiteQueen(to) && context.getField().isBlackQueen(to))
                && context.getField().isFigureOnOppositeEdge(to)) {
            context.getField().switchFigureToQueen(to);
            isTurnedToQueen = true;
        }

        boolean isPunished = false;
        // If punishment required then all killers must be removed
        if (isShouldToPunish) {
            getKillerToVictimsMap().keySet()
                    .forEach(cell -> {
                        if (cell.equals(from)) {
                            cell = to;
                        }
                        context.getField().removeFigure(cell);
                    });
            isPunished = true;
        }

        // Update preprocessor
        context.getStateAnalyzer().updateDataForAnalysis();

        // if figure was switched to queen remove it's victims.
        if (isTurnedToQueen) {
            getKillerToVictimsMap().remove(to);
        }

        //Check if game ended
        if (isWhitesTurn()) {
            if (context.getStateAnalyzer().countBlacks() > 0 ) {
                if (context.getStateAnalyzer().countBlacksPossibleSteps() == 0) {
                    context.setBlackInOuthouse(true);
                    context.setWhitesWin(true);
                    return context;
                } else {
                    // If player has new victims for figure he made step with & was not punished do not switch turn
                    if (!isPunished && !getVictimsFor(to).isEmpty() && isKilledFigureDuringStep) {
                        return context;
                    } else {
                        context.setWhitesTurn(!isWhitesTurn());
                        return context;
                    }
                }
            } else {
                context.setWhitesWin(true);
                return context;
            }
        } else {
            if (context.getStateAnalyzer().countWhites() > 0) {
                if (context.getStateAnalyzer().countWhitesPossibleSteps() == 0) {
                    context.setWhitesInOuthouse(true);
                    context.setBlacksWin(true);
                } else {
                    // If player has new victims for figure he made step with & was not punished do not switch turn
                    if (!isPunished && !getVictimsFor(to).isEmpty() && isKilledFigureDuringStep) {
                        return context;
                    } else {
                        context.setWhitesTurn(!isWhitesTurn());
                        return context;
                    }
                }
            } else {
                context.setBlacksWin(true);
                return context;
            }
        }

        return context;
    }

    private boolean isEnemyHasFigures() {
        return isWhitesTurn() ? context.getField().getStateAnalyzer().countWhites() > 0 :
                context.getField().getStateAnalyzer().countBlacks() > 0;
    }

    private boolean isVictimsEmpty() {
        return getKillerToVictimsMap().isEmpty();
    }

    private boolean isQueen(Cell cell) {
        return isWhitesTurn() ? context.getField().isWhiteQueen(cell) : context.getField().isBlackQueen(cell);
    }

    public List<Cell> getStepsFor(Cell cell) {
        return getPossibleStepsMap().getOrDefault(cell, new ArrayList<>());
    }

    private List<Cell> getVictimsFor(Cell killer) {
        return getKillerToVictimsMap().getOrDefault(killer, new ArrayList<>());
    }

    private Map<Cell, List<Cell>> getKillerToVictimsMap() {
        return isWhitesTurn() ? getWhitesPossibleAttacks() : getBlacksPossibleAttacks();
    }

    private Map<Cell, List<Cell>> getPossibleStepsMap() {
        return isWhitesTurn() ? getWhitesPossibleSteps() : getBlacksPossibleSteps();
    }

    //booleans
    public boolean isWhitesTurn() {
        return context.isWhitesTurn();
    }

    public boolean isWhitesWin() {
        return context.isWhitesWin();
    }

    public boolean isBlacksWin() {
        return context.isBlacksWin();
    }

    // getters
    public GameContext getContext() {
        return context;
    }

    public byte[][] getField() {
        return context.getField().cloneField();
    }

    public Map<Cell, List<Cell>> getWhitesPossibleAttacks() {
        return context.getStateAnalyzer().getWhitesPossibleAttacks();
    }

    public Map<Cell, List<Cell>> getBlacksPossibleAttacks() {
        return context.getStateAnalyzer().getBlacksPossibleAttacks();
    }

    public Map<Cell, List<Cell>> getBlacksPossibleSteps() {
        return context.getStateAnalyzer().getBlacksPossibleSteps();
    }

    public Map<Cell, List<Cell>> getWhitesPossibleSteps() {
        return context.getStateAnalyzer().getWhitesPossibleSteps();
    }

    public List<Cell> getWhiteFigures() {
        return context.getStateAnalyzer().getWhiteFigures();
    }

    public List<Cell> getWhiteQueens() {
        return context.getStateAnalyzer().getWhiteQueens();
    }

    public List<Cell> getBlackFigures() {
        return context.getStateAnalyzer().getBlackFigures();
    }

    public List<Cell> getBlackQueens() {
        return context.getStateAnalyzer().getBlackQueens();
    }

    @Override
    public String toString() {
        return Objects.toString(context);
    }
}
