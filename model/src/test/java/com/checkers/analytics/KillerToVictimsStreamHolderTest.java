package com.checkers.analytics;

import com.checkers.entity.Cell;
import com.checkers.util.BiPredicateExtension;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.*;

public class KillerToVictimsStreamHolderTest {

    @Mock
    private Cell killerCell;
    @Mock
    private Cell victimCell;

    private KillerToVictimsStreamHolder streamHolder;
    private List<Cell> victimsList;

    @BeforeMethod
    public void init() {
        initMocks(this);
        victimsList = Arrays.asList(victimCell);
        streamHolder = KillerToVictimsStreamHolder.streamFor(killerCell)
                .streamOf(victimsList);
    }

    @Test
    public void filterTrueTest() {
        BiPredicateExtension truePredicate = (killer, victim) -> true;
        Map<Cell, List<Cell>> attackPossible = streamHolder.filter(truePredicate).collect();
        List<Cell> victims = attackPossible.get(killerCell);

        assertNotNull(victims);
        assertEquals(attackPossible.size(), 1);
        assertEquals(victims.iterator().next(), victimCell);
    }

    @Test
    public void filterFalseTest() {
        BiPredicateExtension falsePredicate = (killer, victim) -> false;
        Map<Cell, List<Cell>> attackImpossible = streamHolder.filter(falsePredicate).collect();

        assertTrue(attackImpossible.isEmpty());
    }

    @Test
    public void collectTest() {
        Map<Cell, List<Cell>> killerToVictimsMap = streamHolder.collect();
        List<Cell> victims = killerToVictimsMap.get(killerCell);

        assertNotNull(victims);
        assertEquals(victims, victimsList);
    }

}
