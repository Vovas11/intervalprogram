package com.example.intervalprogram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The subclass implements the method {@code processIntervals} using natural behavior of Set
 * that can not store identical numbers. In addition TreeSet provides an ordered (sorted) sequence.
 * Excluding intervals from others is executed by a Collection's method.
 * The method is fast and simple but can have high memory costs in case of long intervals
 */
public class IntervalHandlerBySet extends IntervalHandler {

    private static final int MONO = 0;
    private static final int INSIDE = 1;
    private static final int LOW_BOUND = 2;
    private static final int HIGH_BOUND = 3;


    @Override
    public List<Interval> processIntervals(List<Interval> includes, List<Interval> excludes) {
        TreeSet<Integer> includesOrdered = fillInWithIntegers(includes);
        TreeSet<Integer> excludesOrdered = fillInWithIntegers(excludes);
        // interact includes and excludes
        includesOrdered.removeAll(excludesOrdered);

        return buildIntervals(includesOrdered);
    }

    /**
     * Fills in a TreeSet with ranges of integers corresponding to given intervals
     *
     * @param includes list of intervals
     * @return ordered Set with integers incoming into intervals
     */
    private TreeSet<Integer> fillInWithIntegers(List<Interval> includes) {
        return includes.stream()
                    .flatMap(i -> IntStream.rangeClosed(i.getStart(), i.getEnd()).boxed())
                    .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Converts a given sorted set of integers into list of intervals
     *
     * @param sortedSet sorted set of integers
     * @return list of intervals
     */
    private List<Interval> buildIntervals(SortedSet<Integer> sortedSet) {
        if (sortedSet.isEmpty()) {
            return Collections.emptyList();
        }
        List<Integer> filledIntervals = new ArrayList<>(sortedSet);
        List<Interval> intervals = new ArrayList<>();

        int lowBound = 0;
        for (int i = 0; i < filledIntervals.size(); i++) {
            Integer current = filledIntervals.get(i);
            int type = getTypeOfAdjacent(filledIntervals, i);
            switch (type) {
                case MONO:
                    intervals.add(new Interval(current, current));
                    break;
                case LOW_BOUND:
                    lowBound = current;
                    break;
                case HIGH_BOUND:
                    intervals.add(new Interval(lowBound, current));
                    break;
            }
        }
        return intervals;
    }

    /**
     * Defines if an item found by a given index is start or end of an interval
     * or is inside an interval or represents a mono (degenerated) interval.
     *
     * @param integers list of sorted integers
     * @param index index of an item in the list to analyze
     * @return a constant corresponding to item position in interval
     */
    private int getTypeOfAdjacent(List<Integer> integers, int index) {
        int value = integers.get(index);
        int type = MONO;
        // start of the list
        if (index == 0) {
            if (integers.get(index + 1) == value + 1) {
                type = LOW_BOUND;
            }
            // end of the list
        } else if (index == integers.size() - 1) {
            if (integers.get(index - 1) == value - 1) {
                type = HIGH_BOUND;
            }
            // inside the list
        } else {
            int prev = integers.get(index - 1);
            int next = integers.get(index + 1);
            if (prev == value - 1 && next == value + 1) {
                type = INSIDE;
            } else if (next == value + 1) {
                type = LOW_BOUND;
            } else if (prev == value - 1) {
                type = HIGH_BOUND;
            }
        }
        return type;
    }
}
