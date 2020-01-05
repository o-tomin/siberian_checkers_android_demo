package com.checkers.entity;

import com.checkers.analytics.FieldStateAnalyzer;

import java.io.Serializable;

public class GameContext implements Serializable {
    private Field field;
    private FieldStateAnalyzer stateAnalyzer;
    private boolean isWhitesTurn;
    private boolean isWhitesInOuthouse;
    private boolean isWhitesWin;
    private boolean isBlacksInOuthouse;
    private boolean isBlacksWin;

    // setters
    public void setField(Field field) {
        this.field = field;
    }

    public void setWhitesTurn(boolean whitesTurn) {
        this.isWhitesTurn = whitesTurn;
    }

    public void setStateAnalyzer(FieldStateAnalyzer fieldStateAnalyzer) {
        this.stateAnalyzer = fieldStateAnalyzer;
    }

    // booleans
    public boolean isWhitesTurn() {
        return isWhitesTurn;
    }

    // getters
    public Field getField() {
        return field;
    }

    public FieldStateAnalyzer getStateAnalyzer() {
        return stateAnalyzer;
    }

    public void setWhitesInOuthouse(boolean isIn) {
        isWhitesInOuthouse = isIn;
    }

    public boolean isWhitesInOuthouse() {
        return isWhitesInOuthouse;
    }

    public void setWhitesWin(boolean isWhitesWin) {
        this.isWhitesWin = isWhitesWin;
    }

    public boolean isWhitesWin() {
        return isWhitesWin;
    }

    public void setBlackInOuthouse(boolean isIn) {
        isBlacksInOuthouse = isIn;
    }

    public boolean isBlacksInOuthouse() {
        return isBlacksInOuthouse;
    }

    public void setBlacksWin(boolean isBlacksWin) {
        this.isBlacksWin = true;
    }

    public boolean isBlacksWin() {
        return isBlacksWin;
    }

    @Override
    public String toString() {
        return "GameContext{" +
                "field=" + field +
                ", stateAnalyzer=" + stateAnalyzer +
                ", isWhitesTurn=" + isWhitesTurn +
                ", isWhitesInOuthouse=" + isWhitesInOuthouse +
                ", isWhitesWin=" + isWhitesWin +
                ", isBlacksInOuthouse=" + isBlacksInOuthouse +
                ", isBlacksWin=" + isBlacksWin +
                '}';
    }
}
