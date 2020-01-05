package com.checkers.view;

public class FigureTag {
    private final MainActivity.Figure figure;
    private final int drawableId;

    public FigureTag(MainActivity.Figure figure, int drawableId) {
        this.figure = figure;
        this.drawableId = drawableId;
    }

    public MainActivity.Figure getFigure() {
        return figure;
    }

    public int getDrawableId() {
        return drawableId;
    }
}
