package com.checkers.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.Toast;

import com.checkers.R;
import com.checkers.animation.AnimatedStep;
import com.checkers.helper.GameSetupHelper;
import com.checkers.helper.ViewLocationHelper;
import com.checkers.viewmodel.FieldSelectionViewModel;
import com.checkers.viewmodel.GameViewModel;

public class MainActivity extends AppCompatActivity {

    public enum Figure {
        BLACK_FIGURE, WHITE_FIGURE, BLACK_QUEEN, WHITE_QUEEN
    }

    private static final int[] TOP_INITIAL_CELLS = new int[] {
            R.id.black61, R.id.black63, R.id.black65, R.id.black67,
            R.id.black72, R.id.black74, R.id.black76, R.id.black78,
            R.id.black81, R.id.black83, R.id.black85, R.id.black87};

    private static final int[] BOTTOM_INITIAL_CELLS = new int[]
            {R.id.black12, R.id.black14, R.id.black16, R.id.black18,
            R.id.black21, R.id.black23, R.id.black25, R.id.black27,
            R.id.black32, R.id.black34, R.id.black36, R.id.black38};

    private static final int[] INITIALLY_EMPTY_CELLS = new int[] {
            R.id.black41, R.id.black43, R.id.black45, R.id.black47,
            R.id.black52, R.id.black54, R.id.black56, R.id.black58,
    };

    private static final String ILLEGAL_STEP_TOAS_MESSAGE = "Illegal step.";

    private FieldSelectionViewModel fieldSelectionViewModel;
    private GameViewModel gameViewModel;
    private AnimatedStep animatedStep;
    private GameSetupHelper gameSetupHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fieldSelectionViewModel = ViewModelProviders.of(this).get(FieldSelectionViewModel.class);
        gameViewModel = ViewModelProviders.of(this).get(GameViewModel.class);
        animatedStep = new AnimatedStep(
                findViewById(R.id.root),
                new ViewLocationHelper(getWindow()),
                (Animation a) -> gameViewModel.updateField(this));
        gameSetupHelper = new GameSetupHelper(this, gameViewModel);
    }

    public void onNewGameButtonClicked(View view) {
        fieldSelectionViewModel.clearSelection();
        gameSetupHelper.setUp(BOTTOM_INITIAL_CELLS, TOP_INITIAL_CELLS ,INITIALLY_EMPTY_CELLS);
        gameViewModel.initNewGame();
    }

    public void onCellClicked(View view) {
        updateSelectionModel((ImageButton) view);

        if (fieldSelectionViewModel.isToCellSelected() && fieldSelectionViewModel.isFromCellSelected()) {
            ImageButton from = fieldSelectionViewModel.getSelectedFromCell();
            ImageButton to = fieldSelectionViewModel.getSelectedToCell();
            if (gameViewModel.go(from, to)) {
                animatedStep.moveFigure(fieldSelectionViewModel.getSelectedFromCell(), fieldSelectionViewModel.getSelectedToCell());
                fieldSelectionViewModel.clearSelection();
            } else {
                showToast(ILLEGAL_STEP_TOAS_MESSAGE);
                fieldSelectionViewModel.clearToCellSelection();
            }
        }

        if(gameViewModel.isWinnerDefined()) {
            if (gameViewModel.isWhitesWin()) {
                showToast("Green player WIN!!!!");
            }
            if (gameViewModel.isBlacksWin()) {
                showToast("Violet player WIN!!!");
            }
        }
    }

    public void showToast(String textToShow) {
        Toast toast = Toast.makeText(getApplicationContext(), textToShow, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void updateSelectionModel(ImageButton cell) {
        if (!fieldSelectionViewModel.isFromCellSelected()) {
            FigureTag tag = (FigureTag) cell.getTag();
            if (tag != null) {
                Figure figure = tag.getFigure();
                if (gameViewModel.isWhitesTurn() && (figure == Figure.WHITE_FIGURE || figure == Figure.WHITE_QUEEN)) {
                    fieldSelectionViewModel.selectFromCell(cell);
                } else if (!gameViewModel.isWhitesTurn() && (figure == Figure.BLACK_FIGURE || figure == Figure.BLACK_QUEEN)) {
                    fieldSelectionViewModel.selectFromCell(cell);
                } else {
                    showToast("Not your figure!");
                }
            }
        } else {
            if (fieldSelectionViewModel.getSelectedFromCell().equals(cell)) {
                fieldSelectionViewModel.clearFromCellSelection();
            } else {
                fieldSelectionViewModel.selectToCell(cell);
            }
        }
    }
}
