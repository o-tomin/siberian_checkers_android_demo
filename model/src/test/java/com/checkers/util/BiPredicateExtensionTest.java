package com.checkers.util;

import com.checkers.entity.Cell;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.function.Predicate;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;

public class BiPredicateExtensionTest {

    @Mock
    private Cell cellOne;
    @Mock
    private Cell cellTwo;

    @BeforeMethod
    public void init() {
        initMocks(this);
    }

    @Test
    public void reduceTest() {
        Cell[] firstParameter = new Cell[1];
        Cell[] secondParameter = new Cell[1];
        BiPredicateExtension<Cell, Cell> biPredicate = (parameterOne, parameterTwo) -> {
            firstParameter[0] = parameterOne;
            secondParameter[0] = parameterTwo;
            return true;
        };
        Predicate<Cell> predicate = biPredicate.reduce(cellOne);
        predicate.test(cellTwo);

        assertEquals(firstParameter[0], cellOne);
        assertEquals(secondParameter[0], cellTwo);
    }
}
