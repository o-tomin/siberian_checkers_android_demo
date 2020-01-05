package com.checkers.helper;

import android.widget.ImageButton;

import com.checkers.view.FigureTag;
import com.checkers.view.MainActivity;
import com.checkers.R;
import com.checkers.viewmodel.GameViewModel;

import java.util.Objects;

import static com.checkers.viewmodel.GameViewModel.CELL_ID_TO_FIELD_LABEL;

public class GameSetupHelper {
    private final MainActivity activity;
    private final GameViewModel gameViewModel;

    public GameSetupHelper(MainActivity activity, GameViewModel gameViewModel) {
        this.activity = activity;
        this.gameViewModel = gameViewModel;
    }

    public void setUp(int[] whitesIds, int[] blacksIds, int[] emptyIds) {
        setUpWhites(whitesIds);
        setUpBlacks(blacksIds);
        setUpEmptyCells(emptyIds);
    }

    private void setUpWhites(int[] ids) {
        setFiguresOnField(ids, new FigureTag(MainActivity.Figure.WHITE_FIGURE, R.drawable.white_figure));
    }

    private void setUpBlacks(int[] ids) {
        setFiguresOnField(ids, new FigureTag(MainActivity.Figure.BLACK_FIGURE, R.drawable.black_figure));
    }

    private void setUpEmptyCells(int[] ids) {
        for (int id : ids) {
            ImageButton cell = activity.findViewById(id);
            cell.setTag(R.string.letter, Objects.toString(CELL_ID_TO_FIELD_LABEL.get(id)));
            gameViewModel.removeFigure(cell);
        }
    }

    private void setFiguresOnField(int[] cells, FigureTag tag) {
        for (int id : cells) {
            ImageButton cell = activity.findViewById(id);
            cell.setImageResource(tag.getDrawableId());
            cell.setTag(tag);
            cell.setTag(R.string.letter, Objects.toString(CELL_ID_TO_FIELD_LABEL.get(id)));
        }
    }
}
