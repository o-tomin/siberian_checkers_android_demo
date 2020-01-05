package com.checkers;

import com.checkers.entity.Cell;
import com.checkers.entity.Field;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Test
public class GameTest {

    private static final String FIGURE_GO_GROUP = "figure-go";
    private static final String FIGURE_ATTACK_GROUP = "figure-attack";
    private static final String QUEEN_GO_GROUP = "queen-go";
    private static final String QUEEN_ATTACK_GROUP = "queen-attack";
    private static final String FIGURE_TURN_TO_QUEEN_GROUP = "figure-turn-to-queen";
    private static final String PUNISH_PLAYER_GROUP = "punish-player";
    private static final String GAME_END_GROUP = "game-end";

    private static final String FIGURE_SHOULD_GO_DATA_PROVIDER = "figure-should-go-data-provider";
    private static final String FIGURE_SHOULD_NOT_GO_DATA_PROVIDER = "figure-should-not-go-data-provider";
    private static final String FIGURE_SHOULD_ATTACK_DATA_PROVIDER = "figure-should-attack-data-provider";
    private static final String FIGURE_SHOULD_NOT_ATTACK_DATA_PROVIDER = "figure-should-not-attack-data-provider";
    private static final String FIGURE_SHOULD_CONTINUE_ATTACK_DATA_PROVIDER = "figure-should-continue-attack";
    private static final String QUEEN_SHOULD_GO_DATA_PROVIDER = "queen-should-go-data-provider";
    private static final String QUEEN_SHOULD_NOT_GO_DATA_PROVIDER = "queen-should-not-go-data-provider";
    private static final String QUEEN_SHOULD_ATTACK_DATA_PROVIDER = "queen-should-attack-data-provider";
    private static final String QUEEN_SHOULD_NOT_ATTACK_DATA_PROVIDER = "queen-should-not-attack-data-provider";
    private static final String FIGURE_SHOULD_TURN_TO_QUEEN_DATA_PROVIDER = "figure-should-turn-to-queen-data-provider";
    private static final String FIGURE_SHOULD_NOT_TURN_TO_QUEEN_DATA_PROVIDER = "figure-should-not-turn-to-queen-data-provider";
    private static final String PUNISH_PLAYER_DATA_PROVIDER = "punish-player-data-provider";
    private static final String GAME_SHOULD_END_DATA_PROVIDER = "game-should-end-data-provider";

    @DataProvider(name = FIGURE_SHOULD_GO_DATA_PROVIDER)
    public Object[][] figureShouldGoDataProvider() throws Exception {
        byte initialField[][] = new byte[][] {
               //1  2  3  4  5  6  7  8
                {2, 0, 2, 0, 1, 0, 4, 0}, //H
                {0, 2, 0, 1, 0, 4, 0, 4}, //G
                {2, 0, 2, 0, 1, 0, 4, 0}, //F
                {0, 2, 0, 1, 0, 4, 0, 4}, //E
                {2, 0, 2, 0, 1, 0, 4, 0}, //D
                {0, 2, 0, 1, 0, 4, 0, 4}, //C
                {2, 0, 2, 0, 1, 0, 4, 0}, //B
                {0, 2, 0, 1, 0, 4, 0, 4}}; //A
               //1  2  3  4  5  6  7  8
        return new Object[][] {
            {
                "White figure should go left forward.",
                true,
                initialField,
                Cell.fromString("D, 3"),
                Cell.fromString("E, 4")
            }, {
                "White figure should go right forward.",
                true,
                initialField,
                Cell.fromString("B, 3"),
                Cell.fromString("A, 4")
            }, {
                "Black figure should go left forward.",
                false,
                initialField,
                Cell.fromString("E, 6"),
                Cell.fromString("D, 5")
            }, {
                "Black figure should go right forward.",
                false,
                initialField,
                Cell.fromString("A, 6"),
                Cell.fromString("B, 5")
            }
        };
    }

    @DataProvider(name = FIGURE_SHOULD_NOT_GO_DATA_PROVIDER)
    public Object[][] figureShouldNotGoDataProvider() throws Exception {
        byte initialField[][] = new byte[][] {
               //1  2  3  4  5  6  7  8
                {1, 0, 1, 0, 1, 0, 1, 0}, //H
                {0, 4, 0, 2, 0, 1, 0, 1}, //G
                {1, 0, 4, 0, 4, 0, 2, 0}, //F
                {0, 5, 0, 3, 0, 1, 0, 1}, //E
                {1, 0, 1, 0, 4, 0, 2, 0}, //D
                {0, 1, 0, 2, 0, 2, 0, 1}, //C
                {2, 0, 1, 0, 5, 0, 3, 0}, //B
                {0, 2, 0, 1, 0, 1, 0, 1}}; //A
               //1  2  3  4  5  6  7  8
        return new Object[][] {
            {
                "White figure should not go left back.",
                true,
                initialField,
                Cell.fromString("F, 7"),
                Cell.fromString("G, 6")
            }, {
                "White figure should not go right back.",
                true,
                initialField,
                Cell.fromString("F, 7"),
                Cell.fromString("E, 6")
            }, {
                "White figure should not go when it is black's turn.",
                false,
                initialField,
                Cell.fromString("F, 7"),
                Cell.fromString("G, 8")
            },
            {
                "Black figure should not go left back.",
                false,
                initialField,
                Cell.fromString("G, 2"),
                Cell.fromString("F, 3")
            }, {
                "Black figure should not go right back.",
                false,
                initialField,
                Cell.fromString("G, 2"),
                Cell.fromString("H, 3")
            }, {
                "Black figure should not go when it is white's turn.",
                true,
                initialField,
                Cell.fromString("G, 2"),
                Cell.fromString("H, 1")
            },
            {
                "White figure should not go when blocked by white figure.",
                true,
                initialField,
                Cell.fromString("C, 6"),
                Cell.fromString("D, 7")
            }, {
                "White figure should not go when blocked by white queen.",
                true,
                initialField,
                Cell.fromString("C, 6"),
                Cell.fromString("B, 7")
            }, {
                "White figure should not go when blocked by black figure.",
                true,
                initialField,
                Cell.fromString("C, 4"),
                Cell.fromString("D, 5")
            }, {
                "White figure should not go when blocked by black queen.",
                true,
                initialField,
                Cell.fromString("C, 4"),
                Cell.fromString("B, 5")
            }, {
                "White figure should not go out of board.",
                true,
                initialField,
                new Cell(7, 1),
                new Cell(8, 2)
            }, {
                "White figure should not go on white cell.",
                true,
                initialField,
                Cell.fromString("A, 2"),
                Cell.fromString("A, 3")
            }, {
                "White figure should not go father then next closest cell.",
                true,
                initialField,
                Cell.fromString("B, 1"),
                Cell.fromString("D, 3")
            },
            {
                "Black figure should not go when blocked by white figure.",
                false,
                initialField,
                Cell.fromString("F, 5"),
                Cell.fromString("G, 4")
            }, {
                "Black figure should not go when blocked by white queen.",
                false,
                initialField,
                Cell.fromString("F, 5"),
                Cell.fromString("E, 4")
            }, {
                "Black figure should not go when blocked by black figure.",
                false,
                initialField,
                Cell.fromString("F, 3"),
                Cell.fromString("G, 2")
            }, {
                "Black figure should not go when blocked by black queen.",
                false,
                initialField,
                Cell.fromString("F, 3"),
                Cell.fromString("E, 2")
            }, {
                "Black figure should not go on white cell.",
                false,
                initialField,
                Cell.fromString("F, 3"),
                Cell.fromString("F, 4")
            }, {
                "Black figure should not go father then next closest cell.",
                false,
                initialField,
                Cell.fromString("F, 3"),
                Cell.fromString("H, 1")
            }
        };
    }

    @Test(groups = FIGURE_GO_GROUP, dataProvider = FIGURE_SHOULD_GO_DATA_PROVIDER)
    public void figureShouldGo(String message, boolean isWhitesTurn, byte[][] fieldInitialState, Cell from, Cell to) throws Exception {
        Game game = Game.newGame();
        game.getContext().getField().magicUpdate(fieldInitialState);
        game.getContext().setWhitesTurn(isWhitesTurn);
        game.getContext().getStateAnalyzer().updateDataForAnalysis();
        game.go(from, to);

        byte[][] expectedField = configureExpectedField(fieldInitialState,
                from.toIndexedCell(),
                Field.BLACK_CELL_CODE,
                to.toIndexedCell(),
                isWhitesTurn ? Field.WHITE_FIGURE_CODE : Field.BLACK_FIGURE_CODE);

        assertTrue(Arrays.deepEquals(expectedField, game.getField()), message);
        assertEquals(game.isWhitesTurn(), !isWhitesTurn, message);
    }

    @Test(groups = FIGURE_GO_GROUP, dataProvider = FIGURE_SHOULD_NOT_GO_DATA_PROVIDER)
    public void figureShouldNotGo(String message, boolean isWhitesTurn, byte[][] fieldInitialState, Cell from, Cell to) throws Exception {
        Game game = Game.newGame();
        game.getContext().getField().magicUpdate(fieldInitialState);
        game.getContext().setWhitesTurn(isWhitesTurn);
        game.getContext().getStateAnalyzer().updateDataForAnalysis();
        game.go(from, to);

        assertTrue(Arrays.deepEquals(fieldInitialState, game.getField()), message);
        assertEquals(game.isWhitesTurn(), isWhitesTurn, message);
    }

    @DataProvider(name = FIGURE_SHOULD_ATTACK_DATA_PROVIDER)
    public Object[][] figureShouldAttackDataProvider() throws Exception {
        byte whiteFigureAttackBlackFiguresField[][] = new byte[][] {
               //1  2  3  4  5  6  7  8
                {1, 0, 1, 0, 1, 0, 1, 0}, //H
                {0, 1, 0, 1, 0, 1, 0, 1}, //G
                {1, 0, 4, 0, 1, 0, 1, 0}, //F
                {0, 2, 0, 2, 0, 1, 0, 1}, //E
                {1, 0, 4, 0, 1, 0, 1, 0}, //D
                {0, 1, 0, 1, 0, 1, 0, 1}, //C
                {1, 0, 1, 0, 1, 0, 1, 0}, //B
                {0, 1, 0, 1, 0, 1, 0, 1}}; //A
               //1  2  3  4  5  6  7  8

        byte whiteFigureAttackBlackQueensField[][] = new byte[][] {
               //1  2  3  4  5  6  7  8
                {1, 0, 1, 0, 1, 0, 1, 0}, //H
                {0, 1, 0, 1, 0, 1, 0, 1}, //G
                {1, 0, 5, 0, 1, 0, 1, 0}, //F
                {0, 2, 0, 2, 0, 1, 0, 1}, //E
                {1, 0, 5, 0, 1, 0, 1, 0}, //D
                {0, 1, 0, 1, 0, 1, 0, 1}, //C
                {1, 0, 1, 0, 1, 0, 1, 0}, //B
                {0, 1, 0, 1, 0, 1, 0, 1}}; //A
               //1  2  3  4  5  6  7  8

        byte blackFigureAttackBlackFiguresField[][] = new byte[][] {
                //1  2  3  4  5  6  7  8
                {1, 0, 1, 0, 1, 0, 1, 0}, //H
                {0, 1, 0, 1, 0, 1, 0, 1}, //G
                {1, 0, 2, 0, 1, 0, 1, 0}, //F
                {0, 4, 0, 4, 0, 1, 0, 1}, //E
                {1, 0, 2, 0, 1, 0, 1, 0}, //D
                {0, 1, 0, 1, 0, 1, 0, 1}, //C
                {1, 0, 1, 0, 1, 0, 1, 0}, //B
                {0, 1, 0, 1, 0, 1, 0, 1}}; //A
                //1  2  3  4  5  6  7  8

        byte blackFigureAttackBlackQueensField[][] = new byte[][] {
                //1  2  3  4  5  6  7  8
                {1, 0, 1, 0, 1, 0, 1, 0}, //H
                {0, 1, 0, 1, 0, 1, 0, 1}, //G
                {1, 0, 3, 0, 1, 0, 1, 0}, //F
                {0, 4, 0, 4, 0, 1, 0, 1}, //E
                {1, 0, 3, 0, 1, 0, 1, 0}, //D
                {0, 1, 0, 1, 0, 1, 0, 1}, //C
                {1, 0, 1, 0, 1, 0, 1, 0}, //B
                {0, 1, 0, 1, 0, 1, 0, 1}}; //A
                //1  2  3  4  5  6  7  8

        return new Object[][] {
                {
                    "White figure should attack black figure forward right.",
                    true,
                    whiteFigureAttackBlackFiguresField,
                    Cell.fromString("E, 2"),
                    Cell.fromString("D, 3"),
                    Cell.fromString("C, 4")
                }, {
                    "White figure should attack black figure forward left.",
                    true,
                    whiteFigureAttackBlackFiguresField,
                    Cell.fromString("E, 2"),
                    Cell.fromString("F, 3"),
                    Cell.fromString("G, 4")
                }, {
                    "White figure should attack black figure backward right.",
                    true,
                    whiteFigureAttackBlackFiguresField,
                    Cell.fromString("E, 4"),
                    Cell.fromString("F, 3"),
                    Cell.fromString("G, 2")
                }, {
                    "White figure should attack black figure backward left.",
                    true,
                    whiteFigureAttackBlackFiguresField,
                    Cell.fromString("E, 4"),
                    Cell.fromString("D, 3"),
                    Cell.fromString("C, 2")
                },

                {
                    "White figure should attack black queen forward right.",
                    true,
                    whiteFigureAttackBlackQueensField,
                    Cell.fromString("E, 2"),
                    Cell.fromString("D, 3"),
                    Cell.fromString("C, 4")
                }, {
                    "White figure should attack black queen forward left.",
                    true,
                    whiteFigureAttackBlackQueensField,
                    Cell.fromString("E, 2"),
                    Cell.fromString("F, 3"),
                    Cell.fromString("G, 4")
                }, {
                    "White figure should attack black queen backward right.",
                    true,
                    whiteFigureAttackBlackQueensField,
                    Cell.fromString("E, 4"),
                    Cell.fromString("F, 3"),
                    Cell.fromString("G, 2")
                }, {
                    "White figure should attack black queen backward left.",
                    true,
                    whiteFigureAttackBlackQueensField,
                    Cell.fromString("E, 4"),
                    Cell.fromString("D, 3"),
                    Cell.fromString("C, 2")
                },

                {
                    "Black figure should attack white figure forward right.",
                    false,
                        blackFigureAttackBlackFiguresField,
                    Cell.fromString("E, 2"),
                    Cell.fromString("D, 3"),
                    Cell.fromString("C, 4")
                }, {
                    "Black figure should attack white figure forward left.",
                false,
                blackFigureAttackBlackFiguresField,
                    Cell.fromString("E, 2"),
                    Cell.fromString("F, 3"),
                    Cell.fromString("G, 4")
                }, {
                    "Black figure should attack white figure backward right.",
                false,
                blackFigureAttackBlackFiguresField,
                    Cell.fromString("E, 4"),
                    Cell.fromString("F, 3"),
                    Cell.fromString("G, 2")
                }, {
                    "Black figure should attack white figure backward left.",
                false,
                blackFigureAttackBlackFiguresField,
                    Cell.fromString("E, 4"),
                    Cell.fromString("D, 3"),
                    Cell.fromString("C, 2")
                },

                {
                    "Black figure should attack white queen forward right.",
                        false,
                    blackFigureAttackBlackQueensField,
                    Cell.fromString("E, 2"),
                    Cell.fromString("D, 3"),
                    Cell.fromString("C, 4")
                }, {
                    "Black figure should attack white queen forward left.",
                false,
                    blackFigureAttackBlackQueensField,
                    Cell.fromString("E, 2"),
                    Cell.fromString("F, 3"),
                    Cell.fromString("G, 4")
                }, {
                    "Black figure should attack white queen backward right.",
                false,
                    blackFigureAttackBlackQueensField,
                    Cell.fromString("E, 4"),
                    Cell.fromString("F, 3"),
                    Cell.fromString("G, 2")
                }, {
                    "Black figure should attack white queen backward left.",
                false,
                    blackFigureAttackBlackQueensField,
                    Cell.fromString("E, 4"),
                    Cell.fromString("D, 3"),
                    Cell.fromString("C, 2")
                }
        };
    }

    @Test(groups = FIGURE_ATTACK_GROUP, dataProvider = FIGURE_SHOULD_ATTACK_DATA_PROVIDER)
    public void figureShouldAttack(String message,
                                   boolean isWhitesTurn,
                                   byte[][] fieldInitialState,
                                   Cell from,
                                   Cell toAttack,
                                   Cell to) throws Exception {
        Game game = Game.newGame();
        game.getContext().getField().magicUpdate(fieldInitialState);
        game.getContext().setWhitesTurn(isWhitesTurn);
        game.getContext().getStateAnalyzer().updateDataForAnalysis();
        game.go(from, to);

        byte[][] expectedField = configureExpectedField(fieldInitialState,
                from.toIndexedCell(),
                Field.BLACK_CELL_CODE,
                toAttack.toIndexedCell(),
                Field.BLACK_CELL_CODE,
                to.toIndexedCell(),
                isWhitesTurn ? Field.WHITE_FIGURE_CODE : Field.BLACK_FIGURE_CODE);

        assertTrue(Arrays.deepEquals(expectedField, game.getField()), message);//todo: print fields normally
        assertEquals(game.isWhitesTurn(), !isWhitesTurn, message);
    }

    @DataProvider(name = FIGURE_SHOULD_NOT_ATTACK_DATA_PROVIDER)
    public Object[][] figureShouldNotAttackDataProvider() throws Exception {

        return new Object[][] {
                {
                    "White figure should not attack blacks when no space after.",
                    true,
                    new byte[][] {
                         //1  2  3  4  5  6  7  8
                          {1, 0, 1, 0, 1, 0, 1, 0},  //H
                          {0, 1, 0, 1, 0, 1, 0, 1},  //G
                          {1, 0, 4, 0, 1, 0, 5, 0},  //F
                          {0, 1, 0, 4, 0, 1, 0, 1},  //E
                          {1, 0, 1, 0, 2, 0, 1, 0},  //D
                          {0, 1, 0, 5, 0, 4, 0, 1},  //C
                          {1, 0, 4, 0, 1, 0, 5, 0},  //B
                          {0, 1, 0, 1, 0, 1, 0, 1}}, //A
                         //1  2  3  4  5  6  7  8
                    Cell.fromString("D, 5"),
                    Cell.fromStrings("C, 6", "C, 4", "E, 4"),
                },
                {
                    "White figure should not attack white figures.",
                    true,
                    new byte[][] {
                         //1  2  3  4  5  6  7  8
                          {1, 0, 1, 0, 1, 0, 1, 0},  //H
                          {0, 1, 0, 1, 0, 1, 0, 1},  //G
                          {1, 0, 4, 0, 1, 0, 5, 0},  //F
                          {0, 1, 0, 2, 0, 3, 0, 1},  //E
                          {1, 0, 1, 0, 2, 0, 1, 0},  //D
                          {0, 1, 0, 2, 0, 1, 0, 1},  //C
                          {1, 0, 4, 0, 1, 0, 5, 0},  //B
                          {0, 1, 0, 1, 0, 1, 0, 1}}, //A
                         //1  2  3  4  5  6  7  8
                    Cell.fromString("D, 5"),
                    Cell.fromStrings("E, 4", "E, 6", "C, 4")
                },
                {
                    "Black figure should not attack whites when no space after.",
                    true,
                    new byte[][] {
                        //1  2  3  4  5  6  7  8
                         {1, 0, 1, 0, 1, 0, 1, 0},  //H
                         {0, 1, 0, 1, 0, 1, 0, 1},  //G
                         {1, 0, 3, 0, 1, 0, 2, 0},  //F
                         {0, 1, 0, 2, 0, 1, 0, 1},  //E
                         {1, 0, 1, 0, 4, 0, 1, 0},  //D
                         {0, 1, 0, 2, 0, 3, 0, 1},  //C
                         {1, 0, 3, 0, 1, 0, 2, 0},  //B
                         {0, 1, 0, 1, 0, 1, 0, 1}}, //A
                        //1  2  3  4  5  6  7  8
                    Cell.fromString("D, 5"),
                    Cell.fromStrings("E, 4", "C, 4", "C, 6")
                },
                {
                    "Black figure should not attack blacks when no space after.",
                    true,
                    new byte[][] {
                        //1  2  3  4  5  6  7  8
                         {1, 0, 1, 0, 1, 0, 1, 0},  //H
                         {0, 1, 0, 1, 0, 1, 0, 1},  //G
                         {1, 0, 4, 0, 1, 0, 5, 0},  //F
                         {0, 1, 0, 4, 0, 5, 0, 1},  //E
                         {1, 0, 1, 0, 4, 0, 1, 0},  //D
                         {0, 1, 0, 5, 0, 1, 0, 1},  //C
                         {1, 0, 4, 0, 1, 0, 5, 0},  //B
                         {0, 1, 0, 1, 0, 1, 0, 1}}, //A
                        //1  2  3  4  5  6  7  8
                    Cell.fromString("D, 5"),
                    Cell.fromStrings("E, 4", "E, 6", "C, 4")
                }
        };
    }

    @Test(groups = FIGURE_ATTACK_GROUP, dataProvider = FIGURE_SHOULD_NOT_ATTACK_DATA_PROVIDER)
    public void figureShouldNotAttack(String message,
                                      boolean isWhitesTurn,
                                      byte[][] fieldInitialState,
                                      Cell from,
                                      List<Cell> toList) throws Exception {
        Game game = Game.newGame();
        game.getContext().getField().magicUpdate(fieldInitialState);
        game.getContext().setWhitesTurn(isWhitesTurn);
        game.getContext().getStateAnalyzer().updateDataForAnalysis();
        for (Cell to: toList) {
            game.go(from, to);
        }
        assertTrue(Arrays.deepEquals(fieldInitialState, game.getField()), message);//todo: print fields normally
        assertEquals(game.isWhitesTurn(), isWhitesTurn, message);
    }



    @DataProvider(name = FIGURE_SHOULD_CONTINUE_ATTACK_DATA_PROVIDER)
    public Object[][] figureShouldContinueAttackDataProvider() throws Exception {
        return new Object[][] {
                {
                    "Turn should not be switched when white figure can attack one more time",
                    true,
                    new byte[][] {
                           //1  2  3  4  5  6  7  8
                            {1, 0, 1, 0, 1, 0, 1, 0},  //H
                            {0, 1, 0, 1, 0, 1, 0, 1},  //G
                            {1, 0, 1, 0, 4, 0, 5, 0},  //F
                            {0, 1, 0, 2, 0, 1, 0, 1},  //E
                            {1, 0, 1, 0, 5, 0, 4, 0},  //D
                            {0, 1, 0, 1, 0, 1, 0, 1},  //C
                            {1, 0, 1, 0, 1, 0, 1, 0},  //B
                            {0, 1, 0, 1, 0, 1, 0, 1}}, //A
                           //1  2  3  4  5  6  7  8
                    Cell.fromString("E, 4"),
                    Cell.fromString("G, 6"),
                    Cell.fromString("F, 7")
                },
                {
                    "Turn should not be switched when white figure can attack one more time",
                    true,
                    new byte[][] {
                           //1  2  3  4  5  6  7  8
                            {1, 0, 1, 0, 1, 0, 1, 0},  //H
                            {0, 1, 0, 1, 0, 1, 0, 1},  //G
                            {1, 0, 1, 0, 4, 0, 5, 0},  //F
                            {0, 1, 0, 2, 0, 1, 0, 1},  //E
                            {1, 0, 1, 0, 5, 0, 4, 0},  //D
                            {0, 1, 0, 1, 0, 1, 0, 1},  //C
                            {1, 0, 1, 0, 1, 0, 1, 0},  //B
                            {0, 1, 0, 1, 0, 1, 0, 1}}, //A
                           //1  2  3  4  5  6  7  8
                    Cell.fromString("E, 4"),
                    Cell.fromString("C, 6"),
                    Cell.fromString("D, 7")
                },
                {
                    "Turn should not be switched when white figure can attack one more time",
                    true,
                    new byte[][] {
                       //1  2  3  4  5  6  7  8
                        {1, 0, 1, 0, 1, 0, 1, 0},  //H
                        {0, 1, 0, 1, 0, 1, 0, 1},  //G
                        {1, 0, 1, 0, 4, 0, 5, 0},  //F
                        {0, 1, 0, 1, 0, 1, 0, 2},  //E
                        {1, 0, 1, 0, 5, 0, 4, 0},  //D
                        {0, 1, 0, 1, 0, 1, 0, 1},  //C
                        {1, 0, 1, 0, 1, 0, 1, 0},  //B
                        {0, 1, 0, 1, 0, 1, 0, 1}}, //A
                       //1  2  3  4  5  6  7  8,
                    Cell.fromString("E, 8"),
                    Cell.fromString("C, 6"),
                    Cell.fromString("D, 5")
                },
                {
                    "Turn should not be switched when white figure can attack one more time",
                    true,
                    new byte[][] {
                           //1  2  3  4  5  6  7  8
                            {1, 0, 1, 0, 1, 0, 1, 0},  //H
                            {0, 1, 0, 1, 0, 1, 0, 1},  //G
                            {1, 0, 1, 0, 4, 0, 5, 0},  //F
                            {0, 1, 0, 1, 0, 1, 0, 2},  //E
                            {1, 0, 1, 0, 5, 0, 4, 0},  //D
                            {0, 1, 0, 1, 0, 1, 0, 1},  //C
                            {1, 0, 1, 0, 1, 0, 1, 0},  //B
                            {0, 1, 0, 1, 0, 1, 0, 1}}, //A
                           //1  2  3  4  5  6  7  8,
                    Cell.fromString("E, 8"),
                    Cell.fromString("G, 6"),
                    Cell.fromString("F, 5")
                },

                {
                        "Turn should not be switched when black figure can attack one more time",
                        false,
                        new byte[][] {
                       //1  2  3  4  5  6  7  8
                        {1, 0, 1, 0, 1, 0, 1, 0},  //H
                        {0, 1, 0, 1, 0, 1, 0, 1},  //G
                        {1, 0, 1, 0, 2, 0, 3, 0},  //F
                        {0, 1, 0, 4, 0, 1, 0, 1},  //E
                        {1, 0, 1, 0, 3, 0, 2, 0},  //D
                        {0, 1, 0, 1, 0, 1, 0, 1},  //C
                        {1, 0, 1, 0, 1, 0, 1, 0},  //B
                        {0, 1, 0, 1, 0, 1, 0, 1}}, //A
                       //1  2  3  4  5  6  7  8
                        Cell.fromString("E, 4"),
                        Cell.fromString("C, 6"),
                        Cell.fromString("D, 7")
                },
                {
                        "Turn should not be switched when black figure can attack one more time",
                        false,
                        new byte[][] {
                        //1  2  3  4  5  6  7  8
                         {1, 0, 1, 0, 1, 0, 1, 0},  //H
                         {0, 1, 0, 1, 0, 1, 0, 1},  //G
                         {1, 0, 1, 0, 2, 0, 3, 0},  //F
                         {0, 1, 0, 4, 0, 1, 0, 1},  //E
                         {1, 0, 1, 0, 3, 0, 2, 0},  //D
                         {0, 1, 0, 1, 0, 1, 0, 1},  //C
                         {1, 0, 1, 0, 1, 0, 1, 0},  //B
                         {0, 1, 0, 1, 0, 1, 0, 1}}, //A
                        //1  2  3  4  5  6  7  8
                        Cell.fromString("E, 4"),
                        Cell.fromString("G, 6"),
                        Cell.fromString("F, 7")
                },
                {
                        "Turn should not be switched when black figure can attack one more time",
                        false,
                        new byte[][] {
                       //1  2  3  4  5  6  7  8
                        {1, 0, 1, 0, 1, 0, 1, 0},  //H
                        {0, 1, 0, 1, 0, 1, 0, 1},  //G
                        {1, 0, 1, 0, 3, 0, 3, 0},  //F
                        {0, 1, 0, 1, 0, 1, 0, 4},  //E
                        {1, 0, 1, 0, 2, 0, 2, 0},  //D
                        {0, 1, 0, 1, 0, 1, 0, 1},  //C
                        {1, 0, 1, 0, 1, 0, 1, 0},  //B
                        {0, 1, 0, 1, 0, 1, 0, 1}}, //A
                       //1  2  3  4  5  6  7  8
                        Cell.fromString("E, 8"),
                        Cell.fromString("G, 6"),
                        Cell.fromString("F, 5")
                },
                {
                        "Turn should not be switched when black figure can attack one more time",
                        false,
                        new byte[][] {
                        //1  2  3  4  5  6  7  8
                         {1, 0, 1, 0, 1, 0, 1, 0},  //H
                         {0, 1, 0, 1, 0, 1, 0, 1},  //G
                         {1, 0, 1, 0, 3, 0, 3, 0},  //F
                         {0, 1, 0, 1, 0, 1, 0, 4},  //E
                         {1, 0, 1, 0, 2, 0, 2, 0},  //D
                         {0, 1, 0, 1, 0, 1, 0, 1},  //C
                         {1, 0, 1, 0, 1, 0, 1, 0},  //B
                         {0, 1, 0, 1, 0, 1, 0, 1}}, //A
                        //1  2  3  4  5  6  7  8
                        Cell.fromString("E, 8"),
                        Cell.fromString("C, 6"),
                        Cell.fromString("D, 5")
                }
        };
    }

    @Test(groups = FIGURE_ATTACK_GROUP, dataProvider = FIGURE_SHOULD_CONTINUE_ATTACK_DATA_PROVIDER)
    public void figureShouldContinueAttackDataProviderTest(String message,
                                                           boolean isWhitesTurn,
                                                           byte[][] fieldInitialState,
                                                           Cell from,
                                                           Cell to,
                                                           Cell nextVictim) throws Exception {
        Game game = Game.newGame();
        game.getContext().getField().magicUpdate(fieldInitialState);
        game.getContext().setWhitesTurn(isWhitesTurn);
        game.getContext().getStateAnalyzer().updateDataForAnalysis();

        game.go(from, to);

        Map<Cell, List<Cell>> possibleAttacks = isWhitesTurn ? game.getWhitesPossibleAttacks() : game.getBlacksPossibleAttacks();

        Assert.assertEquals(possibleAttacks.size(), 1);
        Assert.assertEquals(possibleAttacks.get(to).get(0), nextVictim);
        Assert.assertEquals(game.isWhitesTurn(), isWhitesTurn);
    }

    @DataProvider(name = QUEEN_SHOULD_GO_DATA_PROVIDER)
    public Object[][] queenShouldGoDataProvider() throws Exception {
        return new Object[][] {
                {
                    "White queen should go diagonally on allowed cells.",
                    true,
                    new byte[][] {
                        //1  2  3  4  5  6  7  8
                         {5, 0, 1, 0, 1, 0, 2, 0},  //H
                         {0, 5, 0, 1, 0, 1, 0, 1},  //G
                         {1, 0, 1, 0, 1, 0, 1, 0},  //F
                         {0, 1, 0, 3, 0, 1, 0, 1},  //E
                         {1, 0, 4, 0, 1, 0, 1, 0},  //D
                         {0, 4, 0, 1, 0, 3, 0, 1},  //C
                         {1, 0, 1, 0, 1, 0, 1, 0},  //B
                         {0, 1, 0, 1, 0, 1, 0, 1}}, //A
                        //1  2  3  4  5  6  7  8
                     Cell.fromString("E, 4"),
                     Cell.fromStrings("F, 3", "F, 5", "G, 6", "D, 5")
                },
                {
                    "Black queen should go diagonally on allowed cells.",
                    false,
                    new byte[][] {
                       //1  2  3  4  5  6  7  8
                        {1, 0, 1, 0, 1, 0, 1, 0},  //H
                        {0, 1, 0, 1, 0, 1, 0, 1},  //G
                        {1, 0, 1, 0, 1, 0, 1, 0},  //F
                        {0, 1, 0, 1, 0, 1, 0, 1},  //E
                        {1, 0, 5, 0, 1, 0, 1, 0},  //D
                        {0, 3, 0, 4, 0, 1, 0, 1},  //C
                        {3, 0, 1, 0, 1, 0, 1, 0},  //B
                        {0, 1, 0, 1, 0, 1, 0, 1}}, //A
                       //1  2  3  4  5  6  7  8
                    Cell.fromString("D, 3"),
                    Cell.fromStrings("E, 2", "F, 1", "E, 4", "F, 5", "G, 6", "H, 7")
                }
        };
    }

    @Test(groups = QUEEN_GO_GROUP, dataProvider = QUEEN_SHOULD_GO_DATA_PROVIDER)
    public void queenShouldGo(String message, boolean isWhitesTurn, byte[][] fieldInitialState, Cell from, List<Cell> toList) throws Exception {
        for (Cell to : toList) {
            Game game = Game.newGame();
            game.getContext().getField().magicUpdate(fieldInitialState);
            game.getContext().setWhitesTurn(isWhitesTurn);
            game.getContext().getStateAnalyzer().updateDataForAnalysis();
            game.go(from, to);

            byte[][] expectedField = configureExpectedField(fieldInitialState,
                    from.toIndexedCell(),
                    Field.BLACK_CELL_CODE,
                    to.toIndexedCell(),
                    isWhitesTurn ? Field.WHITE_QUEEN_CODE : Field.BLACK_QUEEN_CODE);

            assertTrue(Arrays.deepEquals(expectedField, game.getField()), message);
            assertEquals(game.isWhitesTurn(), !isWhitesTurn, message);
        }
    }

    @DataProvider(name = QUEEN_SHOULD_NOT_GO_DATA_PROVIDER)
    public Object[][] queenShouldNotGoDataProvider() throws Exception {
        return new Object[][] {
                {
                        "White queen should not go diagonally on cells blocked or occupied by any other figures.",
                        true,
                        new byte[][] {
                               //1  2  3  4  5  6  7  8
                                {5, 0, 1, 0, 1, 0, 2, 0},  //H
                                {0, 5, 0, 1, 0, 1, 0, 1},  //G
                                {1, 0, 1, 0, 1, 0, 1, 0},  //F
                                {0, 1, 0, 3, 0, 1, 0, 1},  //E
                                {1, 0, 4, 0, 1, 0, 1, 0},  //D
                                {0, 4, 0, 1, 0, 3, 0, 1},  //C
                                {1, 0, 1, 0, 1, 0, 1, 0},  //B
                                {0, 1, 0, 1, 0, 1, 0, 1}}, //A
                               //1  2  3  4  5  6  7  8
                        Cell.fromString("E, 4"),
                        Cell.fromStrings("H, 7", "C, 6", "B, 7", "A, 8", "D, 3", "C, 2", "B, 1", "G, 2", "H, 1")
                },
                {
                        "Black queen should go diagonally on allowed cells.",
                        false,
                        new byte[][] {
                               //1  2  3  4  5  6  7  8
                                {1, 0, 1, 0, 1, 0, 1, 0},  //H
                                {0, 1, 0, 1, 0, 1, 0, 1},  //G
                                {1, 0, 1, 0, 1, 0, 1, 0},  //F
                                {0, 1, 0, 1, 0, 1, 0, 1},  //E
                                {1, 0, 5, 0, 1, 0, 1, 0},  //D
                                {0, 3, 0, 4, 0, 1, 0, 1},  //C
                                {3, 0, 1, 0, 1, 0, 1, 0},  //B
                                {0, 1, 0, 1, 0, 1, 0, 1}}, //A
                               //1  2  3  4  5  6  7  8
                        Cell.fromString("D, 3"),
                        Cell.fromStrings("C, 2", "B, 1", "C, 4", "B, 5", "A, 6")
                }
        };
    }

    @Test(groups = QUEEN_GO_GROUP, dataProvider = QUEEN_SHOULD_NOT_GO_DATA_PROVIDER)
    public void queenShouldNotGo(String message, boolean isWhitesTurn, byte[][] fieldInitialState, Cell from, List<Cell> toList) throws Exception {
        for (Cell to : toList) {
            Game game = Game.newGame();
            game.getContext().getField().magicUpdate(fieldInitialState);
            game.getContext().setWhitesTurn(isWhitesTurn);
            game.getContext().getStateAnalyzer().updateDataForAnalysis();
            game.go(from, to);

            assertTrue(Arrays.deepEquals(fieldInitialState, game.getField()), message);
            assertEquals(game.isWhitesTurn(), isWhitesTurn, message);
        }
    }

    @DataProvider(name = QUEEN_SHOULD_ATTACK_DATA_PROVIDER)
    public Object[][] queenShouldAttackDataProvider() throws Exception {
        return new Object[][] {
                {
                    "White queen should attack black figures.",
                    true,
                    new byte[][] {
                       //1  2  3  4  5  6  7  8
                        {1, 0, 1, 0, 1, 0, 1, 0},  //H
                        {0, 4, 0, 1, 0, 1, 0, 5},  //G
                        {1, 0, 1, 0, 1, 0, 1, 0},  //F
                        {0, 1, 0, 1, 0, 1, 0, 1},  //E
                        {1, 0, 1, 0, 3, 0, 1, 0},  //D
                        {0, 1, 0, 1, 0, 4, 0, 1},  //C
                        {1, 0, 5, 0, 1, 0, 1, 0},  //B
                        {0, 1, 0, 1, 0, 1, 0, 1}}, //A
                       //1  2  3  4  5  6  7  8
                    Cell.fromString("D, 5"),
                    Cell.fromStrings("G, 2", "B, 3", "C, 6", "C, 6"),
                    Cell.fromStrings("H, 1", "A, 2", "B, 7", "A, 8")
                },
                {
                    "Black queen should attack white figures.",
                    false,
                    new byte[][] {
                       //1  2  3  4  5  6  7  8
                        {1, 0, 1, 0, 1, 0, 1, 0},  //H
                        {0, 1, 0, 3, 0, 1, 0, 3},  //G
                        {1, 0, 1, 0, 1, 0, 1, 0},  //F
                        {0, 1, 0, 1, 0, 5, 0, 1},  //E
                        {1, 0, 1, 0, 1, 0, 2, 0},  //D
                        {0, 1, 0, 1, 0, 1, 0, 1},  //C
                        {1, 0, 2, 0, 1, 0, 1, 0},  //B
                        {0, 1, 0, 1, 0, 1, 0, 1}}, //A
                       //1  2  3  4  5  6  7  8
                    Cell.fromString("E, 6"),
                    Cell.fromStrings("B, 3", "D, 7", "G, 4"),
                    Cell.fromStrings("A, 2", "C, 8", "H, 3")
                }
        };
    }

    @Test(groups = QUEEN_ATTACK_GROUP, dataProvider = QUEEN_SHOULD_ATTACK_DATA_PROVIDER)
    public void queenShouldAttack(String message,
                                  boolean isWhitesTurn,
                                  byte[][] fieldInitialState,
                                  Cell from,
                                  List<Cell> victimsList,
                                  List<Cell> toList) throws Exception {
        for (int i = 0; i < victimsList.size(); i++) {
            Cell victim = victimsList.get(i);
            Cell to = toList.get(i);
            Game game = Game.newGame();
            game.getContext().getField().magicUpdate(fieldInitialState);
            game.getContext().setWhitesTurn(isWhitesTurn);
            game.getContext().getStateAnalyzer().updateDataForAnalysis();
            game.go(from, to);

            byte[][] expectedField = configureExpectedField(fieldInitialState,
                    from.toIndexedCell(),
                    Field.BLACK_CELL_CODE,
                    victim.toIndexedCell(),
                    Field.BLACK_CELL_CODE,
                    to.toIndexedCell(),
                    isWhitesTurn ? Field.WHITE_QUEEN_CODE : Field.BLACK_QUEEN_CODE);

            assertTrue(Arrays.deepEquals(game.getField(), expectedField), message);
            boolean shouldSwitchTurn = isWhitesTurn ? game.getWhitesPossibleAttacks().getOrDefault(to, new ArrayList<>()).size() == 0 :
                    game.getBlacksPossibleAttacks().getOrDefault(to, new ArrayList<>()).size() == 0;
            assertEquals(game.isWhitesTurn(), shouldSwitchTurn != isWhitesTurn, message);
        }
    }

    @DataProvider(name = QUEEN_SHOULD_NOT_ATTACK_DATA_PROVIDER)
    public Object[][] queenShouldNotAttackDataProvider() throws Exception {
        return new Object[][] {
                {
                        "White queen should not attack black figures if path is blocked.",
                        true,
                        new byte[][] {
                               //1  2  3  4  5  6  7  8
                                {5, 0, 1, 0, 1, 0, 4, 0},  //H
                                {0, 1, 0, 1, 0, 1, 0, 1},  //G
                                {1, 0, 1, 0, 1, 0, 1, 0},  //F
                                {0, 1, 0, 3, 0, 1, 0, 1},  //E
                                {1, 0, 1, 0, 4, 0, 1, 0},  //D
                                {0, 5, 0, 1, 0, 1, 0, 1},  //C
                                {4, 0, 1, 0, 1, 0, 4, 0},  //B
                                {0, 1, 0, 1, 0, 1, 0, 1}}, //A
                               //1  2  3  4  5  6  7  8
                        Cell.fromString("E, 4"),
                        Cell.fromStrings("A, 8", "B, 1")
                },
                {
                        "Black queen should not attack black figures if path is blocked.",
                        false,
                        new byte[][] {
                               //1  2  3  4  5  6  7  8
                                {1, 0, 1, 0, 1, 0, 1, 0},  //H
                                {0, 3, 0, 1, 0, 1, 0, 3},  //G
                                {1, 0, 1, 0, 1, 0, 1, 0},  //F
                                {0, 1, 0, 2, 0, 1, 0, 1},  //E
                                {1, 0, 1, 0, 5, 0, 1, 0},  //D
                                {0, 1, 0, 1, 0, 1, 0, 1},  //C
                                {1, 0, 3, 0, 1, 0, 2, 0},  //B
                                {0, 3, 0, 1, 0, 1, 0, 2}}, //A
                               //1  2  3  4  5  6  7  8
                        Cell.fromString("E, 6"),
                        Cell.fromStrings("H, 1", "A, 8", "A, 2")
                }
        };
    }

    @Test(groups = QUEEN_ATTACK_GROUP, dataProvider = QUEEN_SHOULD_NOT_ATTACK_DATA_PROVIDER)
    public void queenShouldNotAttack(String message, boolean isWhitesTurn, byte[][] fieldInitialState, Cell from, List<Cell> toList) throws Exception {
        for (Cell to : toList) {
            Game game = Game.newGame();
            game.getContext().getField().magicUpdate(fieldInitialState);
            game.getContext().setWhitesTurn(isWhitesTurn);
            game.getContext().getStateAnalyzer().updateDataForAnalysis();
            game.go(from, to);

            assertTrue(Arrays.deepEquals(fieldInitialState, game.getField()), message);
            assertEquals(game.isWhitesTurn(), isWhitesTurn, message);
        }
    }

    @DataProvider(name = FIGURE_SHOULD_TURN_TO_QUEEN_DATA_PROVIDER)
    public Object[][] figureShouldTurnToQueenDataProvider() throws Exception {
        return new Object[][] {
                {
                    "White figure should turn to queen when rich opposite side of the field.",
                        true,
                        new byte[][] {
                               //1  2  3  4  5  6  7  8
                                {1, 0, 1, 0, 1, 0, 2, 0},  //H
                                {0, 1, 0, 1, 0, 1, 0, 1},  //G
                                {1, 0, 1, 0, 1, 0, 2, 0},  //F
                                {0, 1, 0, 1, 0, 1, 0, 1},  //E
                                {1, 0, 1, 0, 1, 0, 2, 0},  //D
                                {0, 1, 0, 1, 0, 1, 0, 1},  //C
                                {1, 0, 1, 0, 1, 0, 2, 0},  //B
                                {0, 1, 0, 1, 0, 1, 0, 1}}, //A
                               //1  2  3  4  5  6  7  8
                        Cell.fromStrings("H, 7", "F, 7", "D, 7", "B, 7"),
                        Cell.fromStrings("G, 8", "E, 8", "C, 8", "A, 8")
                },
                {
                    "Black figure should turn to queen when rich opposite side of the field.",
                        false,
                        new byte[][] {
                               //1  2  3  4  5  6  7  8
                                {1, 0, 1, 0, 1, 0, 1, 0},  //H
                                {0, 4, 0, 1, 0, 1, 0, 1},  //G
                                {1, 0, 1, 0, 1, 0, 1, 0},  //F
                                {0, 4, 0, 1, 0, 1, 0, 1},  //E
                                {1, 0, 1, 0, 1, 0, 1, 0},  //D
                                {0, 4, 0, 1, 0, 1, 0, 1},  //C
                                {1, 0, 1, 0, 1, 0, 1, 0},  //B
                                {0, 4, 0, 1, 0, 1, 0, 1}}, //A
                               //1  2  3  4  5  6  7  8
                        Cell.fromStrings("G, 2", "E, 2", "C, 2", "A, 2"),
                        Cell.fromStrings("H, 1", "F, 1", "D, 1", "B, 1")
                },
        };
    }

    @Test(groups = FIGURE_TURN_TO_QUEEN_GROUP, dataProvider = FIGURE_SHOULD_TURN_TO_QUEEN_DATA_PROVIDER)
    public void figureShouldTurnToQueen(String message,
                                        boolean isWhitesTurn,
                                        byte[][] fieldInitialState,
                                        List<Cell> fromList,
                                        List<Cell> toList) throws Exception {
        for (int i = 0; i < fromList.size(); i++) {
            Cell from = fromList.get(i);
            Cell to = toList.get(i);
            Game game = Game.newGame();
            game.getContext().getField().magicUpdate(fieldInitialState);
            game.getContext().setWhitesTurn(isWhitesTurn);
            game.getContext().getStateAnalyzer().updateDataForAnalysis();
            game.go(from, to);

            byte[][] expectedField = configureExpectedField(fieldInitialState,
                    from.toIndexedCell(),
                    Field.BLACK_CELL_CODE,
                    to.toIndexedCell(),
                    isWhitesTurn ? Field.WHITE_QUEEN_CODE : Field.BLACK_QUEEN_CODE);

            assertTrue(Arrays.deepEquals(game.getField(), expectedField), message);
            assertTrue(isWhitesTurn ? game.isWhitesWin() : game.isBlacksWin(), message);
        }
    }

    @DataProvider(name = FIGURE_SHOULD_NOT_TURN_TO_QUEEN_DATA_PROVIDER)
    public Object[][] figureShouldNotTurnToQueenDataProvider() throws Exception {
        return new Object[][] {
                {
                    "White queen should not turn to white queen again & lost victims.",
                        true,
                        new byte[][] {
                               //1  2  3  4  5  6  7  8
                                {1, 0, 1, 0, 1, 0, 1, 0},  //H
                                {0, 1, 0, 1, 0, 1, 0, 1},  //G
                                {1, 0, 1, 0, 1, 0, 1, 0},  //F
                                {0, 1, 0, 1, 0, 3, 0, 1},  //E
                                {1, 0, 1, 0, 1, 0, 4, 0},  //D
                                {0, 1, 0, 1, 0, 1, 0, 1},  //C
                                {1, 0, 1, 0, 1, 0, 5, 0},  //B
                                {0, 1, 0, 1, 0, 1, 0, 1}}, //A
                               //1  2  3  4  5  6  7  8
                        Cell.fromString("E, 6"),
                        Cell.fromString("D, 7"),
                        Cell.fromString("C, 8"),
                        Cell.fromString("B, 7")
                },
                {
                    "Black queen should not turn to black queen again & lost victims",
                        false,
                        new byte[][] {
                               //1  2  3  4  5  6  7  8
                                {1, 0, 1, 0, 1, 0, 1, 0},  //H
                                {0, 1, 0, 1, 0, 1, 0, 1},  //G
                                {1, 0, 1, 0, 1, 0, 1, 0},  //F
                                {0, 2, 0, 1, 0, 1, 0, 1},  //E
                                {1, 0, 1, 0, 1, 0, 1, 0},  //D
                                {0, 3, 0, 1, 0, 1, 0, 1},  //C
                                {1, 0, 5, 0, 1, 0, 1, 0},  //B
                                {0, 1, 0, 1, 0, 1, 0, 1}}, //A
                               //1  2  3  4  5  6  7  8
                        Cell.fromString("B, 3"),
                        Cell.fromString("C, 2"),
                        Cell.fromString("D, 1"),
                        Cell.fromString("E, 2")
                }
        };
    }

    @Test(groups = FIGURE_TURN_TO_QUEEN_GROUP, dataProvider = FIGURE_SHOULD_NOT_TURN_TO_QUEEN_DATA_PROVIDER)
    public void figureShouldNotTurnToQueen(String message,
                                           boolean isWhitesTurn,
                                           byte[][] fieldInitialState,
                                           Cell from,
                                           Cell firstVictim,
                                           Cell to,
                                           Cell nextVictim) throws Exception {
        Game game = Game.newGame();
        game.getContext().getField().magicUpdate(fieldInitialState);
        game.getContext().setWhitesTurn(isWhitesTurn);
        game.getContext().getStateAnalyzer().updateDataForAnalysis();
        game.go(from, to);

        byte[][] expectedField = configureExpectedField(fieldInitialState,
                from.toIndexedCell(),
                Field.BLACK_CELL_CODE,
                firstVictim.toIndexedCell(),
                Field.BLACK_CELL_CODE,
                to.toIndexedCell(),
                isWhitesTurn ? Field.WHITE_QUEEN_CODE : Field.BLACK_QUEEN_CODE);

        assertTrue(Arrays.deepEquals(game.getField(), expectedField), message);

        List<Cell> victimList = isWhitesTurn ? game.getWhitesPossibleAttacks().get(to) :
                game.getBlacksPossibleAttacks().get(to);

        assertEquals(victimList.get(0), nextVictim, message);
        assertEquals(game.isWhitesTurn(), isWhitesTurn, message);
    }

    @DataProvider(name = GAME_SHOULD_END_DATA_PROVIDER)
    public Object[][] gameShouldEndDataProvider() throws Exception {
        return new Object[][] {
                {
                        "Game should end if only white figures left on the board.",
                        true,
                        new byte[][] {
                               //1  2  3  4  5  6  7  8
                                {1, 0, 1, 0, 1, 0, 1, 0},  //H
                                {0, 1, 0, 1, 0, 4, 0, 1},  //G
                                {1, 0, 1, 0, 2, 0, 1, 0},  //F
                                {0, 1, 0, 1, 0, 1, 0, 1},  //E
                                {1, 0, 1, 0, 1, 0, 1, 0},  //D
                                {0, 1, 0, 1, 0, 2, 0, 1},  //C
                                {1, 0, 1, 0, 2, 0, 1, 0},  //B
                                {0, 1, 0, 1, 0, 1, 0, 1}}, //A
                               //1  2  3  4  5  6  7  8
                        Cell.fromString("F, 5"),
                        Cell.fromString("H, 7")
                },
                {
                        "Game should end if only whites left on the board & blacks have no space to go.",
                        true,
                        new byte[][] {
                               //1  2  3  4  5  6  7  8
                                {1, 0, 1, 0, 1, 0, 1, 0},  //H
                                {0, 1, 0, 1, 0, 4, 0, 1},  //G
                                {1, 0, 1, 0, 2, 0, 1, 0},  //F
                                {0, 1, 0, 1, 0, 1, 0, 1},  //E
                                {1, 0, 1, 0, 1, 0, 1, 0},  //D
                                {0, 3, 0, 2, 0, 1, 0, 1},  //C
                                {1, 0, 1, 0, 3, 0, 1, 0},  //B
                                {0, 1, 0, 2, 0, 4, 0, 1}}, //A
                               //1  2  3  4  5  6  7  8
                        Cell.fromString("F, 5"),
                        Cell.fromString("H, 7")
                },

                {
                        "Game should end if only black figures left on the board.",
                        false,
                        new byte[][] {
                               //1  2  3  4  5  6  7  8
                                {1, 0, 1, 0, 4, 0, 1, 0},  //H
                                {0, 1, 0, 2, 0, 1, 0, 1},  //G
                                {1, 0, 1, 0, 4, 0, 1, 0},  //F
                                {0, 1, 0, 1, 0, 1, 0, 1},  //E
                                {1, 0, 1, 0, 5, 0, 1, 0},  //D
                                {0, 1, 0, 1, 0, 1, 0, 1},  //C
                                {1, 0, 5, 0, 1, 0, 1, 0},  //B
                                {0, 1, 0, 1, 0, 1, 0, 1}}, //A
                               //1  2  3  4  5  6  7  8
                        Cell.fromString("F, 5"),
                        Cell.fromString("H, 3")
                },
                {
                        "Game should end if only blacks left on the board & whites have no space to go.",
                        false,
                        new byte[][] {
                               //1  2  3  4  5  6  7  8
                                {1, 0, 1, 0, 1, 0, 1, 0},  //H
                                {0, 1, 0, 2, 0, 1, 0, 1},  //G
                                {1, 0, 1, 0, 4, 0, 1, 0},  //F
                                {0, 1, 0, 1, 0, 1, 0, 1},  //E
                                {1, 0, 1, 0, 1, 0, 1, 0},  //D
                                {0, 4, 0, 1, 0, 4, 0, 1},  //C
                                {1, 0, 5, 0, 5, 0, 1, 0},  //B
                                {0, 1, 0, 3, 0, 1, 0, 1}}, //A
                               //1  2  3  4  5  6  7  8
                        Cell.fromString("F, 5"),
                        Cell.fromString("H, 3")
                }
        };
    }

    @Test(groups = GAME_END_GROUP, dataProvider = GAME_SHOULD_END_DATA_PROVIDER)
    public void gameShouldEnd(String message,
                              boolean isWhitesTurn,
                              byte[][] fieldInitialState,
                              Cell from,
                              Cell to) throws Exception {
        Game game = Game.newGame();
        game.getContext().getField().magicUpdate(fieldInitialState);
        game.getContext().setWhitesTurn(isWhitesTurn);
        game.getContext().getStateAnalyzer().updateDataForAnalysis();
        game.go(from, to);

        Assert.assertTrue(game.isWhitesTurn() ? game.isWhitesWin() : game.isBlacksWin(), message);
    }

    @DataProvider(name = PUNISH_PLAYER_DATA_PROVIDER)
    public Object[][] punishPlayerDataProvider() throws Exception {
        return new Object[][] {
                {
                    "Punish whites player if he forgot to kill.",
                        true,
                        new byte[][] {
                               //1  2  3  4  5  6  7  8
                                {1, 0, 1, 0, 1, 0, 1, 0},  //H
                                {0, 4, 0, 5, 0, 1, 0, 1},  //G
                                {2, 0, 3, 0, 1, 0, 1, 0},  //F
                                {0, 1, 0, 1, 0, 1, 0, 1},  //E
                                {1, 0, 1, 0, 1, 0, 1, 0},  //D
                                {0, 4, 0, 5, 0, 1, 0, 1},  //C
                                {3, 0, 2, 0, 1, 0, 1, 0},  //B
                                {0, 1, 0, 1, 0, 1, 0, 1}}, //A
                               //1  2  3  4  5  6  7  8
                        Cell.fromStrings("F, 1", "F, 3", "B, 1", "B, 3"),
                        Cell.fromStrings("E, 2", "E, 4", "A, 2", "A, 4"),
                        Cell.fromStrings("F, 1", "F, 3", "B, 1", "B, 3")
                },
                {
                    "Punish blacks player if he forgot to kill.",
                        false,
                        new byte[][] {
                               //1  2  3  4  5  6  7  8
                                {1, 0, 5, 0, 4, 0, 1, 0},  //H
                                {0, 1, 0, 2, 0, 1, 0, 1},  //G
                                {1, 0, 1, 0, 1, 0, 1, 0},  //F
                                {0, 2, 0, 1, 0, 1, 0, 1},  //E
                                {1, 0, 1, 0, 3, 0, 1, 0},  //D
                                {0, 1, 0, 5, 0, 1, 0, 1},  //C
                                {1, 0, 1, 0, 1, 0, 1, 0},  //B
                                {0, 1, 0, 1, 0, 1, 0, 1}}, //A
                               //1  2  3  4  5  6  7  8
                        Cell.fromStrings("H, 3", "C, 4"),
                        Cell.fromStrings("F, 1", "B, 3"),
                        Cell.fromStrings("H, 5", "C, 4", "H, 3")
                }
        };
    }

    @Test(groups = PUNISH_PLAYER_GROUP, dataProvider = PUNISH_PLAYER_DATA_PROVIDER)
    public void punishPlayerTest(String message,
                                 boolean isWhitesTurn,
                                 byte[][] fieldInitialState,
                                 List<Cell> fromList,
                                 List<Cell> toList,
                                 List<Cell> removeList) throws Exception {
        for (int i = 0; i < fromList.size(); i++) {
            Cell from = fromList.get(i);
            Cell to = toList.get(i);

            Game game = Game.newGame();
            game.getContext().getField().magicUpdate(fieldInitialState);
            game.getContext().setWhitesTurn(isWhitesTurn);
            game.getContext().getStateAnalyzer().updateDataForAnalysis();
            game.go(from, to);

            List<Cell> punishedCells = new ArrayList<>(removeList);
            byte[][] expectedField = configureExpectedFieldBlackoutCells(fieldInitialState, punishedCells);

            assertTrue(Arrays.deepEquals(game.getField(), expectedField), message);
        }

    }

    private byte[][] configureExpectedField(byte[][] source, int[] pointA, byte valueA, int[] pointB, byte valueB) {
        byte[][] destination = new byte[source.length][source[0].length];
        for (int i = 0; i < source.length; i++) {
            for (int j = 0; j < source[i].length; j++) {
                destination[i][j] = source[i][j];
            }
        }
        destination[pointA[0]][pointA[1]] = valueA;
        destination[pointB[0]][pointB[1]] = valueB;
        return destination;
    }

    private byte[][] configureExpectedField(byte[][] source, int[] pointA, byte valueA, int[] pointB, byte valueB, int[] pointC, byte valueC) {
        byte[][] destination = new byte[source.length][source[0].length];
        for (int i = 0; i < source.length; i++) {
            for (int j = 0; j < source[i].length; j++) {
                destination[i][j] = source[i][j];
            }
        }
        destination[pointA[0]][pointA[1]] = valueA;
        destination[pointB[0]][pointB[1]] = valueB;
        destination[pointC[0]][pointC[1]] = valueC;
        return destination;
    }

    private byte[][] configureExpectedFieldBlackoutCells(byte[][] source, List<Cell> cellList) {
        byte[][] destination = new byte[source.length][source[0].length];
        for (int i = 0; i < source.length; i++) {
            for (int j = 0; j < source[i].length; j++) {
                destination[i][j] = source[i][j];
            }
        }
        for (Cell cell : cellList) {
            int[] indexes = cell.toIndexedCell();
            destination[indexes[0]][indexes[1]] = Field.BLACK_CELL_CODE;
        }
        return destination;
    }
}
