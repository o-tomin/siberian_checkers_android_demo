package com.checkers.viewmodel;

import android.widget.ImageButton;
import androidx.lifecycle.ViewModel;

import com.checkers.view.FigureTag;

import org.jetbrains.annotations.NotNull;

public class FieldSelectionViewModel extends ViewModel {
    private ImageButton fromCell;
    private ImageButton toCell;

    public void selectFromCell(@NotNull ImageButton cell) {
        if (isCellOccupied(cell)) {
            if (fromCell != null) {
                clearSelection();
            }
            cell.setActivated(true);
            fromCell = cell;
        }
    }

    public void selectToCell(@NotNull ImageButton cell) {
        if (fromCell != null && fromCell.isActivated()) {
            if (toCell != null) {
                toCell.setActivated(false);
                toCell = null;
            }
            cell.setActivated(true);
            toCell = cell;
        }
    }

    public void clearFromCellSelection() {
        if (fromCell != null) {
            fromCell.setActivated(false);
            fromCell = null;
        }
    }

    public void clearToCellSelection() {
        if (toCell != null) {
            toCell.setActivated(false);
            toCell = null;
        }
    }

    public void clearSelection() {
        clearFromCellSelection();
        clearToCellSelection();
    }

    private boolean isCellOccupied(ImageButton cell) {
        Object tag = cell.getTag();
        if (tag != null && tag.getClass() == FigureTag.class) {
            return  ((FigureTag) tag).getFigure() != null;
        }
        return false;
    }

    public boolean isFromCellSelected() {
        return fromCell != null && fromCell.isActivated();
    }

    public boolean isToCellSelected() {
        return toCell != null && toCell.isActivated();
    }

    public ImageButton getSelectedFromCell() {
        return fromCell;
    }

    public ImageButton getSelectedToCell() {
        return toCell;
    }
}
