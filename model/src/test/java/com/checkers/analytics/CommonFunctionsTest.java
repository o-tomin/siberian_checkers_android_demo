package com.checkers.analytics;

import com.checkers.entity.Cell;
import com.checkers.entity.Field;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.*;


public class CommonFunctionsTest {
    private static final Cell ZERO_ZERO_CELL = new Cell(0,0);
    private static final Cell ONE_ONE_CELL = new Cell(1, 1);
    private static final Cell TWO_TWO_CELL = new Cell(2,2);

    private static final Map<Cell, List<Cell>> CELL_ASSOCIATION_MAP = new HashMap<Cell, List<Cell>>() {
        {
            put(ZERO_ZERO_CELL, Arrays.asList(ZERO_ZERO_CELL));
            put(ONE_ONE_CELL, Arrays.asList(ZERO_ZERO_CELL, ZERO_ZERO_CELL));
            put(TWO_TWO_CELL, Arrays.asList(ZERO_ZERO_CELL, ZERO_ZERO_CELL, ZERO_ZERO_CELL));
        }
    };

    private Field field;
    private FieldStateAnalyzer analyzer;

    @BeforeMethod
    public void init() {
        field = new Field();
        analyzer = new FieldStateAnalyzer(field);
    }

    //White Figure possible game situations
    private static final byte[][] WHITE_FIGURE_CAN_GO_LEFT_FORWARD_FIELD = {
            {0, 1},
            {2, 0},
            {0, 3}};

    @Test
    public void takeStockOfFigurePossibleSteps_WhiteFigureCanGoLeftForwardTest() throws Exception {
        Map<Cell, List<Cell>> figureToPossibleStepCells = new HashMap<>();
        field.magicUpdate(WHITE_FIGURE_CAN_GO_LEFT_FORWARD_FIELD);
        Cell whiteFigure = new Cell(1, 0);
        CommonFunctions.takeStockOfFigurePossibleSteps(
                field,
                whiteFigure,
                Field.WHITE_FIGURE_LEFT_VECTOR,
                Field.WHITE_FIGURE_RIGHT_VECTOR,
                figureToPossibleStepCells);

        assertEquals(figureToPossibleStepCells.get(whiteFigure),
                Arrays.asList(new Cell(0, 1)));
    }

    private static final byte[][] WHITE_FIGURE_CAN_GO_RIGHT_FORWARD_FIELD = {
            {0, 3},
            {2, 0},
            {0, 1}
    };

    @Test
    public void takeStockOfFigurePossibleSteps_whiteFigureCanGoRightForwardTest() throws Exception {
        Map<Cell, List<Cell>> figureToPossibleStepCells = new HashMap<>();
        field.magicUpdate(WHITE_FIGURE_CAN_GO_RIGHT_FORWARD_FIELD);
        Cell whiteFigure = new Cell(1, 0);
        CommonFunctions.takeStockOfFigurePossibleSteps(
                field,
                whiteFigure,
                Field.WHITE_FIGURE_LEFT_VECTOR,
                Field.WHITE_FIGURE_RIGHT_VECTOR,
                figureToPossibleStepCells);

        assertEquals(figureToPossibleStepCells.get(whiteFigure),
                Arrays.asList(new Cell(2, 1)));
    }
    private static final byte[][] WHITE_FIGURE_CAN_GO_LEFT_OR_RIGHT_FORWARD_FIELD = {
            {0, 1},
            {2, 0},
            {0, 1}
    };

    @Test
    public void takeStockOfFigurePossibleSteps_whiteFigureCanGoLeftOrRightForwardTest() throws Exception {
        Map<Cell, List<Cell>> figureToPossibleStepCells = new HashMap<>();
        field.magicUpdate(WHITE_FIGURE_CAN_GO_LEFT_OR_RIGHT_FORWARD_FIELD);
        Cell whiteFigure = new Cell(1, 0);
        CommonFunctions.takeStockOfFigurePossibleSteps(
                field,
                whiteFigure,
                Field.WHITE_FIGURE_LEFT_VECTOR,
                Field.WHITE_FIGURE_RIGHT_VECTOR,
                figureToPossibleStepCells);

        List<Cell> calculatedList = figureToPossibleStepCells.get(whiteFigure);
        List<Cell> expectedList = Arrays.asList(new Cell(0, 1), new Cell(2, 1));
        assertEquals(calculatedList, expectedList);
    }

    private static final byte[][] WHITE_FIGURE_CANT_GO_FORWARD_FIELD = {
            {0, 3},
            {2, 0},
            {0, 3}
    };

    @Test
    public void takeStockOfFigurePossibleSteps_whiteFigureCantGoForwardTest() throws Exception {
        Map<Cell, List<Cell>> figureToPossibleStepCells = new HashMap<>();
        field.magicUpdate(WHITE_FIGURE_CANT_GO_FORWARD_FIELD);
        Cell whiteFigure = new Cell(1, 0);
        CommonFunctions.takeStockOfFigurePossibleSteps(
                field,
                whiteFigure,
                Field.WHITE_FIGURE_LEFT_VECTOR,
                Field.WHITE_FIGURE_RIGHT_VECTOR,
                figureToPossibleStepCells);

        assertEquals(figureToPossibleStepCells.get(whiteFigure), null);
    }

    private static final byte[][] WHITE_FIGURE_CANT_GO_BACK_FIELD = {
            {1, 0},
            {0, 2},
            {1, 0}
    };

    @Test
    public void takeStockOfFigurePossibleSteps_whiteFigureCantGoBackTest() throws Exception {
        Map<Cell, List<Cell>> figureToPossibleStepCells = new HashMap<>();
        field.magicUpdate(WHITE_FIGURE_CANT_GO_BACK_FIELD);
        Cell whiteFigure = new Cell(1, 0);
        CommonFunctions.takeStockOfFigurePossibleSteps(
                field,
                whiteFigure,
                Field.WHITE_FIGURE_LEFT_VECTOR,
                Field.WHITE_FIGURE_RIGHT_VECTOR,
                figureToPossibleStepCells);

        assertEquals(figureToPossibleStepCells.get(whiteFigure), null);
    }

    private static final byte[][] WHITE_FIGURE_CAN_ATTACK_BLACK_FIGURE_FORWARD_FIELD = {
            {1, 0, 1},
            {0, 4, 0},
            {2, 0, 1}
    };

    @Test
    public void takeStockOfPossibleAttacks_whiteFigureCanAttackBlackFigureForwardTest() throws Exception {
        Map<Cell, List<Cell>> killerToVictimsMap = new HashMap<>();
        field.magicUpdate(WHITE_FIGURE_CAN_ATTACK_BLACK_FIGURE_FORWARD_FIELD);
        Cell whiteFigure = new Cell(2, 0);
        List<Cell> possibleVictims = Collections.singletonList(new Cell(1,1));
        CommonFunctions.takeStockOfPossibleAttacks(
                whiteFigure,
                possibleVictims,
                analyzer::isReachableEnemyForWhiteFigure,
                analyzer::isAttackPossible,
                killerToVictimsMap);

        assertEquals(killerToVictimsMap.get(whiteFigure), possibleVictims);
    }

    private static final byte[][] WHITE_FIGURE_CANT_ATTACK_BLACK_FIGURE_FORWARD_FIELD = {
            {1, 0, 3},
            {0, 4, 0},
            {2, 0, 1}
    };

    @Test
    public void takeStockOfPossibleAttacks_whiteFigureCantAttackBlackFigureForwardTest() throws Exception {
        Map<Cell, List<Cell>> killerToVictimsMap = new HashMap<>();
        field.magicUpdate(WHITE_FIGURE_CANT_ATTACK_BLACK_FIGURE_FORWARD_FIELD);
        Cell whiteFigure = new Cell(2, 0);
        List<Cell> possibleVictims = Collections.singletonList(new Cell(1,1));
        CommonFunctions.takeStockOfPossibleAttacks(
                whiteFigure,
                possibleVictims,
                analyzer::isReachableEnemyForWhiteFigure,
                analyzer::isAttackPossible,
                killerToVictimsMap);

        assertEquals(killerToVictimsMap.get(whiteFigure), null);
    }

    private static final byte[][] WHITE_FIGURE_CAN_ATTACK_BLACK_FIGURE_BACKWARD_FIELD = {
            {1, 0, 1},
            {0, 4, 0},
            {1, 0, 2}
    };

    @Test
    public void takeStockOfPossibleAttacks_whiteFigureCanAttackBlackFigureBackwardTest() throws Exception {
        Map<Cell, List<Cell>> killerToVictimsMap = new HashMap<>();
        field.magicUpdate(WHITE_FIGURE_CAN_ATTACK_BLACK_FIGURE_BACKWARD_FIELD);
        Cell whiteFigure = new Cell(2, 2);
        List<Cell> possibleVictims = Collections.singletonList(new Cell(1,1));
        CommonFunctions.takeStockOfPossibleAttacks(
                whiteFigure,
                possibleVictims,
                analyzer::isReachableEnemyForWhiteFigure,
                analyzer::isAttackPossible,
                killerToVictimsMap);

        assertEquals(killerToVictimsMap.get(whiteFigure), possibleVictims);
    }

    private static final byte[][] WHITE_FIGURE_CANT_ATTACK_BLACK_FIGURE_BACKWARD_FIELD = {
            {3, 0, 1},
            {0, 4, 0},
            {1, 0, 2}
    };

    @Test
    public void takeStockOfPossibleAttacks_whiteFigureCantAttackBlackFigureBackwardTest() throws Exception {
        Map<Cell, List<Cell>> killerToVictimsMap = new HashMap<>();
        field.magicUpdate(WHITE_FIGURE_CANT_ATTACK_BLACK_FIGURE_BACKWARD_FIELD);
        Cell whiteFigure = new Cell(2, 2);
        List<Cell> possibleVictims = Collections.singletonList(new Cell(1,1));
        CommonFunctions.takeStockOfPossibleAttacks(
                whiteFigure,
                possibleVictims,
                analyzer::isReachableEnemyForWhiteFigure,
                analyzer::isAttackPossible,
                killerToVictimsMap);

        assertEquals(killerToVictimsMap.get(whiteFigure), null);
    }

    private static final byte[][] WHITE_FIGURE_CAN_ATTACK_BLACK_QUEEN_FORWARD_FIELD = {
            {2, 0, 1},
            {0, 5, 0},
            {1, 0, 1}
    };

    @Test
    public void takeStockOfPossibleAttacks_whiteFigureCanAttackBlackQueenForwardTest() throws Exception {
        Map<Cell, List<Cell>> killerToVictimsMap = new HashMap<>();
        field.magicUpdate(WHITE_FIGURE_CAN_ATTACK_BLACK_QUEEN_FORWARD_FIELD);
        Cell whiteFigure = new Cell(0, 0);
        List<Cell> possibleVictims = Collections.singletonList(new Cell(1,1));
        CommonFunctions.takeStockOfPossibleAttacks(
                whiteFigure,
                possibleVictims,
                analyzer::isReachableEnemyForWhiteFigure,
                analyzer::isAttackPossible,
                killerToVictimsMap);

        assertEquals(killerToVictimsMap.get(whiteFigure), possibleVictims);
    }

    private static final byte[][] WHITE_FIGURE_CANT_ATTACK_BLACK_QUEEN_FORWARD_FIELD = {
            {2, 0, 1},
            {0, 5, 0},
            {1, 0, 4}
    };

    @Test
    public void takeStockOfPossibleAttacks_whiteFigureCantAttackBlackQueenForwardTest() throws Exception {
        Map<Cell, List<Cell>> killerToVictimsMap = new HashMap<>();
        field.magicUpdate(WHITE_FIGURE_CANT_ATTACK_BLACK_QUEEN_FORWARD_FIELD);
        Cell whiteFigure = new Cell(0, 0);
        List<Cell> possibleVictims = Collections.singletonList(new Cell(1,1));
        CommonFunctions.takeStockOfPossibleAttacks(
                whiteFigure,
                possibleVictims,
                analyzer::isReachableEnemyForWhiteFigure,
                analyzer::isAttackPossible,
                killerToVictimsMap);

        assertEquals(killerToVictimsMap.get(whiteFigure), null);
    }

    private static final byte[][] WHITE_FIGURE_CAN_ATTACK_BLACK_QUEEN_BACKWARD_FIELD = {
            {1, 0, 2},
            {0, 5, 0},
            {1, 0, 1}
    };

    @Test
    public void takeStockOfPossibleAttacks_whiteFigureCanAttackBlackQueenBackwardTest() throws Exception {
        Map<Cell, List<Cell>> killerToVictimsMap = new HashMap<>();
        field.magicUpdate(WHITE_FIGURE_CAN_ATTACK_BLACK_QUEEN_BACKWARD_FIELD);
        Cell whiteFigure = new Cell(0, 2);
        List<Cell> possibleVictims = Collections.singletonList(new Cell(1,1));
        CommonFunctions.takeStockOfPossibleAttacks(
                whiteFigure,
                possibleVictims,
                analyzer::isReachableEnemyForWhiteFigure,
                analyzer::isAttackPossible,
                killerToVictimsMap);

        assertEquals(killerToVictimsMap.get(whiteFigure), possibleVictims);
    }

    private static final byte[][] WHITE_FIGURE_CANT_ATTACK_BLACK_QUEEN_BACKWARD_FIELD = {
            {1, 0, 2},
            {0, 5, 0},
            {3, 0, 1}
    };

    @Test
    public void takeStockOfPossibleAttacks_whiteFigureCantAttackBlackQueenBackwardTest() throws Exception {
        Map<Cell, List<Cell>> killerToVictimsMap = new HashMap<>();
        field.magicUpdate(WHITE_FIGURE_CANT_ATTACK_BLACK_QUEEN_BACKWARD_FIELD);
        Cell whiteFigure = new Cell(0, 2);
        List<Cell> possibleVictims = Collections.singletonList(new Cell(1,1));
        CommonFunctions.takeStockOfPossibleAttacks(
                whiteFigure,
                possibleVictims,
                analyzer::isReachableEnemyForWhiteFigure,
                analyzer::isAttackPossible,
                killerToVictimsMap);

        assertEquals(killerToVictimsMap.get(whiteFigure), null);
    }

    //White Queen possible game situations
    private static final byte[][] WHITE_QUEEN_CAN_GO_FORWARD_FIELD = {
            {1, 0, 2},
            {0, 1, 0},
            {3, 0, 1}
    };

    @Test
    public void takeStockOfQueenPossibleSteps_whiteQueenCanGoForwardTest() throws Exception {
        Map<Cell, List<Cell>> figureToPossibleStepCells = new HashMap<>();
        field.magicUpdate(WHITE_QUEEN_CAN_GO_FORWARD_FIELD);
        Cell whiteQueen = new Cell(2, 0);
        CommonFunctions.takeStockOfQueenPossibleSteps(
                field,
                whiteQueen,
                Field.WHITE_FIGURE_LEFT_VECTOR,
                Field.WHITE_FIGURE_RIGHT_VECTOR,
                Field.BLACK_FIGURE_LEFT_VECTOR,
                Field.BLACK_FIGURE_RIGHT_VECTOR,
                figureToPossibleStepCells);

        assertEquals(figureToPossibleStepCells.get(whiteQueen),
                Collections.singletonList(new Cell(1,1)));
    }

    private static final byte[][] WHITE_QUEEN_CANT_GO_FORWARD_FIELD = {
            {1, 0, 1},
            {0, 2, 0},
            {3, 0, 1}
    };

    @Test
    public void takeStockOfQueenPossibleSteps_whiteQueenCantGoForwardTest() throws Exception {
        Map<Cell, List<Cell>> figureToPossibleStepCells = new HashMap<>();
        field.magicUpdate(WHITE_QUEEN_CANT_GO_FORWARD_FIELD);
        Cell whiteQueen = new Cell(2, 0);
        CommonFunctions.takeStockOfQueenPossibleSteps(
                field,
                whiteQueen,
                Field.WHITE_FIGURE_LEFT_VECTOR,
                Field.WHITE_FIGURE_RIGHT_VECTOR,
                Field.BLACK_FIGURE_LEFT_VECTOR,
                Field.BLACK_FIGURE_RIGHT_VECTOR,
                figureToPossibleStepCells);

        assertEquals(figureToPossibleStepCells.get(whiteQueen), null);
    }

    private static final byte[][] WHITE_QUEEN_CAN_ATTACK_BLACK_FIGURE_FIELD = {
            {3, 0, 1, 0, 1},
            {0, 1, 0, 1, 0},
            {1, 0, 1, 0, 1},
            {0, 1, 0, 4, 0},
            {1, 0, 1, 0, 1}
    };

    @Test
    public void takeStockOfPossibleAttacks_whiteQueenCanAttackBlackFigureTest() throws Exception {
        Map<Cell, List<Cell>> killerToVictimsMap = new HashMap<>();
        field.magicUpdate(WHITE_QUEEN_CAN_ATTACK_BLACK_FIGURE_FIELD);
        Cell whiteQueen = new Cell(0, 0);
        List<Cell> possibleVictims = Collections.singletonList(new Cell(3,3));
        CommonFunctions.takeStockOfPossibleAttacks(
                whiteQueen,
                possibleVictims,
                analyzer::isReachableEnemyForWhiteQueen,
                analyzer::isAttackPossible,
                killerToVictimsMap);

        assertEquals(killerToVictimsMap.get(whiteQueen), possibleVictims);
    }

    private static final byte[][] WHITE_QUEEN_CANT_ATTACK_BLACK_FIGURE_FIELD = {
            {3, 0, 1, 0, 1},
            {0, 1, 0, 1, 0},
            {1, 0, 1, 0, 1},
            {0, 1, 0, 4, 0},
            {1, 0, 1, 0, 5}
    };

    @Test
    public void takeStockOfPossibleAttacks_whiteQueenCantAttackBlackFigureTest() throws Exception {
        Map<Cell, List<Cell>> killerToVictimsMap = new HashMap<>();
        field.magicUpdate(WHITE_QUEEN_CANT_ATTACK_BLACK_FIGURE_FIELD);
        Cell whiteQueen = new Cell(0, 0);
        List<Cell> possibleVictims = Collections.singletonList(new Cell(3,3));
        CommonFunctions.takeStockOfPossibleAttacks(
                whiteQueen,
                possibleVictims,
                analyzer::isReachableEnemyForWhiteQueen,
                analyzer::isAttackPossible,
                killerToVictimsMap);

        assertEquals(killerToVictimsMap.get(whiteQueen), null);
    }

    private static final byte[][] WHITE_QUEEN_CAN_ATTACK_BLACK_QUEEN_FIELD = {
            {1, 0, 1, 0, 1},
            {0, 1, 0, 5, 0},
            {1, 0, 1, 0, 1},
            {0, 1, 0, 4, 0},
            {3, 0, 1, 0, 1}
    };

    @Test
    public void takeStockOfPossibleAttacks_whiteQueenCanAttackBlackQueenTest() throws Exception {
        Map<Cell, List<Cell>> killerToVictimsMap = new HashMap<>();
        field.magicUpdate(WHITE_QUEEN_CAN_ATTACK_BLACK_QUEEN_FIELD);
        Cell whiteQueen = new Cell(4, 0);
        List<Cell> possibleVictims = Collections.singletonList(new Cell(1,3));
        CommonFunctions.takeStockOfPossibleAttacks(
                whiteQueen,
                possibleVictims,
                analyzer::isReachableEnemyForWhiteQueen,
                analyzer::isAttackPossible,
                killerToVictimsMap);

        assertEquals(killerToVictimsMap.get(whiteQueen), possibleVictims);
    }

    private static final byte[][] WHITE_QUEEN_CANT_ATTACK_BLACK_QUEEN_FIELD = {
            {1, 0, 1, 0, 4},
            {0, 1, 0, 5, 0},
            {1, 0, 1, 0, 1},
            {0, 1, 0, 4, 0},
            {3, 0, 1, 0, 1}
    };

    @Test
    public void takeStockOfPossibleAttacks_whiteQueenCantAttackBlackQueenTest() throws Exception {
        Map<Cell, List<Cell>> killerToVictimsMap = new HashMap<>();
        field.magicUpdate(WHITE_QUEEN_CANT_ATTACK_BLACK_QUEEN_FIELD);
        Cell whiteQueen = new Cell(0, 0);
        List<Cell> possibleVictims = Collections.singletonList(new Cell(3,3));
        CommonFunctions.takeStockOfPossibleAttacks(
                whiteQueen,
                possibleVictims,
                analyzer::isReachableEnemyForWhiteQueen,
                analyzer::isAttackPossible,
                killerToVictimsMap);

        assertEquals(killerToVictimsMap.get(whiteQueen), null);
    }

    private static final byte[][] WHITE_QUEEN_CANT_ATTACK_BLACK_QUEEN_AND_FIGURE_FIELD = {
            {1, 0, 1, 0, 1},
            {0, 1, 0, 5, 0},
            {1, 0, 1, 0, 3},
            {0, 1, 0, 4, 0},
            {1, 0, 1, 0, 1}
    };

    @Test
    public void takeStockOfPossibleAttacks_whiteQueenCantAttackBlackQueenAndFigureTest() throws Exception {
        Map<Cell, List<Cell>> killerToVictimsMap = new HashMap<>();
        field.magicUpdate(WHITE_QUEEN_CANT_ATTACK_BLACK_QUEEN_AND_FIGURE_FIELD);
        Cell whiteQueen = new Cell(2, 4);
        List<Cell> possibleVictims = Arrays.asList(new Cell(1,3),
                new Cell(3,3));
        CommonFunctions.takeStockOfPossibleAttacks(
                whiteQueen,
                possibleVictims,
                analyzer::isReachableEnemyForWhiteQueen,
                analyzer::isAttackPossible,
                killerToVictimsMap);

        assertEquals(killerToVictimsMap.get(whiteQueen), possibleVictims);
    }

    @Test
    public void countValuesTest() {
        assertEquals(6, CommonFunctions.countValues(CELL_ASSOCIATION_MAP));
    }

    private static final byte[][] FIGURE_KILLER_CAN_STEP_ON_FILED_AFTER_VICTIM_FIELD = {
            {1, 0, 1, 0, 1},
            {0, 4, 0, 4, 0},
            {1, 0, 2, 0, 1},
            {0, 4, 0, 4, 0},
            {1, 0, 1, 0, 1}
    };

    @Test
    public void takeStockOfKillingBasedStepsFigureKillerTest() throws Exception {
        field.magicUpdate(FIGURE_KILLER_CAN_STEP_ON_FILED_AFTER_VICTIM_FIELD);
        Cell whiteFigure = new Cell(2, 2);
        Map<Cell, List<Cell>> possibleAttacks = new HashMap<>();
        possibleAttacks.put(whiteFigure, Arrays.asList(
                new Cell(1, 1),
                new Cell(3, 1),
                new Cell(1, 3),
                new Cell(3, 3)));
        Map<Cell, List<Cell>> possibleSteps = new HashMap<>();
        CommonFunctions.takeStockOfKillingBasedSteps(field,
                possibleAttacks,
                possibleSteps,
                analyzer::isGoodForNextQueenAttack);
        assertEqualsDeep(new HashMap() {
            {put(whiteFigure, Arrays.asList(
                    new Cell(0, 0),
                    new Cell(4, 0),
                    new Cell(0, 4),
                    new Cell(4, 4)));}
        }, possibleSteps);
    }

    private static final byte[][] QUEEN_KILLER_CAN_STEP_ON_FILED_AFTER_VICTIM_FIELD = {
           //1  2  3  4  5  6  7  8
            {1, 0, 1, 0, 1, 0, 1, 0},  //H
            {0, 1, 0, 1, 0, 1, 0, 1},  //G
            {1, 0, 4, 0, 5, 0, 1, 0},  //F
            {0, 1, 0, 3, 0, 1, 0, 1},  //E
            {1, 0, 5, 0, 4, 0, 1, 0},  //D
            {0, 1, 0, 1, 0, 1, 0, 1},  //C
            {1, 0, 1, 0, 1, 0, 1, 0},  //B
            {0, 1, 0, 1, 0, 1, 0, 5}   //A
           //1  2  3  4  5  6  7  8
    };

    @Test
    public void takeStockOfKillingBasedStepsQueenKillerTest() throws Exception {
        field.magicUpdate(QUEEN_KILLER_CAN_STEP_ON_FILED_AFTER_VICTIM_FIELD);
        analyzer.updateFiguresData();
        Cell whiteQueen = Cell.fromString("E, 4");
        Map<Cell, List<Cell>> possibleAttacks = new HashMap<>();
        possibleAttacks.put(whiteQueen, Arrays.asList(
                new Cell(2, 2),
                new Cell(2, 4),
                new Cell(4, 2),
                new Cell(4, 4)));
        Map<Cell, List<Cell>> possibleSteps = new HashMap<>();
        CommonFunctions.takeStockOfKillingBasedSteps(field,
                possibleAttacks,
                possibleSteps,
                analyzer::isGoodForNextQueenAttack);

        List<Cell> stepsActual = possibleSteps.get(whiteQueen);
        List<Cell> stepsExpected = new ArrayList(){
            {
                add(new Cell(0, 0));
                add(new Cell(1, 1));
                add(new Cell(0, 6));
                add(new Cell(1, 5));
                add(new Cell(6, 6));
                add(new Cell(5, 5));
                add(new Cell(6, 0));
                add(new Cell(5, 1));
            }
        };
        //todo: need to fix compareTo in Cell class
        assertTrue(stepsActual.containsAll(stepsExpected));
    }

    private static final byte[][] QUEEN_SHOULD_STEP_ON_GOOD_FOR_NEXT_KILLING_CELL_IF_NEXT_VICTIM_PRESENT = {
           //1  2  3  4  5  6  7  8
            {1, 0, 1, 0, 1, 0, 1, 0}, //H
            {0, 1, 0, 5, 0, 1, 0, 1}, //G
            {1, 0, 1, 0, 1, 0, 1, 0}, //F
            {0, 1, 0, 1, 0, 1, 0, 1}, //E
            {1, 0, 4, 0, 4, 0, 1, 0}, //D
            {0, 1, 0, 1, 0, 1, 0, 1}, //C
            {1, 0, 1, 0, 1, 0, 3, 0}, //B
            {0, 1, 0, 1, 0, 1, 0, 1}  //A
           //1  2  3  4  5  6  7  8
    };

    @Test
    public void takeStockOfKillingBasedStepsQueenKillerStepToCorrectCellTest() throws Exception {
        field.magicUpdate(QUEEN_SHOULD_STEP_ON_GOOD_FOR_NEXT_KILLING_CELL_IF_NEXT_VICTIM_PRESENT);
        analyzer.updateFiguresData();
        Cell whiteQueen = new Cell(6, 6);
        Map<Cell, List<Cell>> possibleAttacks = new HashMap<>();
        possibleAttacks.put(whiteQueen, Arrays.asList(
                new Cell(4, 4)));
        Map<Cell, List<Cell>> possibleSteps = new HashMap<>();
        CommonFunctions.takeStockOfKillingBasedSteps(field,
                possibleAttacks,
                possibleSteps,
                analyzer::isGoodForNextQueenAttack);

        List<Cell> stepsActual = possibleSteps.get(whiteQueen);
        List<Cell> stepsExpected = new ArrayList(){
            {
                add(Cell.fromString("E, 4"));
                add(Cell.fromString("F, 3"));
            }
        };
        //todo: need to fix compareTo in Cell class
        assertTrue(stepsActual.containsAll(stepsExpected));
    }
}
