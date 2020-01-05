package com.checkers.entity;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class KillersContract {
    private Map<Cell, List<Cell>> killerToVictimMap = new HashMap<>();
    private boolean isValid;
    private boolean isMet;

    public KillerAgreement addKiller(@NotNull Cell killer) {
        return new KillerAgreement(killer);
    }

    public List<Cell> getVictims(Cell killer) {
        return killerToVictimMap.get(killer);
    }

    // setters
    public void setIsMet(boolean isMet) {
        this.isMet = isMet;
    }

    public KillersContract sign() {
        this.isValid = true;
        return this;
    }

    public Set<Cell> getKillers() {
        return killerToVictimMap.keySet();
    }

    // booleans
    public boolean isMet() {
        return isMet;
    }

    public boolean isValid() {
        return isValid;
    }

    public class KillerAgreement {
        private Cell killer;
        private List<Cell> victims = new ArrayList<>();
        private Consumer<List<Cell>> agreement;

        public KillerAgreement(@NotNull Cell cell) {
            this.killer = cell;
            this.agreement = (victims) -> killerToVictimMap.put(killer, victims);
        }

        public KillerAgreement addVictims(@NotNull List<Cell> victims) {
            victims.addAll(victims);
            return this;
        }

        public KillersContract sign() {
            agreement.accept(victims);
            return KillersContract.this;
        }
    }
}
