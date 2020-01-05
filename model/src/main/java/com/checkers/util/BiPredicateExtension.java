package com.checkers.util;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

@FunctionalInterface
public interface BiPredicateExtension<P1, P2> extends BiPredicate<P1, P2> {

    default Predicate<P2> reduce(P1 parameter1) {
        return (P2 parameter2) -> test(parameter1, parameter2);
    }
}
