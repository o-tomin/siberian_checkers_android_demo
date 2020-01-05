package com.checkers.viewmodel;

import android.util.Log;
import android.widget.ImageButton;

import androidx.lifecycle.ViewModel;

import com.checkers.view.FigureTag;
import com.checkers.Game;
import com.checkers.view.MainActivity;
import com.checkers.R;
import com.checkers.entity.Cell;
import com.checkers.entity.Field;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GameViewModel extends ViewModel {
    private static final String TAG = "[CHESS][GameViewModel]";
    public static final Map<Integer, Cell> CELL_ID_TO_FIELD_LABEL = Collections.unmodifiableMap(
            new HashMap<Integer, Cell>() {
                {
                    put(R.id.black12, fromString("B, 1"));
                    put(R.id.black14, fromString("D, 1"));
                    put(R.id.black16, fromString("F, 1"));
                    put(R.id.black18, fromString("H, 1"));
                    put(R.id.black21, fromString("A, 2"));
                    put(R.id.black23, fromString("C, 2"));
                    put(R.id.black25, fromString("E, 2"));
                    put(R.id.black27, fromString("G, 2"));
                    put(R.id.black32, fromString("B, 3"));
                    put(R.id.black34, fromString("D, 3"));
                    put(R.id.black36, fromString("F, 3"));
                    put(R.id.black38, fromString("H, 3"));
                    put(R.id.black41, fromString("A, 4"));
                    put(R.id.black43, fromString("C, 4"));
                    put(R.id.black45, fromString("E, 4"));
                    put(R.id.black47, fromString("G, 4"));
                    put(R.id.black52, fromString("B, 5"));
                    put(R.id.black54, fromString("D, 5"));
                    put(R.id.black56, fromString("F, 5"));
                    put(R.id.black58, fromString("H, 5"));
                    put(R.id.black61, fromString("A, 6"));
                    put(R.id.black63, fromString("C, 6"));
                    put(R.id.black65, fromString("E, 6"));
                    put(R.id.black67, fromString("G, 6"));
                    put(R.id.black72, fromString("B, 7"));
                    put(R.id.black74, fromString("D, 7"));
                    put(R.id.black76, fromString("F, 7"));
                    put(R.id.black78, fromString("H, 7"));
                    put(R.id.black81, fromString("A, 8"));
                    put(R.id.black83, fromString("C, 8"));
                    put(R.id.black85, fromString("E, 8"));
                    put(R.id.black87, fromString("G, 8"));
                }
            }
    );

    private Game game;

    public GameViewModel() {
        this.game = Game.newGame();
    }

    public boolean go(ImageButton from, ImageButton to) {
        try {
            Cell fromCell = Cell.fromString((String) from.getTag(R.string.letter));
            Cell toCell = Cell.fromString((String) to.getTag(R.string.letter));
            if (game.getStepsFor(fromCell).contains(toCell)) {
                game.go(fromCell, toCell);
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "[go]", e);
        }
        return false;
    }

    public void updateField(MainActivity activity) {
        Field field = game.getContext().getField();
        field.forEachCell( cell -> {
            if (!field.isWhiteCell(cell)) {
                int resourceId = getResourceIdByCell(cell);
                ImageButton viewCell = activity.findViewById(resourceId);
                if (field.isBlackFigure(cell)) {
                    viewCell.setImageResource(R.drawable.black_figure);
                    viewCell.setTag(new FigureTag(MainActivity.Figure.BLACK_FIGURE, R.drawable.black_figure));
                } else if (field.isWhiteFigure(cell)) {
                    viewCell.setImageResource(R.drawable.white_figure);
                    viewCell.setTag(new FigureTag(MainActivity.Figure.WHITE_FIGURE, R.drawable.white_figure));
                } else if (field.isBlackQueen(cell)) {
                    viewCell.setImageResource(R.drawable.black_queen);
                    viewCell.setTag(new FigureTag(MainActivity.Figure.BLACK_QUEEN, R.drawable.black_queen));
                } else if (field.isWhiteQueen(cell)) {
                    viewCell.setImageResource(R.drawable.white_queen);
                    viewCell.setTag(new FigureTag(MainActivity.Figure.WHITE_QUEEN, R.drawable.white_queen));
                } else {
                    removeFigure(viewCell);
                }
                viewCell.refreshDrawableState();
            }
        });
    }

    public void removeFigure(ImageButton cell) {
        cell.setImageResource(android.R.color.transparent);
        cell.setTag(null);
        cell.refreshDrawableState();
    }

    private static int getResourceIdByCell(Cell cell) {
        for (Map.Entry<Integer, Cell> entry : CELL_ID_TO_FIELD_LABEL.entrySet()) {
            if (entry.getValue().equals(cell)) {
                return entry.getKey();
            }
        }
        Log.e(TAG, "[getResourceIdByCell][ERROR] Cant get Resource id for cell: " + cell
                + ". Application will not work correctly!");
        return Integer.MIN_VALUE;
    }

    private static Cell fromString(String boardLabels) {
        try {
            return Cell.fromString(boardLabels);
        } catch (Exception e) {
            Log.e(TAG, "[fromString][ERROR] Cell configuration wrong! Application will not work correctly!", e);
        }
        throw new RuntimeException();
    }

    public void initNewGame() {
        game = Game.newGame();
    }

    public boolean isWinnerDefined() {
        return game.isWhitesWin() || game.isBlacksWin();
    }

    public boolean isWhitesWin() {
        return game.isWhitesWin();
    }

    public boolean isBlacksWin() {
        return game.isBlacksWin();
    }

    public boolean isWhitesTurn() {
        return game.isWhitesTurn();
    }
}
