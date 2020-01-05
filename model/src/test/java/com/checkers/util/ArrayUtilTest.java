package com.checkers.util;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

public class ArrayUtilTest {

    private final byte[][] TEST_FIELD = {
           //1  2  3  4  5  6  7  8
            {2, 0, 2, 0, 1, 0, 4, 0}, //H
            {0, 2, 0, 1, 0, 4, 0, 4}, //G
            {2, 0, 2, 0, 1, 0, 4, 0}, //F
            {0, 2, 0, 1, 0, 4, 0, 4}, //E
            {2, 0, 2, 0, 1, 0, 4, 0}, //D
            {0, 2, 0, 1, 0, 4, 0, 4}, //C
            {2, 0, 2, 0, 1, 0, 4, 0}, //B
            {0, 2, 0, 1, 0, 4, 0, 4}};//A
           //1  2  3  4  5  6  7  8

    @Test
    public void copyTest() {
        Assert.assertTrue(Arrays.deepEquals(ArraysUtil.copy(TEST_FIELD), TEST_FIELD));
    }
}
