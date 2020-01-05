package com.checkers.analytics;

import com.checkers.entity.Cell;
import com.checkers.entity.Field;
import com.checkers.util.BiPredicateExtension;
import com.checkers.util.TriplePredicate;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class CommonFunctions {

    static long countValues(Map<Cell, List<Cell>> map) {
        return map.keySet().stream()
                .map(map::get)
                .mapToLong(List::size)
                .sum();
    }

    static void takeStockOfFigurePossibleSteps(Field field,
                                               Cell figure,
                                               int[] leftVector,
                                               int[] rightVector,
                                               Map<Cell, List<Cell>> figureToPossibleStepCells) {
        List<Cell> possibleSteps = new ArrayList<>(2);

        field.calculateNextFigureValidCell(figure, leftVector)
                .filter(field::isBlackCell).ifPresent(possibleSteps::add);
        field.calculateNextFigureValidCell(figure, rightVector)
                .filter(field::isBlackCell).ifPresent(possibleSteps::add);

        if (!possibleSteps.isEmpty()) {
            figureToPossibleStepCells.put(figure, possibleSteps);
        }
    }

    static void takeStockOfQueenPossibleSteps(Field field,
                                              Cell queen,
                                              int[] forwardLeftVector,
                                              int[] forwardRightVector,
                                              int[] backwardLeftVector,
                                              int[] backwardRightVector,
                                              Map<Cell, List<Cell>> queenToPossibleStepCells) {
        List<Cell> possibleSteps = new ArrayList<>();
        for (int[] vector : Arrays.asList(forwardLeftVector, forwardRightVector, backwardLeftVector, backwardRightVector)) {
            Optional<Cell> next = Optional.of(queen);
            do {
                next = field.calculateNextFigureValidCell(next.get(), vector).filter(field::isBlackCell);
                next.ifPresent(possibleSteps::add);
            } while (next.isPresent());
        }
        if (!possibleSteps.isEmpty()) {
            queenToPossibleStepCells.put(queen, possibleSteps);
        }
    }

    static void takeStockOfPossibleAttacks(Cell killer,
                                           List<Cell> potentialVictims,
                                           BiPredicateExtension<Cell, Cell> isReachableEnemy,
                                           BiPredicateExtension<Cell, Cell> isAttackPossible,
                                           Map<Cell, List<Cell>> killerToVictimsPossibleAttacks) {
        Map<Cell, List<Cell>> possibleAttacks = KillerToVictimsStreamHolder
                .streamFor(killer)
                .streamOf(potentialVictims)
                .filter(isReachableEnemy)
                .filter(isAttackPossible)
                .collect();

        killerToVictimsPossibleAttacks.putAll(possibleAttacks);
    }

    static void takeStockOfKillingBasedSteps(Field field,
                                             Map<Cell, List<Cell>> possibleAttacks,
                                             Map<Cell, List<Cell>> possibleSteps,
                                             TriplePredicate<Cell, Cell, Cell> isGoodForNextQueenAttack) {
        for(Cell killer : possibleAttacks.keySet()) {
            List<Cell> victims = possibleAttacks.get(killer);
            List<Cell> steps = possibleSteps.getOrDefault(killer, new ArrayList<>());
            if (field.isWhiteFigure(killer) || field.isBlackFigure(killer)) {
                victims.stream()
                        .map(victim -> field.calculateNextCell(killer, victim))
                        .forEach(steps::add);
                possibleSteps.put(killer, steps);
            } else {
                Map<Cell, List<Cell>> victimsToPossibleStepsAfterKilling =  victims.stream()
                        .collect(Collectors.toMap(Function.identity(), victim -> field.calculateNextCells(killer, victim)));
                int initialSize = steps.size();
                victimsToPossibleStepsAfterKilling.forEach((victim, blackCells) ->
                    blackCells.stream()
                            .filter(blackCell -> isGoodForNextQueenAttack.test(killer, victim, blackCell))
                            .forEach(steps::add));
                if (initialSize == steps.size()) {
                    victims.stream()
                            .map(victim -> field.calculateNextCells(killer, victim))
                            .flatMap(List::stream)
                            .forEach(steps::add);
                }
                possibleSteps.put(killer, steps);
            }
        }
    }
}
