package com.checkers.util;


public interface TriplePredicate<P1, P2, P3> {
    boolean test(P1 p1, P2 p2, P3 p3);
}
