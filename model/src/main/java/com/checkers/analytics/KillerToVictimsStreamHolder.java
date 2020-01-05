package com.checkers.analytics;

import com.checkers.entity.Cell;
import com.checkers.util.BiPredicateExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KillerToVictimsStreamHolder {
    private Cell killer;
    private Stream<Cell> victims;

    private KillerToVictimsStreamHolder(Cell killer, Stream<Cell> victims) {
        this.killer = killer;
        this.victims = victims;
    }

    public static KillerToVictimsStreamHolderBuilder streamFor(Cell killer) {
        return new KillerToVictimsStreamHolderBuilder(killer);
    }

    public KillerToVictimsStreamHolder filter(BiPredicateExtension<Cell, Cell> predicate) {
        this.victims = victims.filter(predicate.reduce(killer));
        return this;
    }

    public Map<Cell, List<Cell>> collect() {
        return victims.collect(Collectors.toMap(key -> killer, this::asList, this::merge));
    }

    private List<Cell> asList(Cell cell) {
        List<Cell> cellList = new ArrayList<>();
        cellList.add(cell);
        return cellList;
    }

    private List<Cell> merge(List<Cell> added, List<Cell> toAdd) {
        added.addAll(toAdd);
        return added;
    }

    public static class KillerToVictimsStreamHolderBuilder {
        private Cell killer;

        private KillerToVictimsStreamHolderBuilder(Cell killer) {
            this.killer = killer;
        }

        public KillerToVictimsStreamHolder streamOf(List<Cell>... lists) {
            return new KillerToVictimsStreamHolder(killer, Stream.of(lists).flatMap(List::stream));
        }
    }
}
