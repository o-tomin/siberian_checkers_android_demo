package com.checkers.util;

import java.util.Arrays;

public class ArraysUtil {

    public static byte[][] copy(byte[][] array) {
        byte[][] newArray = new byte[array.length][];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = new byte[array[i].length];
            for (int j = 0; j < array[i].length; j++) {
                newArray[i][j] = array[i][j];
            }
        }
        return newArray;
    }

    public static void reset(byte[][] from, byte[][] to) {
        boolean failed = false;
        if (from.length == to.length) {
            for (int i = 0; i < from.length; i++) {
                if (from[i].length == to[i].length) {
                    for (int j = 0; j < from[i].length; j++) {
                        to[i][j] = from[i][j];
                    }
                } else {
                    failed = true;
                    break;
                }
            }
        } else {
            failed = true;
        }

        if (failed) {
            throw new RuntimeException(String.format("Failed to reset from %s to %s.", Arrays.toString(from),
                    Arrays.toString(to)));
        }
    }
}
